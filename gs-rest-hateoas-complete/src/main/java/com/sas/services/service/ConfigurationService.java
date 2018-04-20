package com.sas.services.service;

import java.util.List;

import com.sas.services.model.Configuration;

/**
 * 
 * @author sugawd
 *
 */
public interface ConfigurationService {
	/**
	 * 
	 * @param key
	 * @return the configuration by key
	 */
	Configuration getConfigurationByKey(String key);
	
	/**
	 * 
	 * @param config
	 * @return the saved configuration
	 */
	Configuration saveConfiguration(Configuration config);
	
	/**
	 * updates the configuration  i.e. the value 
	 * @param config
	 * @return the 
	 */
	Configuration updateConfiguration(Configuration config);
	
	/**
	 * Deletes the configuration 
	 * @param config the configuration to be deleted.
	 */
	void deleteConfiguration(Configuration config);
	
	/**
	 * 
	 * @return all configuration else null
	 */
	List<Configuration> getAllConfiguraions();
	
	/**
	 * Deletes all configuration from the database
	 */
	void deleteAllConfigurations();
	
	/**
	 * Returns true if the configuraion exists 
	 * @param config
	 * @return
	 */
	
	public boolean isConfigurationExist(Configuration config);
}
