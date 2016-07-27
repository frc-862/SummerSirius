package org.usfirst.frc862.sirius;

import java.io.File;

import org.usfirst.frc862.sirius.config.Configuration;

/**
 * Creates a configuration file with sane defaults on the computer
 */
public class CreateConfigFilePC {

	public static void main(String[] args) throws Exception {
		File configurationFile = new File("config.yaml");
    	configurationFile.createNewFile();
    	Configuration configuration = new Configuration(true);
    	configuration.serialize(configurationFile);
	}

}
