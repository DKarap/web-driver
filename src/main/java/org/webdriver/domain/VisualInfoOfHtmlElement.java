package org.webdriver.domain;


import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public 	class VisualInfoOfHtmlElement{
	private final Dimension dimension;
	private final Point point;
	private final boolean isDisplayed;
	private final Integer font_size;
	private final Integer font_weight;
	private final String color;
	
	public VisualInfoOfHtmlElement(Dimension dimension, Point point,
			boolean isDisplayed, Integer font_size, Integer font_weight,
			String color) {
		super();
		this.dimension = dimension;
		this.point = point;
		this.isDisplayed = isDisplayed;
		this.font_size = font_size;
		this.font_weight = font_weight;
		this.color = color;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public Point getPoint() {
		return point;
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public Integer getFont_size() {
		return font_size;
	}

	public Integer getFont_weight() {
		return font_weight;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "VisualInfoOfHtmlElement [dimension=" + dimension + ", point="
				+ point + ", isDisplayed=" + isDisplayed + ", font_size="
				+ font_size + ", font_weight=" + font_weight + ", color="
				+ color + "]";
	}	
}
