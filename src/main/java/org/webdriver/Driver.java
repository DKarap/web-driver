package org.webdriver;

import java.util.Collection;
import java.util.List;

import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;


/**
 * Driver is responsible for visiting urls and extracting their content(title,links,text..etc)
 * 
 * @author Dimitrios Karapapas
 *
 */
public interface Driver {
	
	
	
	/**
	 * Select each option that exist after the detected (web or select or whatever) element to the one with most relevant text to the given one
	 * @param method the method that we use to find the element which after it we will set the options to ALL, 
 	 * @param value the value of the method
  	 * @param textToSelect list of text(synonims) that we look for to select  
	 */
	public void selectOptions(String method, String value, Collection<String> textToSelect);
	
	
	
	/**
	 * Load a new web page 
	 * @param url the URL to load.
	 * @return true if we manage to go to the web page, otherwise if an timeout exception happened return false
	 */
	public boolean get(String url);

	
	
	/**
	 * Load a new web page by clicking the element which is find by the given method's value
	 * @param method the method that we use to find the element for click (xpath,name,id,etc...)
 	 * @param value the value of the element that we look for to click
 	 * @param openInNewWindow true if we want to open it in a new window..
	 * @return true if we manage to find and click the element, otherwise false
	 */
	public boolean clickElement(String method, String value, boolean openInNewWindow);
	
	
	
	
	
	/**
	 * Return all the links of the current web page after the element which is found with the given method;if we want all the links then simply choose the body element by tag name 
	 * @param method the method that we use to find the element for further looking for links
 	 * @param value the value of the method 
 	 * @param LINK_TAG_NAME_LIST list of tag names that we want to retrieve
	 * @return list of links of the current web page after a given element or all of them
	 */
	public List<Link> getLinks(String method, Object value, Collection<String> LINK_TAG_NAME_LIST);
	

	/**
	 * Return all the frames of the current web page
	 * @param  FRAME_TAG_NAME_LIST list of tag names that we consider frames
	 * @return list of frames of the current web page
	 */
	public List<Frame> getFrames(Collection<String> FRAME_TAG_NAME_LIST);


	
		
	
	/**
	 * @return int the number of currently open windows
	 */
	public int getNumberOfOpenWindows();
	
	
	/**
	 * Switch to frame 
	 * @param method the method that we use to find the frame (index,name,etc...)
 	 * @param value the value of the method 
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
