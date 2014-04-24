package org.webdriver;

import java.util.List;
import java.util.NoSuchElementException;

import org.webdriver.domain.Link;


/**
 * WebDriver is responsible for visiting urls and extracting their content(title,links,text..etc)
 * 
 * @author Dimitrios Karapapas
 *
 */
public interface WebDriver {
	
	
	/**
	 * Load a new web page by the given url or index in case of (i)frame
	 * @param url The URL to load. If is (i)frame then its index (integer)
	 * @return true if we manage to go to the web page, otherwise false
	 */
	public boolean get(String url);

	
	
	/**
	 * Load a new web page by clicking the link which is find by the given xpath
	 * @throws NoSuchElementException - If the given element is not within a form
	 */
	public void clickLinkByXpath(String xpath);
	
	
	

	/**
	 * Return all the links(include iframes) of the current web page
	 * @return list of links(include iframes) of the current web page
	 */	
	public List<Link> getPageLinks();

	
	/**
	 * Return all the child links(include iframes) of the html element finding by the given xpath
	 * @return list of child links(include iframes) of the html element finding by the given xpath
	 */	
	public List<Link> getElementChildLinksByXpath(String xpath);
	
	
	
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
