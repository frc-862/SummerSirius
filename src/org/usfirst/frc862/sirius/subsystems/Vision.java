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

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Vision extends Subsystem {
    private ReadWriteLock rwl;
    private double distance_to_target;
    private double theta_to_target;
    
    private Thread vthread;
    
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
        return new JSONObject(httpGET(url));
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
    
    public Vision() {
        super();
        rwl = new ReentrantReadWriteLock();
        
        // create a thread here to poll against
        // the vision server, update distance_to_target
        // and theta_to_target variables

        vthread = new Thread() {
            public void run() {

                while (true) {
                    try {
                        JSONObject json = httpJSON("http://vision.local:5801/");
                
                        rwl.writeLock().lock();
                        distance_to_target = json.getDouble("");
                        theta_to_target = json.getDouble("");
                        rwl.writeLock().unlock();
                        
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
}

