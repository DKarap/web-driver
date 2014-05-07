package org.webdriver.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;
import org.webdriver.domain.VisualInfoOfHtmlElement;
import org.webdriver.domain.WebPage;

public class SeleniumImpl implements Driver {

	
	private WebDriver webDriver;
    private JavascriptExecutor js; //TODO setScriptTimeout
	private final int THREAD_SLEEP_AFTER_STATE_CHANGE = 1000;
	
	
	public SeleniumImpl(org.openqa.selenium.WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
		js = (JavascriptExecutor) webDriver; 
	}

	
	@Override
	public WebPage getCurrentWebPage(int id, Collection<String> FRAME_TAG_NAME_LIST, Collection<String> LINK_TAG_NAME_LIST) throws WebDriverException {
		String url = getCurrentUrl();
		String title = getTitle();
		String sourceCode = getPageSource();
		return new WebPage(id, url, title, sourceCode, getFrames(FRAME_TAG_NAME_LIST), getLinks("tagName", "body", LINK_TAG_NAME_LIST));
	}

	
	@Override
	public boolean get(String url) throws WebDriverException{
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
	public int getNumberOfOpenWindows() throws WebDriverException{
		return webDriver.getWindowHandles().size();
	}

	
	@Override
	public void closeAllOtherOpenWindows() throws WebDriverException{
    	String current = this.webDriver.getWindowHandle();
        for (String handle : this.webDriver.getWindowHandles()) {
        	if (!handle.equals(current)) {
        		
        		this.webDriver.switchTo().window(handle);
        		System.out.println("opened window:"+getCurrentUrl());
//				closeAlerts();

                this.webDriver.close();
                this.webDriver.switchTo().window(current);
                System.out.println("current:"+getCurrentUrl());
//				closeAlerts();
            }
        }
	}


	
	@Override
	public String getPageSource() throws WebDriverException{
		return webDriver.getPageSource();
	}

	@Override
	public String getTitle() throws WebDriverException{
//		return webDriver.getTitle();
		return (String) js.executeScript(" return document.title;", webDriver.findElement(By.tagName("html")));
	}

	@Override
	public String getCurrentUrl() throws WebDriverException{
		return webDriver.getCurrentUrl();
	}

	@Override
	public void quit() throws WebDriverException{
		webDriver.quit();
	}
	

	@Override
	public void selectOptions(String method, String value, Collection<String> textToSelect) throws WebDriverException{
		WebElement initialElement = null;
		List<WebElement> selectElementList = null; 
		try{
			initialElement = findElement(method, value);
			selectElementList	= initialElement.findElements(By.tagName("select"));
		}catch(NoSuchElementException e){
			System.out.println("Fail to find the initial element or page desnt include select elements:"+e.getLocalizedMessage());
			return;
		}
		
		 
		for(WebElement selectElement : selectElementList){
			
			List<WebElement> allCurrentOptions = null;
			try{
				allCurrentOptions = selectElement.findElements(By.tagName("option"));
			}catch(NoSuchElementException e){
				System.out.println("Current select element doesnt include options..continue with next select element:"+e.getMessage());
				continue;
			}
			
			for (WebElement option : allCurrentOptions) {
				/*
				 * TODO IS the element that we looking for to select?(this need refactoring)
				 */
				String currentOptionText = option.getText().toLowerCase().trim();
				if(textToSelect.contains(currentOptionText)){
//				if(optionText.matches("(.*\\s+?\\W{0,1}|^\\W{0,1})"+rel+"(\\W{0,1}\\s+?.*|\\W{0,1}$)")){
					
					try{
						option.click();
					}catch(StaleElementReferenceException e){
						System.out.println("Exception with the selected option element:"+e.getMessage());
						continue;
					}
					break;
				}
			}
		}
	}

	
	
	
	@Override
	public boolean switchToFrame(String method, Object value) throws WebDriverException{
		try{
			
			if(method.equalsIgnoreCase("index"))
				webDriver.switchTo().frame((Integer)value);
			else if(method.equalsIgnoreCase("nameOrId"))
				webDriver.switchTo().frame((String)value);
			else{
				System.out.println("Invalid method("+method+") to find a frame");
				return false;
			}
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
			
			
		}catch(NoSuchFrameException e){
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
	public List<Frame> getFrames(Collection<String> FRAME_TAG_NAME_LIST) throws WebDriverException{
		List<Frame> frameList = new ArrayList<Frame>();
		int index_of_last_frame = 0;
		
		for(String frameTagName:FRAME_TAG_NAME_LIST){
			
			List<WebElement> elementsList = null;
			try{
				elementsList = webDriver.findElements(By.tagName(frameTagName));
			}catch(NoSuchElementException e){
				System.out.println("Current page doesnt include "+frameTagName+" tags - continue with the next frame tag:"+e.getMessage());
				continue;
			}
			
			for(WebElement webElement:elementsList){
				//this may produce a webdriver exception..in that case stop
				String tagName = webElement.getTagName();
				//get element's attr; safe process with respect to exception handling
				Map<String,String> elementAttrMap = getElementAttributes(webElement);
				
				//construct and add frame to final output list
				frameList.add(new Frame(tagName, elementAttrMap, index_of_last_frame));
				index_of_last_frame++;
			}
		}
		return frameList;
	}
	

	@Override
	public boolean clickElement(String method, String value, boolean openInNewWindow) throws WebDriverException{
		try{
			
			WebElement we = findElement(method, value);
			if(openInNewWindow){
				js.executeScript("arguments[0].setAttribute('target', '_blank');", we);
			}
			we.click();
			//wait some time to load..
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
			
		}catch(NoSuchElementException e){
			System.out.println("NoSuchElementException durring clickLink:"+e.getMessage());
			return false;
		}catch(StaleElementReferenceException e){
			System.out.println("StaleElementReferenceException durring clickLink:"+e.getMessage());
			return false;
		} catch (InterruptedException e) {
			System.out.println("InterruptedException during clickLink:"+e.getMessage());
			return false;
		} catch (ElementNotVisibleException e) {
			System.out.println("ElementNotVisibleException during clickLink:"+e.getMessage());
			return false;
		}

		return true;
	}

	
	
	
	@Override
	public List<Link> getLinks(String method, Object value, Collection<String> LINK_TAG_NAME_LIST) throws WebDriverException{
		List<Link> linkList = new ArrayList<Link>();
		
		WebElement initialElement = null;
		try{
			initialElement = findElement(method, value);
		}catch(NoSuchElementException e){
			System.out.println("Fail to find the initial element:"+e.getLocalizedMessage());
			return linkList;
		}
		
		
		for(String linkTagName:LINK_TAG_NAME_LIST){
			List<WebElement> elementsList = null;
			try{
				elementsList = initialElement.findElements(By.tagName(linkTagName));
			}catch(NoSuchElementException e){
				System.out.println("Current page doesnt include "+linkTagName+" tag - continue with next tag:"+e.getMessage());
				continue;
			}
			
			for(WebElement webElement:elementsList){
				String tagName = webElement.getTagName();
				String anchorText = webElement.getText();
				
				//visual info of current element
				VisualInfoOfHtmlElement visualInfoOfHtmlElement = getVisualInfoOfHtmlElement(webElement);
				
				//get attributes Map
				Map<String,String> elementAttrMap = getElementAttributes(webElement);
				
				//get the fucking XPATHS!!
				String xpath = getAbsoluteXpathFromWebElement(webElement);
				
				//construct and add link to final output list
				linkList.add(new Link(tagName, elementAttrMap, anchorText, xpath, visualInfoOfHtmlElement));
			}
		}
		return linkList;
	}
	


	private VisualInfoOfHtmlElement getVisualInfoOfHtmlElement(WebElement webElement) throws WebDriverException{
		return new VisualInfoOfHtmlElement(webElement.getSize(), webElement.getLocation(), webElement.isDisplayed(),webElement.getCssValue("font-size"),webElement.getCssValue("font-weight"),webElement.getCssValue("color"));
	}
	
	
	
	private String getAbsoluteXpathFromWebElement(WebElement element) throws WebDriverException{
		return (String) js.executeScript(
						"function getXPathExpression(element){"+
							"var paths = [];"+
							"for (; element && element.nodeType == 1; element = element.parentNode){"+
								"var index = 0;"+
								"for (var sibling = element.previousSibling; sibling; sibling = sibling.previousSibling){"+
									"if (sibling.nodeType == Node.DOCUMENT_TYPE_NODE)"+
										"continue;"+
									"if (sibling.nodeName == element.nodeName)"+
										"++index;"+
								"}"+
								"var tagName = element.nodeName.toLowerCase();"+
								"var pathIndex = (index ? \"[\" + (index+1) + \"]\" : \"[1]\");"+
								"paths.splice(0, 0, tagName + pathIndex);"+
							"}"+
							"return paths.length ? \"/\" + paths.join(\"/\") : null;"+
						"}"+
						"return getXPathExpression(arguments[0]);",
						element);
	}
	
	
	/**
	 * 
	 * @param webElement
	 * @return a map with key the attr name and value its value; if an exception happened then return 
	 */
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


	private WebElement findElement(String method, Object value) throws WebDriverException, NoSuchElementException,InvalidSelectorException{
		WebElement we = null;
		if(method.equalsIgnoreCase("xpath"))
			we = webDriver.findElement(By.xpath((String) value));
		else if(method.equalsIgnoreCase("tagName"))
			we = webDriver.findElement(By.tagName((String) value));
		else if(method.equalsIgnoreCase("name"))
			we = webDriver.findElement(By.name((String)value));
		else if(method.equalsIgnoreCase("className"))
			we = webDriver.findElement(By.className((String)value));
		else if(method.equalsIgnoreCase("linkText"))
			we = webDriver.findElement(By.linkText((String)value));
		else
			throw new InvalidSelectorException("Invalid method("+method+") to find an element");
		
		return we;
	}

}
