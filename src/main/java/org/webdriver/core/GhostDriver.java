package org.webdriver.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.webdriver.domain.FindElementBy;
import org.webdriver.domain.FindFrameBy;
import org.webdriver.domain.Frame;
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

	    final String CONFIG_FILE = "./config/ghostdriver/config.ini";
	    Driver ghostDriver = new GhostDriver(CONFIG_FILE);

//		final String url = "http://bethefuture.nl/vacatures-2/";
		final String url = "http://www.textkernel.com";
		

		try{
        	//1. get page
        	System.out.println("\tgeturl");
    		ghostDriver.get(url);
    		
    		boolean success = ghostDriver.clickElement(FindElementBy.xpath, "/HTML[1]/BODY[1]/DIV[1]/DIV[4]/DIV[2]/DIV[1]/DIV[1]/H3[1]/A[1]", true);
    		System.out.println("\tsuccess:"+success+"\topenWindows:"+ghostDriver.getNumberOfOpenWindows());
    		ghostDriver.closeAllOtherOpenWindows();
    		System.out.println("\topenWindows:"+ghostDriver.getNumberOfOpenWindows());
    		
    		
    		//2. get page source code
    		System.out.println("\tgetpagesource");
    		ghostDriver.getPageSource();
    		//3. get title
    		System.out.println("\tgettitle");
    		ghostDriver.getTitle();
    		//3. get current url
    		System.out.println("\tget current url");
    		ghostDriver.getCurrentUrl();
        	System.out.println("\turl:"+ghostDriver.getCurrentUrl());
    		//4. get links
    		System.out.println("\tget links");
        	List<Link> links = ghostDriver.getLinks(FindElementBy.tagName, "body", Arrays.asList("a"),IMG_ATTR_WITH_TEXT_LIST);
        	System.out.println("\t#links:"+ links.size());
        	for(Link l:links)
        		System.out.println(l.getAttributesMap().keySet()+"\t" +l.getXpath()+"\t" +l.getText()+"\t" +l.getText()+"\t"+l.getVisualInfoOfHtmlElement().toString());

    		//5. get frames
        	System.out.println("\tget frames");
        	List<Frame> frames = ghostDriver.getFrames( Arrays.asList("frame","iframe"));
        	System.out.println("\t#frames:"+ frames.size());
        	//6. switch to frame
        	System.out.println("\tswitch to frame");
        	if(frames.size()>0){
        		ghostDriver.switchToFrame(FindFrameBy.index, frames.get(0).getIndex());
        	}
    		System.out.println("\topenWindows:"+ghostDriver.getNumberOfOpenWindows());



		}catch(WebDriverException e){
    		e.printStackTrace();
    	}
	    ghostDriver.quit();
	}
}
