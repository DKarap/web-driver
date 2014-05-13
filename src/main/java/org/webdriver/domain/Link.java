package org.webdriver.domain;

import java.util.Map;


public class Link extends HtmlElement{
	
	private String text;
	private String xpath;
	private VisualInfoOfHtmlElement visualInfoOfHtmlElement;
	
	
	public Link(String tag, Map<String, String> attributesMap,String text, String xpath,VisualInfoOfHtmlElement visualInfoOfHtmlElement) {
		super(tag, attributesMap);
		this.text = text;
		this.xpath = xpath;
		this.visualInfoOfHtmlElement = visualInfoOfHtmlElement;
	}

	
	public String getClassification(){
		return getAttributesMap().get("class");
	}
	public void setClassification(String classification){
		getAttributesMap().put("class", classification);
	}

	public String getAttributeValue(String key){
		return getAttributesMap().get(key);
	}
	
	public void addAttribute(String key, String value){
		getAttributesMap().put(key, value);
	}

	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public String getXpath() {
		return xpath;
	}


	public void setXpath(String xpath) {
		this.xpath = xpath;
	}



	public VisualInfoOfHtmlElement getVisualInfoOfHtmlElement() {
		return visualInfoOfHtmlElement;
	}


	public void setVisualInfoOfHtmlElement(
			VisualInfoOfHtmlElement visualInfoOfHtmlElement) {
		this.visualInfoOfHtmlElement = visualInfoOfHtmlElement;
	}

	


	
	@Override
	public String toString() {
		return "Link [text=" + text + ", xpath=" + xpath
				+ ", visualInfoOfHtmlElement=" + visualInfoOfHtmlElement
				+ ", getTag()=" + getTag() + ", getAttributesMap()="
				+ getAttributesMap() + "]";
	}

	
}

