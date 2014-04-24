package org.webdriver;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Tests base class.
 * Takes care of initialising the Remote WebDriver
 */
public  class App {
    private WebDriver mDriver                      = null;
    private boolean mAutoQuitDriver                = true;

    private static final String CONFIG_FILE        = "./config/ghostdriver/config.ini";
    private static final String DRIVER_FIREFOX     = "firefox";
    private static final String DRIVER_CHROME      = "chrome";
    private static final String DRIVER_PHANTOMJS   = "phantomjs";

    protected static Properties sConfig;
    protected static DesiredCapabilities sCaps;

    
    public static void main(String args[]) throws InterruptedException{
    	
    	DesiredCapabilities caps = new DesiredCapabilities();
    	caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
    	                "/Users/mimis/Development/exe/phantom_js/phantomjs-1.9.7-macosx/bin/phantomjs");
    	caps.setCapability("takesScreenshot", false);

    	
    	WebDriver driver = new PhantomJSDriver(caps);
    	driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
    	driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
    	
    	
    	long time= System.currentTimeMillis();
    	
    	try{
    		driver.get("https://hp.taleo.net/careersection/2/jobsearch.ftl?lang=en");
    	}catch(TimeoutException e){
    		e.printStackTrace();
    	}
    	List<WebElement> weList = driver.findElements(By.tagName("a"));
//    	
//    	for(WebElement we: weList){
//    		try{
//    			System.out.println(we.getText()+"\t"+we.getAttribute("href"));
//    		}catch(StaleElementReferenceException e){
//    		}
//    		
//    	}
    	System.out.println(driver.getTitle()+"\tUrl:"+driver.getCurrentUrl()+"\tLinks:"+weList.size());
    	driver.findElement(By.xpath("//*[@id=\"requisitionListInterface.reqTitleLinkAction.row1\"]")).click();
    	System.out.println(driver.getTitle()+"\tUrl:"+driver.getCurrentUrl());
    	Thread.sleep(2000);
    	
    	driver.navigate().back();
    	System.out.println(driver.getTitle()+"\tUrl:"+driver.getCurrentUrl());
    	Thread.sleep(2000);
    	
    	System.out.println("#Time:"+(System.currentTimeMillis()-time));
    	driver.quit();
    }
    
    
    private static boolean isUrl(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException mue) {
            return false;
        }
    }

    public static void configure() throws IOException {
        // Read config file
        sConfig = new Properties();
        sConfig.load(new FileReader(CONFIG_FILE));

        // Prepare capabilities
        sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot", false);

        String driver = sConfig.getProperty("driver", DRIVER_PHANTOMJS);

        // Fetch PhantomJS-specific configuration parameters
        if (driver.equals(DRIVER_PHANTOMJS)) {
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
        }

        // Disable "web-security", enable all possible "ssl-protocols" and "ignore-ssl-errors" for PhantomJSDriver
//        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
//            "--web-security=false",
//            "--ssl-protocol=any",
//            "--ignore-ssl-errors=true"
//        });
        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);

        // Control LogLevel for GhostDriver, via CLI arguments
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] {
            "--logLevel=" + (sConfig.getProperty("phantomjs_driver_loglevel") != null ? sConfig.getProperty("phantomjs_driver_loglevel") : "INFO")
        });
    }

    public void prepareDriver() throws Exception
    {
        // Which driver to use? (default "phantomjs")
        String driver = sConfig.getProperty("driver", DRIVER_PHANTOMJS);

        // Start appropriate Driver
        if (isUrl(driver)) {
            sCaps.setBrowserName("phantomjs");
            mDriver = new RemoteWebDriver(new URL(driver), sCaps);
        } else if (driver.equals(DRIVER_FIREFOX)) {
            mDriver = new FirefoxDriver(sCaps);
        } else if (driver.equals(DRIVER_CHROME)) {
            mDriver = new ChromeDriver(sCaps);
        } else if (driver.equals(DRIVER_PHANTOMJS)) {
            mDriver = new PhantomJSDriver(sCaps);
        }
    }

    protected WebDriver getDriver() {
        return mDriver;
    }

    protected void disableAutoQuitDriver() {
        mAutoQuitDriver = false;
    }

    protected void enableAutoQuitDriver() {
        mAutoQuitDriver = true;
    }

    protected boolean isAutoQuitDriverEnabled() {
        return mAutoQuitDriver;
    }

    public void quitDriver() {
        if (mAutoQuitDriver && mDriver != null) {
            mDriver.quit();
            mDriver = null;
        }
    }
}