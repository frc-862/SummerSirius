package org.usfirst.frc862.sirius.subsystems;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.FileNotFoundException;

import org.json.JSONObject;
import org.usfirst.frc862.jlib.collection.DoubleLookupTable;
import org.usfirst.frc862.jlib.math.interp.LinearInterpolator;
import org.usfirst.frc862.sirius.Robot;
import org.usfirst.frc862.sirius.subsystems.Pivot.PowerTableValue;
import org.json.JSONArray;
import org.json.JSONException;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Vision extends Subsystem {
    private ReadWriteLock rwl;
    private double z;
    private double x;
    private long timestamp;
    private double lastread;


    private Thread vthread;
    private LinearInterpolator interplator;
    private DoubleLookupTable<ThetaDistanceTableValue> thetaDistanceTable;

    public void resetVisionStatus() {
        rwl.readLock().lock();
        lastread = 0;
        rwl.readLock().unlock();        
    }
    
    public boolean hasGoodData() {
        double time = lastVisionTime();
        
        if (time == 0) return false;
        if (Timer.getFPGATimestamp() - lastread > 4) return false;
        
        return true;
    }

    public double lastVisionTime() {
        rwl.readLock().lock();
        double result = lastread;
        rwl.readLock().unlock();
        return result;
    }
    
    public long getTimestamp() {
        rwl.readLock().lock();
        long result = timestamp;
        rwl.readLock().unlock();
        return result;
    }

    private static String httpGET(String urlToRead) throws FileNotFoundException {
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
        } catch (UnknownHostException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        return "";
    }

    private static JSONObject httpJSON(String url) throws FileNotFoundException {
        String get = httpGET(url);

        JSONArray ja = new JSONArray(get);
        if (ja.length() > 0) {
            return ja.getJSONObject(0);
        }

        return null;
    }

    public double getDistanceToTarget() {
        rwl.readLock().lock();
        double result = z;
        rwl.readLock().unlock();
        return result;
    }

    public double getThetaToTarget() {
        final double FUDGE = 1; // -3

        rwl.readLock().lock();
        double lx = x;
        double lz = z;
        rwl.readLock().unlock();

        System.out.println("x: " + lx);
        System.out.println("z: " + lz);
        double theta = (Math.atan2(lx, lz) * -360 / (2 * Math.PI)) + FUDGE;
        System.out.println("theta: " + theta);

        return theta;
    }

    public double getInterpolatedPivot() {
        double dist = getDistanceToTarget();

        if (dist == 0) {
            return 0;
        }

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
                            rwl.writeLock().lock();
                            z = json.getDouble("z");
                            x = json.getDouble("x");
                            timestamp = json.getLong("timestamp");
                            lastread = Timer.getFPGATimestamp();
                            rwl.writeLock().unlock();
                        }

                        Thread.sleep(500);
                    } catch (FileNotFoundException e) {
                        rwl.writeLock().lock();
                        z = 0;
                        x = 0;
                        rwl.writeLock().unlock();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e2) {
                            // do nothing
                        }
                    } catch (JSONException e) {
                        rwl.writeLock().lock();
                        z = 0;
                        x = 0;
                        rwl.writeLock().unlock();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e2) {
                            // do nothing
                        }
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
