package org.webdriver;

import java.util.List;

import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;

import com.google.common.collect.ImmutableSet;


/**
 * Driver is responsible for visiting urls and extracting their content(title,links,text..etc)
 * 
 * @author Dimitrios Karapapas
 *
 */
public interface Driver {
	
	public static final ImmutableSet<String> FRAME_TAG_NAME_LIST = ImmutableSet.of(
			  "frame",
			  "iframe");
	
	public static final ImmutableSet<String> LINK_TAG_NAME_LIST = ImmutableSet.of(
			  "a",
			  "button",
			  "input");
	

	/**
	 * Load a new web page 
	 * @param url the URL to load.
	 * @return true if we manage to go to the web page, otherwise if an timeout exception happened return false
	 */
	public boolean get(String url);

	
	
	/**
	 * Load a new web page by clicking the link which is find by the given method's value
	 * @param method the method that we use to find the link for click (xpath,name,id,etc...)
 	 * @param value the value of the element that we look for to click
 	 * @param openInNewWindow true if we want to open it in a new window..
	 * @return true if we manage to find and click the link, otherwise false
	 */
	public boolean clickLink(String method, String value, boolean openInNewWindow);
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Return all the links of the current web page
	 * @return list of links of the current web page
	 */	
	public List<Link> getLinks();

	/**
	 * Return all the frames of the current web page
	 * @return list of frames of the current web page
	 */
	public List<Frame> getFrames();


	/**
	 * Return all the child links of the html element finding by the given selector method and its value value
	 * @return list of child links of the html element 
	 */	
	public List<Link> getElementChildLinks(String method, String value);
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getNumberOfOpenWindows();
	
	
	
	
	
	/**
	 * Switch to frame 
	 * @param method the method that we use to find the frame (index,name,etc...)
 	 * @param value the value of the frame that we look for to switch
	 * @return true if we manage to switch to frame, otherwise false
	 */
	public boolean switchToFrame(String method, Object value);	
	
	
	/**
	 * Get the source of the last loaded page.	 
	 * @return the source of the last loaded page
	 */
	public String getPageSource();

	
	/**
	 * The title of the current page.
	 * @return The title of the current page, with leading and trailing whitespace stripped, or null
	 *         if one is not already set
	 */
	public String getTitle();

	
	/**
	 * Get a string representing the current URL that the driver is looking at.
	 * @return The URL of the page currently loaded in the driver
	 */
	public String getCurrentUrl();

	
	
	/**
	 * Quits this driver
	 */
	public void quit();

	

}
