package org.webdriver.domain;

import java.util.Map;

public class Frame extends HtmlElement{

	private int index;
	private double score;//how important is that FRAME(in order to follow?)

	public Frame(String tag, Map<String, String> attributesMap, int index) {
		super(tag, attributesMap);
		this.index = index;
	}

	
	public double getScore() {
		return score;
	}


	public void setScore(double score) {
		this.score = score;
	}


	public int getIndex() {
		return index;
	}

	public void setIndex(int frameIndex) {
		this.index = frameIndex;
	}


	@Override
	public String toString() {
		return "Frame [index=" + index + ", score=" + score + ", getTag()="
				+ getTag() + ", getAttributesMap()=" + getAttributesMap() + "]";
	}
	
	


	
}
