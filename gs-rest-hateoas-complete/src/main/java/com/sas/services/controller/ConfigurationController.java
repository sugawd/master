package com.sas.services.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.sas.services.model.Configuration;
import com.sas.services.model.RestConstants;
import com.sas.services.service.ConfigurationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * RESTful Controller for CRUD Operations on String Keys and String Values
 * @author sugawd
 *
 */
@RestController
@RequestMapping(RestConstants.CONFIGURATIONS_MAPPING)
@Api(value=RestConstants.CONTROLLER_API, produces=MediaType.APPLICATION_JSON_VALUE)
public class ConfigurationController {

	private final ConfigurationService configurationService;

	public ConfigurationController(ConfigurationService service) {
		configurationService = service;
	}
	/***
	 * 
	 * @return returns all the configurations
	 */
	@RequestMapping(
			value = { RestConstants.EMPTY_ROOT_MAPPING/*, RestConstants.ROOT_MAPPING */}, // TODO Commented out as it creates duplicate entries in Swagger. 
			method= {RequestMethod.GET, RequestMethod.HEAD},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public HttpEntity<List<Configuration>> getConfigurations() {

		List<Configuration> configurations = configurationService.getAllConfiguraions();
		if(CollectionUtils.isEmpty(configurations)) {
			return new ResponseEntity<List<Configuration>>(HttpStatus.NO_CONTENT);
		}
		for(Configuration configuration : configurations) {
			configuration.generateBasicLinks(ConfigurationController.class, RestConstants.CONFIGURATIONS_MAPPING, MediaType.APPLICATION_JSON);
		}
		return new ResponseEntity<List<Configuration>>(configurations, HttpStatus.OK);
	}

	//-------------------Get a Configuration--------------------------------------------------------
	/**
	 * 
	 * @param key GET configuration for a given key 
	 * @return configuration
	 */
	@RequestMapping(
			value = {RestConstants.ID_PARAM/*, RestConstants.ID_PARAM + RestConstants.ROOT_MAPPING*/},  
			method={RequestMethod.GET, RequestMethod.HEAD},
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	@ApiOperation("Gets the configurations i.e the key values pair")
	@ApiResponses(value = {@ApiResponse(code=200, message="OK", response=Configuration.class)})
	public ResponseEntity<Configuration> getConfiguration(@PathVariable(RestConstants.KEY) String key) {
		System.out.println("Fetching Configuration with key " + key);
		Configuration configuration = configurationService.getConfigurationByKey(key);
		if (configuration == null) {
			System.out.println("Configuration with key " + key + " not found");
			return new ResponseEntity<Configuration>(HttpStatus.NOT_FOUND);
		}
		configuration.generateBasicLinks(ConfigurationController.class, RestConstants.CONFIGURATIONS_MAPPING, MediaType.APPLICATION_JSON);
		return new ResponseEntity<Configuration>(configuration, HttpStatus.OK);
	}


	//-------------------Create a Configuration--------------------------------------------------------
	/**
	 * 
	 * @param configuration
	 * @return returning the created configuration
	 */
	@RequestMapping(
			value = { RestConstants.EMPTY_ROOT_MAPPING/*, RestConstants.ROOT_MAPPING */},
			method = RequestMethod.POST,
			consumes={MediaType.APPLICATION_JSON_VALUE},
			produces={MediaType.APPLICATION_JSON_VALUE}
			)
	public ResponseEntity<?> createConfiguratin(@RequestBody Configuration configuration) {
		System.out.println("Creating Configuration " + configuration.getKey());

		if (configurationService.isConfigurationExist(configuration)) {
			System.out.println("A Configuration with name " + configuration.getKey() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		configurationService.saveConfiguration(configuration);
		configuration.generateBasicLinks(ConfigurationController.class, RestConstants.CONFIGURATIONS_MAPPING, MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Configuration>(configuration, HttpStatus.CREATED);
	}


	//------------------- Update a Configuration --------------------------------------------------------
	/**
	 * 
	 * @param key
	 * @param configuration
	 * @return returns the updated configuration
	 */
	@RequestMapping(
			value = {RestConstants.ID_PARAM/*, RestConstants.ID_PARAM + RestConstants.ROOT_MAPPING*/}, 
			method = RequestMethod.PUT)
	public ResponseEntity<Configuration> updateConfiguration(@PathVariable(RestConstants.KEY) String key, @RequestBody Configuration configuration) {
		System.out.println("Updating Configuration " + key);

		Configuration currentConfiguration = configurationService.getConfigurationByKey(key);

		if (currentConfiguration==null) {
			System.out.println("Configuration with key " + key + " not found");
			return new ResponseEntity<Configuration>(HttpStatus.NOT_FOUND);
		}

		currentConfiguration.setValue(configuration.getValue());
		configuration.generateBasicLinks(ConfigurationController.class, RestConstants.CONFIGURATIONS_MAPPING, MediaType.APPLICATION_JSON);
		return new ResponseEntity<Configuration>(currentConfiguration, HttpStatus.OK);
	}


	//------------------- Delete a Configuration --------------------------------------------------------
	/**
	 * 
	 * @param key The key of the configuration that needs to be deleted 
	 * @return No Content
	 */
	@RequestMapping(
			value = {RestConstants.ID_PARAM/*, RestConstants.ID_PARAM + RestConstants.ROOT_MAPPING*/},  
			method = RequestMethod.DELETE)
	public ResponseEntity<Configuration> deleteConfiguration(@PathVariable("key") String key) {
		System.out.println("Fetching & Deleting Configuration with key " + key);

		Configuration configuration = configurationService.getConfigurationByKey(key);
		if (configuration == null) {
			System.out.println("Unable to delete. Configuration with key " + key + " not found");
			return new ResponseEntity<Configuration>(HttpStatus.NOT_FOUND);
		}

		configurationService.deleteConfiguration(configuration);
		return new ResponseEntity<Configuration>(HttpStatus.NO_CONTENT);
	}

}
