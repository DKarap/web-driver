package org.webdriver.domain;

import java.util.Collection;

public class WebPage {
	
	private final int id;
	private final String url;
	private final String title;
	private final String sourceCode;
	private final Collection<Frame> frames;
	private final Collection<Link> links;
	
	

	public WebPage(int id, String url, String title, String sourceCode,
			Collection<Frame> frames, Collection<Link> links) {
		super();
		this.id = id;
		this.url = url;
		this.title = title;
		this.sourceCode = sourceCode;
		this.frames = frames;
		this.links = links;
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


	public Collection<Frame> getFrames() {
		return frames;
	}


	public Collection<Link> getLinks() {
		return links;
	}	
	
}
