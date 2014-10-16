package org.webdriver.domain;

import java.util.ArrayList;
import java.util.List;

public class WebPage {
	
	private final int id;
	private final String url;
	private final String title;
	private final String description;
	private final String sourceCode;
	private List<Frame> frames;
	private List<Link> links;
	private List<Link> linksToThisWebPage;
	private String classification;
	
	
	private List<String> xpaths_or_frame_index_to_this_page; //last one is the last link's xpath or frame's index that we follow to this page
	

	public WebPage(int id, String url, String title, String description, String sourceCode,
			List<Frame> frames, List<Link> links) {
		super();
		this.id = id;
		this.url = url;
		this.title = title;
		this.description = description;
		this.sourceCode = sourceCode;
		this.frames = frames;
		this.links = links;
		this.linksToThisWebPage = new ArrayList<Link>();
		this.setXpaths_or_frame_index_to_this_page(new ArrayList<String>());
	}


	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
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


	public String getDescription() {
		return description;
	}


//	public void setLinksToThisWebPage(List<Link> linksToThisWebPage) {
//		this.linksToThisWebPage = linksToThisWebPage;
//	}

	public List<Link> getLinksToThisWebPage() {
		return linksToThisWebPage;
	}

	public void addLinkToThisWebPage(Link linkToThisWebPage) {
		this.linksToThisWebPage.add(linkToThisWebPage);
	}


	public List<String> getXpaths_or_frame_index_to_this_page() {
		return xpaths_or_frame_index_to_this_page;
	}


	public void setXpaths_or_frame_index_to_this_page(List<String> xpaths_or_frame_index_to_this_page) {
		this.xpaths_or_frame_index_to_this_page = xpaths_or_frame_index_to_this_page;
	}



}
