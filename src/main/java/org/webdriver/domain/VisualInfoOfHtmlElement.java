package org.webdriver.domain;


import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public 	class VisualInfoOfHtmlElement{
	private Dimension dimension;
	private Point point;
	private boolean isDisplayed;
	
	public VisualInfoOfHtmlElement(Dimension dimension, Point point, boolean isDisplayed) {
		super();
		this.dimension = dimension;
		this.point = point;
		this.isDisplayed = isDisplayed;
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}		
}
