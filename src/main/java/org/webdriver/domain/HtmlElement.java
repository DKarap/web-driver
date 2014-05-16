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


	@Override
	public String toString() {
		return "HtmlElement [tag=" + tag + ", attributesMap=" + attributesMap
				+ "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributesMap == null) ? 0 : attributesMap.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		HtmlElement other = (HtmlElement) obj;
		if (attributesMap == null) {
			if (other.attributesMap != null)
				return false;
		} else if (!attributesMap.equals(other.attributesMap))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}	
	
	
	
	
	
}
