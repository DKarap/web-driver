package org.webdriver;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GhostDriverTest {

    private Driver ghostDriver = null;


    @Before
    public void prepareDriver() throws Exception
    {
	    final String CONFIG_FILE = "./config/ghostdriver/config.ini";
	    ghostDriver = new GhostDriver(CONFIG_FILE);
    }


    @After
    public void quitDriver() {
        if (ghostDriver != null) {
        	ghostDriver.quit();
            ghostDriver = null;
        }
    }

    @Test
    public void testGetTimeOutException() throws Exception{
	    final String CONFIG_FILE = "./config/ghostdriver/configForTests.ini";
	    Driver ghostDriver = new GhostDriver(CONFIG_FILE);
    	final String url = "http://www.in.gr";
    	
    	boolean clickSuccess = ghostDriver.get(url);
    	assertFalse("failed to time out durring get(url)..", clickSuccess);
    	
	    ghostDriver.quit();
    }
    
    
    @Test
    public void testClickLink(){
    	final boolean openInNewWindow = false;
    	final String url = "http://en.wikipedia.org/wiki/Main_Page";
    	ghostDriver.get(url);
        
	    boolean clickSuccess = ghostDriver.clickLink("linkText","Main page", openInNewWindow);
	    assertTrue("failed to find link..",clickSuccess);
	    
	    clickSuccess = ghostDriver.clickLink("xpath","//*[@id=\"n-mainpage-description\"]/a", openInNewWindow);
	    assertTrue("failed to find element by xpath..",clickSuccess);
	    
	    clickSuccess = ghostDriver.clickLink("malaka","Main page", openInNewWindow);
	    assertFalse("failed to throw selector exception..",clickSuccess);

    }
    
    @Test
    public void testSwitchToFrame(){
    	final String url = "http://www.corelab.com/careers/job-search";
    	ghostDriver.get(url);
	    
	    boolean clickSuccess = ghostDriver.switchToFrame("nameOrId", "jobSearch");
	    assertTrue("failed to switch to fram by nameOrId..",clickSuccess);
	    
    	ghostDriver.get(url);
	    clickSuccess = ghostDriver.switchToFrame("index", 0);
	    assertTrue("failed to switch to fram by index..",clickSuccess);


	    clickSuccess = ghostDriver.switchToFrame("index", "0");
	    assertFalse("failed to throw cast exception..",clickSuccess);

    }
}
