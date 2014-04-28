package org.webdriver.domain;

import java.util.Map;


public class Link extends HtmlElement{
	
	private String anchorText;
	private String xpath;
	private VisualInfoOfHtmlElement visualInfoOfHtmlElement;
	
	
	public Link(String tag, Map<String, String> attributesMap,
			String anchorText, String xpath,
			VisualInfoOfHtmlElement visualInfoOfHtmlElement) {
		super(tag, attributesMap);
		this.anchorText = anchorText;
		this.xpath = xpath;
		this.visualInfoOfHtmlElement = visualInfoOfHtmlElement;
	}


	public String getAnchorText() {
		return anchorText;
	}


	public void setAnchorText(String anchorText) {
		this.anchorText = anchorText;
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
		return "Link [anchorText=" + anchorText + ", xpath=" + xpath
				+ ", visualInfoOfHtmlElement=" + visualInfoOfHtmlElement
				+ ", getTag()=" + getTag() + ", getAttributesMap()="
				+ getAttributesMap() + "]";
	}

	
}

