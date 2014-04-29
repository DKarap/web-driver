package org.webdriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;

import com.google.common.collect.ImmutableSet;


public class GhostDriverTest {

    private Driver ghostDriver = null;

	public static final ImmutableSet<String> FRAME_TAG_NAME_LIST = ImmutableSet.of(
	  "frame",
	  "iframe");

	public static final ImmutableSet<String> LINK_TAG_NAME_LIST = ImmutableSet.of(
	  "a",
	  "button",
	  "input");


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
    public void testgetFrame(){
    	final String url = "http://www.corelab.com/careers/job-search";
    	ghostDriver.get(url);
	    
	    List<Frame> frameList = ghostDriver.getFrames(FRAME_TAG_NAME_LIST);
	    assertEquals("Get frames broke..or page changed..",1, frameList.size());
    }
    
    @Test
    public void testElementXpathComputation(){
    	final String url = "http://en.wikipedia.org/wiki/Main_Page";
    	ghostDriver.get(url);
	    
	    List<Link> linkList = ghostDriver.getLinks("xpath", "//*[@id=\"p-navigation\"]/div",LINK_TAG_NAME_LIST);
	    assertEquals("Get links failed or wikipedia changed..", 7, linkList.size());
	    assertEquals("Computation xapth is broken or wiki changed..","/html[1]/body[1]/div[4]/div[2]/div[2]/div[1]/ul[1]/li[1]/a[1]",linkList.get(0).getXpath());
	    linkList = ghostDriver.getLinks("xpath", "//*[@id=\"malaka\"]/div",LINK_TAG_NAME_LIST);
	    assertEquals("Get links failed or wikipedia changed..", 0, linkList.size());

    }
    
    @Test
    public void testSelectOption(){
    	final String url = "https://philips.taleo.net/careersection/2/moresearch.ftl";
    	ghostDriver.get(url);
	    List<Link> linkListBefore = ghostDriver.getLinks("xpath", "//*[@id=\"requisitionListInterface.listRequisitionContainer\"]",LINK_TAG_NAME_LIST);
	    System.out.println( linkListBefore.size());

		final ImmutableSet<String> OptionSelectALLRelevantTerms = ImmutableSet.of("armenia");
	    ghostDriver.selectOptions("tagName", "body", OptionSelectALLRelevantTerms);
	    ghostDriver.clickLink("xpath", "//*[@id=\"advancedSearchFooterInterface.searchAction\"]", false);
	    List<Link> linkListAfter = ghostDriver.getLinks("xpath", "//*[@id=\"requisitionListInterface.listRequisitionContainer\"]",LINK_TAG_NAME_LIST);
	    
	    System.out.println( linkListBefore.size()+"\t"+linkListAfter.size());
	    assertTrue("Select Option broke..",linkListBefore.size() > linkListAfter.size());
    }
}
