package org.usfirst.frc862.sirius.subsystems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONObject;
import org.usfirst.frc862.jlib.collection.DoubleLookupTable;
import org.usfirst.frc862.jlib.math.interp.LinearInterpolator;
import org.usfirst.frc862.sirius.Robot;
import org.usfirst.frc862.sirius.subsystems.Pivot.PowerTableValue;
import org.json.JSONArray;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Vision extends Subsystem {
    private ReadWriteLock rwl;
    private double distance_to_target;
    private double theta_to_target;
    
    private Thread vthread;
    private LinearInterpolator interplator;
    private DoubleLookupTable<ThetaDistanceTableValue> thetaDistanceTable;
    
    private static String httpGET(String urlToRead) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }
    
    private static JSONObject httpJSON(String url) {
        String get = httpGET(url);
        System.out.println("Vision get");
        System.out.println(get);
        
        JSONArray ja = new JSONArray(get);
        if (ja.length() > 0) {
            return ja.getJSONObject(0);
        }
        
        return null;
    }
    
    public double getDistanceToTarget() {
        rwl.readLock().lock();
        double result = distance_to_target;
        rwl.readLock().unlock();
        return result;
    }
    
    public double getThetaToTarget() {
        rwl.readLock().lock();
        double result = theta_to_target;
        rwl.readLock().unlock();
        return result;
    }
    
    public double getInterpolatedPivot() {
        double dist = getDistanceToTarget();
        
        // find floor/ceiling values
        DoubleLookupTable<ThetaDistanceTableValue>.DoubleValuePair floor = thetaDistanceTable.floorEntry(dist);
        DoubleLookupTable<ThetaDistanceTableValue>.DoubleValuePair ceil = thetaDistanceTable.ceilingEntry(dist);
        if (ceil == null) {
            ceil = thetaDistanceTable.lastEntry();
        }
        if (floor == null) {
            floor = thetaDistanceTable.firstEntry();
        }

        // Pull angle and values from the ceil and floor
        double floorDistance = floor.getKey();
        double floorValue = floor.getValue().theta;
        double ceilDistance = ceil.getKey();
        double ceilValue = ceil.getValue().theta;

        // interpolate to position
        return interplator.interpolate(floorDistance, ceilDistance, dist, floorValue, ceilValue);
    }
    
    public Vision() {
        super();
        
        interplator = new LinearInterpolator();
        thetaDistanceTable = new DoubleLookupTable<>(Robot.configuration.thetaDistanceTable.length);
        for (ThetaDistanceTableValue val : Robot.configuration.thetaDistanceTable) {
            thetaDistanceTable.put(val.distance, val);
        }

        System.out.println("Start vision");
        rwl = new ReentrantReadWriteLock();
        
        // create a thread here to poll against
        // the vision server, update distance_to_target
        // and theta_to_target variables

        vthread = new Thread() {
            public void run() {

                while (true) {
                    try {
                        JSONObject json = httpJSON("http://vision.local:5801/last_match_data");

                        if (json != null) {
                            double z = json.getDouble("z");
                            double x = json.getDouble("x");
                            double theta = Math.atan2(x, z) * -360 / (2 * Math.PI);

                            rwl.writeLock().lock();
                            distance_to_target = z;
                            theta_to_target = theta;
                            rwl.writeLock().unlock();
                        }
                        
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        vthread.start();
    }

    public boolean isAlive() {
        return vthread.isAlive();
    }
    
    public void initDefaultCommand() {
    }
    
    public static class ThetaDistanceTableValue {
        public double distance;
        public double theta;

        public ThetaDistanceTableValue() {
        } // Needed for serialization

        public ThetaDistanceTableValue(double d, double t) {
            distance = d;
            theta = t;
        }
    }
}

