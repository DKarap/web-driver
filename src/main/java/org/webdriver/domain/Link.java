package org.webdriver.domain;

import java.util.Map;


public class Link extends HtmlElement{
	
	private String anchorText;
	private String xpath;
	private String xpathOfParentNode;
	private VisualInfoOfHtmlElement visualInfoOfHtmlElement;
	
	
	public Link(String tag, Map<String, String> attributesMap,
			String anchorText, String xpath, String xpathOfParentNode,
			VisualInfoOfHtmlElement visualInfoOfHtmlElement) {
		super(tag, attributesMap);
		this.anchorText = anchorText;
		this.xpath = xpath;
		this.xpathOfParentNode = xpathOfParentNode;
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


	public String getXpathOfParentNode() {
		return xpathOfParentNode;
	}


	public void setXpathOfParentNode(String xpathOfParentNode) {
		this.xpathOfParentNode = xpathOfParentNode;
	}


	public VisualInfoOfHtmlElement getVisualInfoOfHtmlElement() {
		return visualInfoOfHtmlElement;
	}


	public void setVisualInfoOfHtmlElement(
			VisualInfoOfHtmlElement visualInfoOfHtmlElement) {
		this.visualInfoOfHtmlElement = visualInfoOfHtmlElement;
	}

	
}

