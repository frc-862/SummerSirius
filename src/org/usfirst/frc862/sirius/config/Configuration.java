package org.usfirst.frc862.sirius.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.usfirst.frc862.sirius.subsystems.Pivot.PowerTableValue;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

public class Configuration implements Serializable {
	
	public List<PowerTableValue> pivotPowerTable;
	
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
			pivotPowerTable = Arrays.asList(
					new PowerTableValue(-180, -0.3, 0.15, 0.0),
					new PowerTableValue(0, -0.3, 0.15, 0.0),
					new PowerTableValue(10, -0.4, 0.15, -0.25),
					new PowerTableValue(40, -0.5, 0.15, -0.3),
					new PowerTableValue(180, -0.5, 0.15, -0.3)
					);
		}
	}
	
	public static Configuration deserialize(File file) throws FileNotFoundException, YamlException {
		YamlReader reader = new YamlReader(new FileReader(file));
		return reader.read(Configuration.class);
	}
	
	public void serialize(File file) throws IOException {
		YamlWriter writer = new YamlWriter(new FileWriter(file));
		writer.write(this);
		writer.close();
	}
	
	public List<PowerTableValue> getPivotPowerTable() {
		return this.pivotPowerTable;
	}
	
	public boolean debuggingPivot() {
		return this.debuggingPivot;
	}

}
