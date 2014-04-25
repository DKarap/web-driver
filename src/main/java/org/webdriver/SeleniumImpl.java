package org.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webdriver.domain.Link;

public class SeleniumImpl implements Driver {

	
	private WebDriver webDriver;

	public SeleniumImpl(org.openqa.selenium.WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
	}

	
	@Override
	public String getPageSource() {
		//TODO we may clean the code before send it back @see pageSourceCodetoUniformVersion()
		return webDriver.getPageSource();
	}

	@Override
	public String getTitle() {
		return webDriver.getTitle();
	}

	@Override
	public String getCurrentUrl() {
		return webDriver.getCurrentUrl();
	}

	@Override
	public void quit() {
		webDriver.quit();
	}	
	
	@Override
	public boolean get(String url) {
		if(url == null || url.isEmpty())
			return false;
		
		try{
			webDriver.get(url);
		}catch(TimeoutException timeoutException){
			System.out.println("Timeout during page loading:"+url+"\tException:"+timeoutException.getMessage());
			return false;
		}
		return true;
	}
	
	
		
	
	@Override
	public boolean clickLink(String method, String value, boolean openInNewWindow) {
		try{
			WebElement we = findElement(method, value);
			we.click();
		}catch(NoSuchElementException e){
			System.out.println("Exception durring clickLink:"+e.getMessage());
			return false;
		}catch(StaleElementReferenceException e){
			System.out.println("Exception durring clickLink:"+e.getMessage());
			return false;
		}
		return true;
	}

	private WebElement findElement(String method, String value){
		WebElement we = null;
		
		if(method.equalsIgnoreCase("xpath"))
			we = webDriver.findElement(By.xpath(value));
		else if(method.equalsIgnoreCase("name"))
			we = webDriver.findElement(By.name(value));
		else if(method.equalsIgnoreCase("className"))
			we = webDriver.findElement(By.className(value));
		else
			throw new InvalidSelectorException("Invalid method("+method+") to find an element");
		
		return we;
	}

	
	
	
	@Override
	public List<Link> getPageLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Link> getElementChildLinksByXpath(String xpath) {
		// TODO Auto-generated method stub
		return null;
	}
	


	
}
