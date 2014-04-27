package org.webdriver.domain;

import java.util.Map;

/**
 * 
 * An HTML element starts with a start tag / opening tag and ends with an end tag / closing tag
 * Some HTML elements have empty content
 * Empty elements are closed in the start tag
 * Most HTML elements can have attributes
 * 
 * 
 * @author Dimitrios Karapapas
 *
 */
public class HtmlElement {

	private String tag; 
	private Map<String,String> attributesMap;
	
	
	public HtmlElement(String tag, Map<String, String> attributesMap) {
		super();
		this.tag = tag;
		this.attributesMap = attributesMap;
	}


	public String getTag() {
		return tag;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}


	public Map<String, String> getAttributesMap() {
		return attributesMap;
	}


	public void setAttributesMap(Map<String, String> attributesMap) {
		this.attributesMap = attributesMap;
	}	
	
	
}
