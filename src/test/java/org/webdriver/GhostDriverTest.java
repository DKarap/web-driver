package org.webdriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.webdriver.domain.Link;


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
    	System.out.println(ghostDriver.getTitle());
	    System.out.println(ghostDriver.getCurrentUrl());
	    
	    assertEquals("failed to switch to frame..page or functionality broke..","https://www5.recruitingcenter.net/Clients/CoreLab/PublicJobs/Canviewjobs.cfm?", ghostDriver.getCurrentUrl());
	    assertEquals("failed to switch to frame..page or functionality broke..","Core Laboratories Job Postings", ghostDriver.getTitle());
	    assertTrue("failed to switch to frame by index..",clickSuccess);


	    clickSuccess = ghostDriver.switchToFrame("nameOrId", 0);
	    assertFalse("failed to throw cast exception..",clickSuccess);
    }
    
    @Test
    public void testElementXpathComputation(){
    	final String url = "http://en.wikipedia.org/wiki/Main_Page";
    	ghostDriver.get(url);
	    
	    List<Link> linkList = ghostDriver.getLinks("xpath", "//*[@id=\"p-navigation\"]/div");
	    assertEquals("Get links failed or wikipedia changed..", 7, linkList.size());
	    
	    linkList = ghostDriver.getLinks("xpath", "//*[@id=\"malaka\"]/div");
	    assertEquals("Get links failed or wikipedia changed..", 0, linkList.size());

    }
}
