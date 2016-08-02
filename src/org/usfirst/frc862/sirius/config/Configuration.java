package org.usfirst.frc862.sirius.config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.usfirst.frc862.sirius.subsystems.Pivot.PowerTableValue;

public class Configuration implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public PowerTableValue[] pivotPowerTable;
	
	public boolean debuggingPivot = false;
	
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
			pivotPowerTable = new PowerTableValue[] {
					new PowerTableValue(-180, -0.3, 0.15, 0.0),
					new PowerTableValue(0, -0.3, 0.15, 0.0),
					new PowerTableValue(10, -0.4, 0.15, -0.25),
					new PowerTableValue(40, -0.5, 0.15, -0.3),
					new PowerTableValue(180, -0.5, 0.15, -0.3)
			};
		}
	}
	
	public static Configuration deserialize(File file) throws IOException {
		// Yaml yaml = new Yaml();
		
		// FileReader fr = new FileReader(file);
		// Configuration config = yaml.loadAs(fr, Configuration.class);
		// fr.close();
		
		return new Configuration(true);
	}
	
	public void serialize(File file) throws IOException {
//		DumperOptions options = new DumperOptions();
//		options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
//		options.setPrettyFlow(true);
//		Yaml yaml = new Yaml(options);
//		
//		FileWriter fw = new FileWriter(file);
//		yaml.dump(this, fw);
//		fw.close();
	}

}
