package com.sas.services.model;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sas.services.controller.ConfigurationController;

public class Configuration extends ResourceSupport {

    private String key;
    private String value;
    
    @JsonCreator
    public Configuration(@JsonProperty("key") String key, @JsonProperty("value") String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
        return value;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (key.equals(other.getKey())) {
			return true;
		}
		return false;
	}
	
	public void generateBasicLinks(Class<?> configClass, String objectMapping, MediaType applicationJson) {
		Link selfLink = linkTo(configClass).slash(key).withSelfRel().withType(applicationJson.toString());
		add(selfLink);
		
		Link putLink1 = linkTo(configClass).slash(key).withRel(RestConstants.PUT).withType(applicationJson.toString());
		add(putLink1);
		
		Link deleteLink =  linkTo(configClass).slash(key).withRel(RestConstants.DELETE).withType(applicationJson.toString());
		add(deleteLink);
	}
}

