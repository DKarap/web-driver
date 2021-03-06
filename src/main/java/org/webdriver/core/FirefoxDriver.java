package org.webdriver.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriver extends SeleniumImpl{

	
	

	public FirefoxDriver(String CONFIG_FILE) throws Exception {
		super(prepareAndGetDriver(CONFIG_FILE));
	}
	
	

	private static WebDriver prepareAndGetDriver(String CONFIG_FILE) throws Exception{
    	DesiredCapabilities sCaps = configure(CONFIG_FILE);
		WebDriver webDriver = new org.openqa.selenium.firefox.FirefoxDriver();
        
        /*
         * WebDriver settings... 
         */
        //set page load time out if there is the corresponding setting
        if(sCaps.getCapability("pageLoadTimeout") != null)
        	webDriver.manage().timeouts().pageLoadTimeout(Integer.parseInt((String)sCaps.getCapability("pageLoadTimeout")), TimeUnit.MILLISECONDS);
        if(sCaps.getCapability("scriptTimeout") != null)
        	webDriver.manage().timeouts().setScriptTimeout(Integer.parseInt((String)sCaps.getCapability("scriptTimeout")), TimeUnit.MILLISECONDS);
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

//        /*
//         *  Prepare capabilities
//         */
        DesiredCapabilities sCaps = new DesiredCapabilities();
//        sCaps.setJavascriptEnabled(true);
//        sCaps.setCapability("takesScreenshot", sConfig.getProperty("takesScreenshot"));
//        sCaps.setCapability("phantomjs.page.settings.loadImages", sConfig.getProperty("phantomjs_page_settings_loadImages"));
//        //replace that with the webdriver timeout setting: pageLoadTimeOut
//        //sCaps.setCapability("phantomjs.page.settings.resourceTimeout", sConfig.getProperty("phantomjs_page_settings_resourceTimeout"));
//        sCaps.setCapability("pageLoadTimeout", sConfig.getProperty("pageLoadTimeout"));
//        sCaps.setCapability("implicitlyWait", sConfig.getProperty("implicitlyWait"));
//        sCaps.setCapability("thread_sleep_after_state_change", sConfig.getProperty("thread_sleep_after_state_change"));
//
//        
//        /*
//         *  Fetch PhantomJS-specific configuration parameters
//         */
//        // "phantomjs_exec_path"
//        if (sConfig.getProperty("phantomjs_exec_path") != null) {
//            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, sConfig.getProperty("phantomjs_exec_path"));
//        } else {
//            throw new IOException(String.format("Property '%s' not set!", PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
//        }
//        // "phantomjs_driver_path"
//        if (sConfig.getProperty("phantomjs_driver_path") != null) {
//            System.out.println("Test will use an external GhostDriver");
//            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY, sConfig.getProperty("phantomjs_driver_path"));
//        } else {
//            System.out.println("Test will use PhantomJS internal GhostDriver");
//        }
//    
//        
//        
//        /*
//         *  Disable "web-security", enable all possible "ssl-protocols" and "ignore-ssl-errors" for PhantomJSDriver
//         */
//        ArrayList<String> cliArgsCap = new ArrayList<String>();
//        cliArgsCap.add("--web-security=false");
//        cliArgsCap.add("--ssl-protocol=any");
//        cliArgsCap.add("--ignore-ssl-errors=true");
//        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
//
//        /*
//         *  Control LogLevel for GhostDriver, via CLI arguments
//         */
//        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] {
//            "--logLevel=" + (sConfig.getProperty("phantomjs_driver_loglevel") != null ? sConfig.getProperty("phantomjs_driver_loglevel") : "INFO")
//        });

        return sCaps;
	}	
	
}
