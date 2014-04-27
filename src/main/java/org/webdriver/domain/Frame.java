package org.webdriver.domain;

import java.util.Map;

public class Frame extends HtmlElement{

	private int frameIndex;

	public Frame(String tag, Map<String, String> attributesMap, int frameIndex) {
		super(tag, attributesMap);
		this.frameIndex = frameIndex;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	} 
	
	
}