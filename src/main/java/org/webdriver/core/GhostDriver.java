package org.webdriver.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.webdriver.domain.FindElementBy;
import org.webdriver.domain.Link;

import com.google.common.collect.ImmutableList;


public class GhostDriver extends SeleniumImpl{

	
	

	public GhostDriver(String CONFIG_FILE) throws Exception {
		super(prepareAndGetDriver(CONFIG_FILE));
	}
	
	

	private static WebDriver prepareAndGetDriver(String CONFIG_FILE) throws Exception{
    	DesiredCapabilities sCaps = configure(CONFIG_FILE);
        WebDriver webDriver = new PhantomJSDriver(sCaps);
        
        /*
         * WebDriver settings... 
         */
        //set page load time out if there is the corresponding setting
        if(sCaps.getCapability("pageLoadTimeout") != null)
        	webDriver.manage().timeouts().pageLoadTimeout(Integer.parseInt((String)sCaps.getCapability("pageLoadTimeout")), TimeUnit.MILLISECONDS);
        //set implicitlyWait time out if there is the corresponding setting
        if(sCaps.getCapability("implicitlyWait") != null)
            webDriver.manage().timeouts().implicitlyWait(Integer.parseInt((String)sCaps.getCapability("implicitlyWait")), TimeUnit.MILLISECONDS);
        
        return webDriver;
    }

	private static DesiredCapabilities configure(String CONFIG_FILE) throws FileNotFoundException, IOException{
        /*
         *  Read config file
         */
		Properties sConfig = new Properties();
        sConfig.load(new FileReader(CONFIG_FILE));

        /*
         *  Prepare capabilities
         */
        DesiredCapabilities sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot", sConfig.getProperty("takesScreenshot"));
        sCaps.setCapability("phantomjs.page.settings.loadImages", sConfig.getProperty("phantomjs_page_settings_loadImages"));
        //replace that with the webdriver timeout setting: pageLoadTimeOut
        //sCaps.setCapability("phantomjs.page.settings.resourceTimeout", sConfig.getProperty("phantomjs_page_settings_resourceTimeout"));
        sCaps.setCapability("pageLoadTimeout", sConfig.getProperty("pageLoadTimeout"));
        sCaps.setCapability("implicitlyWait", sConfig.getProperty("implicitlyWait"));
        sCaps.setCapability("thread_sleep_after_state_change", sConfig.getProperty("thread_sleep_after_state_change"));

        
        /*
         *  Fetch PhantomJS-specific configuration parameters
         */
        // "phantomjs_exec_path"
        if (sConfig.getProperty("phantomjs_exec_path") != null) {
            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, sConfig.getProperty("phantomjs_exec_path"));
        } else {
            throw new IOException(String.format("Property '%s' not set!", PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
        }
        // "phantomjs_driver_path"
        if (sConfig.getProperty("phantomjs_driver_path") != null) {
            System.out.println("Test will use an external GhostDriver");
            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY, sConfig.getProperty("phantomjs_driver_path"));
        } else {
            System.out.println("Test will use PhantomJS internal GhostDriver");
        }
    
        
        
        /*
         *  Disable "web-security", enable all possible "ssl-protocols" and "ignore-ssl-errors" for PhantomJSDriver
         */
        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);

        /*
         *  Control LogLevel for GhostDriver, via CLI arguments
         */
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] {
            "--logLevel=" + (sConfig.getProperty("phantomjs_driver_loglevel") != null ? sConfig.getProperty("phantomjs_driver_loglevel") : "INFO")
        });

        return sCaps;
	}	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]) throws Exception{
		
		
		//attribute names that include valuable text in image elements
		ImmutableList<String> IMG_ATTR_WITH_TEXT_LIST = new ImmutableList.Builder<String>().addAll(Arrays.asList("alt","src","value","title","name", "id")).build();
		ImmutableList<String> LINK_TAG_NAME_LIST = new ImmutableList.Builder<String>().addAll(Arrays.asList("a","input")).build();

	    final String CONFIG_FILE = "./config/ghostdriver/config.ini";
	    Driver ghostDriver = new GhostDriver(CONFIG_FILE);
	    ghostDriver.maximizeBrowserWindow();
		final String url = "http://bethefuture.nl/vacatures-2/";
//		final String url = "http://www.careers.macquarie.com/jobSearch.asp?stp=WEBSITE&lLocationGroupID_Expand=1";
//		final String url = "https://hca.taleo.net/careersection/newskylinemadisoncampus/jobsearch.ftl";
		
		
		
		try{
        	//1. get page
    		ghostDriver.get(url);
			
			ghostDriver.closeAlerts();
//			System.out.println("\t#url:"+ ghostDriver.getCurrentUrl());			
//			ghostDriver.clickElement(FindElementBy.xpath, "/html[1]/body[1]/div[8]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]", false);
			
//			boolean s = ghostDriver.clickElement(FindElementBy.xpath, "/html[1]/body[1]/div[1]/div[1]/footer[1]/div[1]/div[2]/ul[1]/li[5]/a[1]", false);
//			System.out.println(s+"\t#url:"+ ghostDriver.getCurrentUrl());
			long start = System.currentTimeMillis();
        	List<Link> links = ghostDriver.getLinks(FindElementBy.tagName, "html", LINK_TAG_NAME_LIST,IMG_ATTR_WITH_TEXT_LIST);
    		System.out.println("Time:"+(System.currentTimeMillis() - start));
        	System.out.println("\t#links:"+ links.size());
        	for(Link l:links){
        		System.out.println(l.getAttributeValue("href")+"\t"+l.getText()+"\t"+l.getAttributesMap().keySet()+"\t"+l.getAttributeValue("src")+"\t" +l.getXpath()+"\t" +l.getText()+"\t" +l.getText()+"\t"+l.getVisualInfoOfHtmlElement().toString());
        	}
		}catch(WebDriverException e){
    		e.printStackTrace();
    	}
		
	    ghostDriver.quit();
	    
	    //a();
	}
	
	public static void a(){
		HtmlUnitDriver s=new HtmlUnitDriver();
		
		s.get("http://www.textkernel.com/jobs/");
		List<WebElement> a = s.findElements(By.tagName("a"));
		for(WebElement c: a)
			System.out.println(c.getText()+"\t"+c.isDisplayed());
		s.quit();
	}
}
