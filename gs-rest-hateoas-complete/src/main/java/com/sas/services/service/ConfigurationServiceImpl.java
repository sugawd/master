package com.sas.services.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sas.services.model.Configuration;

@Service("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

	private static List<Configuration> configurations;
	
	static{
		configurations= populateDefaultConfigurations();
	}
	
	@Override
	public Configuration getConfigurationByKey(String key) {
		if(!CollectionUtils.isEmpty(configurations)) {
			for(Configuration configuration: configurations) {
				if(configuration.getKey().equals(key)) {
					return configuration;
				}
			}
		}
		return null;
	}

	private static List<Configuration> populateDefaultConfigurations() {
		List<Configuration> configurations = new ArrayList<Configuration>();
		configurations.add(new Configuration("key1","value1"));
		configurations.add(new Configuration("key2","value2"));
		configurations.add(new Configuration("key3","value3"));
		configurations.add(new Configuration("key4","value4"));
		return configurations;
	}

	@Override
	public Configuration saveConfiguration(Configuration config) {
		configurations.add(config);
		return config; 
	}

	@Override
	public Configuration updateConfiguration(Configuration config) {
		Configuration configuration = getConfigurationByKey(config.getKey());
		configuration.setValue(config.getValue());
		return configuration;
	}

	@Override
	public void deleteConfiguration(Configuration config) {
		configurations.remove(config);
		
	}

	@Override
	public List<Configuration> getAllConfiguraions() {
		return configurations;
	}

	@Override
	public void deleteAllConfigurations() {
		configurations.clear();
		
	}

	@Override
	public boolean isConfigurationExist(Configuration config) {
		boolean isConfigurationExist = false;
		Configuration savedConfig = getConfigurationByKey(config.getKey());
		if(savedConfig != null) {
			isConfigurationExist = true;
		}
		return isConfigurationExist;

	}

}
