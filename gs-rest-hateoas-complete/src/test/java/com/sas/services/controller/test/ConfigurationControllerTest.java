package com.sas.services.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.services.model.Configuration;
import com.sas.services.model.RestConstants;
import com.sas.services.service.ConfigurationService;
/**
 * RESTful Test Controller for CRUD Operations on String Keys and String Values
 * 
 * @author sugawd
 * @version .10
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConfigurationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConfigurationService configurationService;

	@Test
	public void testGetConfigurationList_NoError() throws Exception {
		List<Configuration> configurations = getDefaultConfigurations();

		when(configurationService.getAllConfiguraions()).thenReturn(configurations);

		MvcResult result = this.mockMvc.perform(get(RestConstants.CONFIGURATIONS_MAPPING)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))  
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))  
				.andExpect(status().isOk())
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);

	}

	@Test
	public void testCreateConfiguration_NoError() throws Exception {
		Configuration configuration = new Configuration("key5", "Value5");
		ObjectMapper mapper = new ObjectMapper();
		String postJSON = mapper.writeValueAsString(configuration);

		when(configurationService.isConfigurationExist(any(Configuration.class))).thenReturn(false);
		when(configurationService.saveConfiguration(any(Configuration.class))).then(AdditionalAnswers.returnsFirstArg());
		MvcResult result = mockMvc.perform(post(RestConstants.CONFIGURATIONS_MAPPING)
				.contentType(MediaType.APPLICATION_JSON)
				.content(postJSON))
				.andExpect(status().isCreated())
				.andReturn();

	}

	@Test
	public void testCreateConfiguration_ConfigurationExistsError() throws Exception {
		Configuration configuration = new Configuration("key5", "Value5");
		ObjectMapper mapper = new ObjectMapper();
		String postJSON = mapper.writeValueAsString(configuration);

		when(configurationService.isConfigurationExist(any(Configuration.class))).thenReturn(true);
		when(configurationService.saveConfiguration(any(Configuration.class))).then(AdditionalAnswers.returnsFirstArg());
		MvcResult result = mockMvc.perform(post(RestConstants.CONFIGURATIONS_MAPPING)
				.contentType(MediaType.APPLICATION_JSON)
				.content(postJSON))
				.andExpect(status().isConflict())
				.andReturn();
	}
	
	@Test
	public void testUpdateConfiguration_NoError() throws Exception {
		Configuration updateConfiguration = new Configuration("key4", "Value44");
		Configuration configuration = new Configuration("key5", "Value5");
		ObjectMapper mapper = new ObjectMapper();
		String putJSON = mapper.writeValueAsString(configuration);
			
		when(configurationService.getConfigurationByKey(any(String.class))).thenReturn(updateConfiguration);
		Map <String, Object> urlVars = new HashMap<String,Object>();
		urlVars.put(RestConstants.KEY, "key4");
		
		MvcResult result = mockMvc.perform(put(RestConstants.CONFIGURATIONS_MAPPING + RestConstants.ID_PARAM, urlVars)
				.contentType(MediaType.APPLICATION_JSON)
				.content(putJSON))
				.andExpect(status().isOk())
				.andReturn();

	}
	
	@Test
	public void testUpdateConfiguration_ConfigurationDoesNotExistsError() throws Exception {
		Configuration configuration = new Configuration("key5", "Value5");
		ObjectMapper mapper = new ObjectMapper();
		String putJSON = mapper.writeValueAsString(configuration);
			
		when(configurationService.getConfigurationByKey(any(String.class))).thenReturn(null);
		Map <String, Object> urlVars = new HashMap<String,Object>();
		urlVars.put(RestConstants.KEY, "key4");
		
		MvcResult result = mockMvc.perform(put(RestConstants.CONFIGURATIONS_MAPPING + RestConstants.ID_PARAM, urlVars)
				.contentType(MediaType.APPLICATION_JSON)
				.content(putJSON))
				.andExpect(status().isNotFound())
				.andReturn();

	}
	
	@Test
	public void testDeleteConfiguration_NoError() throws Exception {
		Configuration deleteConfiguration = new Configuration("key4", "Value44");
			
		when(configurationService.getConfigurationByKey(any(String.class))).thenReturn(deleteConfiguration);
		Map <String, Object> urlVars = new HashMap<String,Object>();
		urlVars.put(RestConstants.KEY, "key4");
		
		MvcResult result = mockMvc.perform(delete(RestConstants.CONFIGURATIONS_MAPPING + RestConstants.ID_PARAM, urlVars)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andReturn();


	}
	
	@Test
	public void testDeleteConfiguration_ConfigurationDoesNotExistsError() throws Exception {
		Configuration deleteConfiguration = new Configuration("key4", "Value44");
			
		when(configurationService.getConfigurationByKey(any(String.class))).thenReturn(null);
		Map <String, Object> urlVars = new HashMap<String,Object>();
		urlVars.put(RestConstants.KEY, "key4");
		
		MvcResult result = mockMvc.perform(delete(RestConstants.CONFIGURATIONS_MAPPING + RestConstants.ID_PARAM, urlVars)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();

	}
	
	private static List<Configuration> getDefaultConfigurations() {
		List<Configuration> configurations = new ArrayList<Configuration>();
		configurations.add(new Configuration("key1","value1"));
		configurations.add(new Configuration("key2","value2"));
		configurations.add(new Configuration("key3","value3"));
		configurations.add(new Configuration("key4","value4"));
		return configurations;
	}
}
