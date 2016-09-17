package org.usfirst.frc862.sirius.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.usfirst.frc862.sirius.subsystems.Pivot.PowerTableValue;
import org.usfirst.frc862.sirius.subsystems.Vision.ThetaDistanceTableValue;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Configuration implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public PowerTableValue[] pivotPowerTable;	
    public ThetaDistanceTableValue[] thetaDistanceTable;
    public double obstacleLightLevel;
    public double crossObstacleTimeout;
    public double timeToRaiseArm;

    public double timeToLowerArm;

    public double hardstopResetTimeout;

    public double rotateP;
    public double rotateI;
    public double rotateD;

    public int rotateToleranceBuffer;
    public double rotateTolerance;

    public double nAutonSeconds;
	
	/**
	 * Create a configuration with all state being zero or null
	 */
	public Configuration() {
		this(false);
	}
	
	/**
	 * Create a configuration with default values
	 * 
	 * @param sensibleDefaults - If true, use sensible defaults, otherwise, use 0/null
	 */
	public Configuration(boolean sensibleDefaults) {
		if(sensibleDefaults) {
		    setReasonableDefaults();
		}
	}
	
	public void setReasonableDefaults() {
        pivotPowerTable = new PowerTableValue[] {
                new PowerTableValue(-180, -0.3, 0.25, 0.0),
                new PowerTableValue(0, -0.3, 0.25, 0.0),
                new PowerTableValue(10, -0.4, 0.25, -0.25),
                new PowerTableValue(40, -0.6, 0.15, -0.3),
                new PowerTableValue(180, -0.7, 0.15, -0.3)
        };
        
        thetaDistanceTable = new ThetaDistanceTableValue[] {
                new ThetaDistanceTableValue(1.8, 42),
                new ThetaDistanceTableValue(2.3, 34),
                new ThetaDistanceTableValue(2.9, 28),
                new ThetaDistanceTableValue(3.5, 25),
                new ThetaDistanceTableValue(4.6, 22)
        };
        obstacleLightLevel = 0.25;
        this.crossObstacleTimeout = 7.5;
        this.timeToRaiseArm = 1.5;
        this.timeToLowerArm = 1.0;
        this.hardstopResetTimeout = 3;
        this.rotateP = 0.2;
        this.rotateI = 0;
        this.rotateD = 0;
        this.rotateToleranceBuffer = 3;
        this.rotateTolerance = 1;
        this.nAutonSeconds = 6.0;
	}
	
	public static Configuration deserialize(File file) throws IOException {
		Yaml yaml = new Yaml();
		
		FileReader fr = new FileReader(file);
		Configuration config = yaml.loadAs(fr, Configuration.class);
		fr.close();
		
		return config;
	}
	
	public void serialize(File file) throws IOException {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		options.setPrettyFlow(true);
		Yaml yaml = new Yaml(options);
		
		FileWriter fw = new FileWriter(file);
		yaml.dump(this, fw);
		fw.close();
	}

}
