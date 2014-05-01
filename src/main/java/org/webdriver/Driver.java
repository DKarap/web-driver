package org.webdriver;

import java.util.Collection;
import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;


/**
 * Driver is responsible for visiting urls and extracting their content(title,links,text..etc)
 * Because in this process a lot of things can go wrong with the browser, we throw a RunTime 
 * exception from most of the methods in order to know when the browser or website is DEAD and stop the program
 * 
 * @author Dimitrios Karapapas
 *
 */
public interface Driver {
	
	

	/**
	 * Load a new web page 
	 * @param url the URL to load.
	 * @return true if we manage to go to the web page, otherwise if an exception happened return false
	 * @throws WebDriverException If something fucked up happened with the webdriver
	 */
	public boolean get(String url);

	
	/**
	 * @return int the number of currently open windows
	 * @throws WebDriverException If something fucked up happened with the webdriver
	 */
	public int getNumberOfOpenWindows();

	
	/**
	 * Get the source of the last loaded page.	 
	 * @return the source of the last loaded page
	 * @throws WebDriverException If something fucked up happened with the webdriver
	 */
	public String getPageSource();

	
	/**
	 * The title of the current page.
	 * @return The title of the current page, with leading and trailing whitespace stripped, or null
	 *         if one is not already set
	 * @throws WebDriverException If something fucked up happened with the webdriver
	 */
	public String getTitle();

	
	/**
	 * Get a string representing the current URL that the driver is looking at.
	 * @return The URL of the page currently loaded in the driver
	 * @throws WebDriverException If something fucked up happened with the webdriver
	 */
	public String getCurrentUrl();

	
	
	/**
	 * Quits this driver
	 * @throws WebDriverException If something fucked up happened with the webdriver
	 */
	public void quit();

	
	
	/**
	 * Select each option that exist after the detected (web or select or whatever) element to the one with most relevant text to the given one
	 * TODO HACK: in case we want to select only the ALL option then we can check only the first option from each select since this is the place where exist normaly
	 * @param method the method that we use to find the element which after it we will set the options to ALL, 
 	 * @param value the value of the method
  	 * @param textToSelect list of text(synonims) that we look for to select
 	 * @throws WebDriverException If something fucked up happened with the webdriver  
	 */
	public void selectOptions(String method, String value, Collection<String> textToSelect);
	
	
	
	/**
	 * Switch to frame 
	 * @param method the method that we use to find the frame (index,name,etc...)
 	 * @param value the value of the method 
	 * @return true if we manage to switch to frame, otherwise false
 	 * @throws WebDriverException If something fucked up happened with the webdriver  
	 */
	public boolean switchToFrame(String method, Object value);	


	

	/**
	 * Return all the frames of the current web page
	 * @param  FRAME_TAG_NAME_LIST list of tag names that we consider frames
	 * @return list of frames of the current web page or empty list if not frames found
 	 * @throws WebDriverException If something fucked up happened with the webdriver  
	 */
	public List<Frame> getFrames(Collection<String> FRAME_TAG_NAME_LIST);

	
	
	
	
	/**
	 * Load a new web page by clicking the element which is find by the given method's value
	 * @param method the method that we use to find the element for click (xpath,name,id,etc...)
 	 * @param value the value of the element that we look for to click
 	 * @param openInNewWindow true if we want to open it in a new window..
	 * @return true if we manage to find and click the element, otherwise false
 	 * @throws WebDriverException If something fucked up happened with the webdriver  
	 */
	public boolean clickElement(String method, String value, boolean openInNewWindow);
	
	
	
	
	
	/**
	 * Return all the links of the current web page after the element which is found with the given method;if we want all the links then simply choose the body element by tag name 
	 * @param method the method that we use to find the element for further looking for links
 	 * @param value the value of the method 
 	 * @param LINK_TAG_NAME_LIST list of tag names that we want to retrieve
	 * @return list of links of the current web page after a given element or all of them; if element cannot be find then return an empty list
 	 * @throws WebDriverException If something fucked up happened with the webdriver  
	 */
	public List<Link> getLinks(String method, Object value, Collection<String> LINK_TAG_NAME_LIST);
	
}
