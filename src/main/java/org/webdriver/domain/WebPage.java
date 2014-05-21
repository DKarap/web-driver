package org.webdriver.domain;

import java.util.ArrayList;
import java.util.List;

public class WebPage {
	
	private final int id;
	private final String url;
	private final String title;
	private final String sourceCode;
	private List<Frame> frames;
	private List<Link> links;
	private List<Link> linksToThisWebPage;
	private String classification;
	
	

	public WebPage(int id, String url, String title, String sourceCode,
			List<Frame> frames, List<Link> links) {
		super();
		this.id = id;
		this.url = url;
		this.title = title;
		this.sourceCode = sourceCode;
		this.frames = frames;
		this.links = links;
		this.linksToThisWebPage = new ArrayList<Link>();
	}


	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}


	public List<Link> getLinksToThisWebPage() {
		return linksToThisWebPage;
	}


	public void setLinksToThisWebPage(List<Link> linksToThisWebPage) {
		this.linksToThisWebPage = linksToThisWebPage;
	}

	public void addLinkToThisWebPage(Link linkToThisWebPage) {
		this.linksToThisWebPage.add(linkToThisWebPage);
	}


	public int getId() {
		return id;
	}


	public String getUrl() {
		return url;
	}


	public String getTitle() {
		return title;
	}


	public String getSourceCode() {
		return sourceCode;
	}


	public List<Frame> getFrames() {
		return frames;
	}


	public List<Link> getLinks() {
		return links;
	}


	public void setFrames(List<Frame> frames) {
		this.frames = frames;
	}


	public void setLinks(List<Link> links) {
		this.links = links;
	}	
	
}
