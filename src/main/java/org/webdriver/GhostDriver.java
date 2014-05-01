package org.webdriver;

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
import org.webdriver.domain.Link;


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
	    final String CONFIG_FILE = "./config/ghostdriver/config.ini";
	    Driver ghostDriver = new GhostDriver(CONFIG_FILE);

		final String url = "http://www.iir.nl/vacatures/";
		
    	ghostDriver.get(url);
    	try{
//    		ghostDriver.clickElement("tagName", "a", true);
//    		ghostDriver.switchToFrame("sss", 1);
//    		List<Frame> f = ghostDriver.getFrames(Arrays.asList("iframe"));
//    		System.out.println(f.size());
//	    	ghostDriver.selectOptions("tagName", "body", Arrays.asList("select"));
//	    	ghostDriver.getPageSource();
//	    	System.out.println("#current title:"+ ghostDriver.getTitle());
//	    	System.out.println("#current url:"+ ghostDriver.getCurrentUrl());
    	}catch(WebDriverException e){
    		e.printStackTrace();
    	}
    	
    	List<Link> links = ghostDriver.getLinks("tagName", "body", Arrays.asList("input"));//    	
    	System.out.println("#links:"+ links.size());
    	for(Link l:links)
    		System.out.println(l.toString());
    	
	    ghostDriver.quit();
	}
}
