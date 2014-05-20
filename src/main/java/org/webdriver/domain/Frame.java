package org.webdriver.domain;

import java.util.Map;

public class Frame extends HtmlElement{

	private int index;

	public Frame(String tag, Map<String, String> attributesMap, int index) {
		super(tag, attributesMap);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int frameIndex) {
		this.index = frameIndex;
	}

	@Override
	public String toString() {
		return "Frame [frameIndex=" + index + ", getTag()=" + getTag()
				+ ", getAttributesMap()=" + getAttributesMap()
				+ ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
	}

	
}
