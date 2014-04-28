package org.webdriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;
import org.webdriver.domain.VisualInfoOfHtmlElement;

public class SeleniumImpl implements Driver {

	
	private WebDriver webDriver;
	//TODO setScriptTimeout
    private JavascriptExecutor js;
	private final int THREAD_SLEEP_AFTER_STATE_CHANGE = 1000;
	
	
	public SeleniumImpl(org.openqa.selenium.WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
		js = (JavascriptExecutor) webDriver; 
	}

	
	
	
	
	

	
	
	@Override
	public List<Link> getLinks() {
		List<Link> linkList = new ArrayList<Link>();
		for(String linkTagName:LINK_TAG_NAME_LIST){
			List<WebElement> elementsList = webDriver.findElements(By.tagName(linkTagName));
			for(WebElement webElement:elementsList){
				String tagName = webElement.getTagName();
				String anchorText = webElement.getText();
				//visual info of current element
				VisualInfoOfHtmlElement visualInfoOfHtmlElement = getVisualInfoOfHtmlElement(webElement);
				//get attributes Map
				Map<String,String> elementAttrMap = getElementAttributes(webElement);
				
				//TODO get the fucking XPATHS!!!
//				String elementParentXPath = XPathHelper.getXPathExpression(element.getParentNode());
				String element =  (String) js.executeScript(" return arguments[0].toString();", webElement);
				System.out.println(element);
				
				
				//construct and add link to final output list
				linkList.add(new Link(tagName, elementAttrMap, anchorText, null, null, visualInfoOfHtmlElement));
			}
		}
		return linkList;
	}
	
	
	
	@Override
	public List<Link> getElementChildLinks(String method, String value) {
		//WebElement we = findElement(method, value);
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String getPageSource() {
		return webDriver.getPageSource();
	}

	@Override
	public String getTitle() {
//		return webDriver.getTitle();
		return (String) js.executeScript(" return document.title;", webDriver.findElement(By.tagName("html")));
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
	public int getNumberOfOpenWindows() {
		return webDriver.getWindowHandles().size();
	}

	@Override
	public boolean switchToFrame(String method, Object value) {
		try{
			findFrame(method, value);
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
		}catch(InvalidSelectorException e){
			System.out.println("Exception durring switchToFrame:"+e.getMessage());
			return false;
		}catch(ClassCastException e){
			System.out.println("Exception durring switchToFrame:"+e.getMessage());
			return false;
		} catch (InterruptedException e) {
			System.out.println("InterruptedException during switchToFrame:"+e.getMessage());
			return false;
		}
		return true;
	}
	
	
	@Override
	public boolean get(String url) {
		if(url == null || url.isEmpty())
			return false;
		
		try{
			webDriver.get(url);
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
		}catch(TimeoutException timeoutException){
			System.out.println("Timeout during page loading:"+url+"\tException:"+timeoutException.getMessage());
			return false;
		} catch (InterruptedException e) {
			System.out.println("InterruptedException during page loading:"+url+"\tException:"+e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public boolean clickLink(String method, String value, boolean openInNewWindow) {
		try{
			WebElement we = findElement(method, value);
			we.click();
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
		}catch(NoSuchElementException e){
			System.out.println("Exception durring clickLink:"+e.getMessage());
			return false;
		}catch(StaleElementReferenceException e){
			System.out.println("Exception durring clickLink:"+e.getMessage());
			return false;
		} catch (InterruptedException e) {
			System.out.println("InterruptedException during clickLink:"+e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public List<Frame> getFrames() {
		List<Frame> frameList = new ArrayList<Frame>();
		int index_of_last_frame = 0;
		for(String frameTagName:FRAME_TAG_NAME_LIST){
			List<WebElement> elementsList = webDriver.findElements(By.tagName(frameTagName));
			for(WebElement webElement:elementsList){
				String tagName = webElement.getTagName();
				//get attributes Map
				Map<String,String> elementAttrMap = getElementAttributes(webElement);
				//construct and add frame to final output list
				frameList.add(new Frame(tagName, elementAttrMap, index_of_last_frame));
				index_of_last_frame++;
			}
		}
		return frameList;
	}
	
	
	
	


	private Map<String,String> getElementAttributes(WebElement webElement){
        Map<String,String> elementAttrMap = new HashMap<String,String>();
        try{
			@SuppressWarnings({ "unchecked" })
			ArrayList<String> parentAttributes = (ArrayList<String>) js.executeScript(
					"var s = []; var attrs = arguments[0].attributes; for (var l = 0; l < attrs.length; ++l) { var a = attrs[l]; s.push(a.name + 'mimis' + a.value); } ; return s;", webElement);
			
			for(String attr : parentAttributes ){
				String[] attrArr = attr.split("mimis");
				if(attrArr.length == 2)
					elementAttrMap.put(attrArr[0], attrArr[1]);
			}
        }catch(Exception e){
        	e.printStackTrace();
        	return elementAttrMap;
        }
    	return elementAttrMap;	        
	}
	

	
	private WebElement findElement(String method, String value) throws NoSuchElementException,InvalidSelectorException{
		WebElement we = null;
		
		if(method.equalsIgnoreCase("xpath"))
			we = webDriver.findElement(By.xpath(value));
		else if(method.equalsIgnoreCase("name"))
			we = webDriver.findElement(By.name(value));
		else if(method.equalsIgnoreCase("className"))
			we = webDriver.findElement(By.className(value));
		else if(method.equalsIgnoreCase("linkText"))
			we = webDriver.findElement(By.linkText(value));
		else
			throw new InvalidSelectorException("Invalid method("+method+") to find an element");
		
		return we;
	}


	private void findFrame(String method, Object value) throws InvalidSelectorException, ClassCastException{		
//		if(method.equalsIgnoreCase("index"))
//			webDriver.switchTo().frame((Integer)value);
		if(method.equalsIgnoreCase("nameOrId"))
			webDriver.switchTo().frame((String)value);
		else
			throw new InvalidSelectorException("Invalid method("+method+") to find a frame");
	}


	private VisualInfoOfHtmlElement getVisualInfoOfHtmlElement(WebElement webElement){
		return new VisualInfoOfHtmlElement(webElement.getSize(), webElement.getLocation(), webElement.isDisplayed(),webElement.getCssValue("font-size"),webElement.getCssValue("font-weight"),webElement.getCssValue("color"));
	}

}
