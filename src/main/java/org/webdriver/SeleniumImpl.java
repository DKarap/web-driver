package org.webdriver;

import java.util.ArrayList;
import java.util.Collection;
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
    private JavascriptExecutor js; //TODO setScriptTimeout
	private final int THREAD_SLEEP_AFTER_STATE_CHANGE = 1000;
	
	
	public SeleniumImpl(org.openqa.selenium.WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
		js = (JavascriptExecutor) webDriver; 
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void selectOptions(String method, String value, Collection<String> textToSelect) {
		WebElement initialElement = null;
		try{
			initialElement = findElement(method, value);
		}catch(NoSuchElementException e){
			System.out.println(e.getMessage());
			return;
		}
		
		List<WebElement> selectElementList = initialElement.findElements(By.tagName("select"));
		for(WebElement selectElement : selectElementList){
			List<WebElement> allCurrentOptions = selectElement.findElements(By.tagName("option"));
			for (WebElement option : allCurrentOptions) {
				String currentOptionText = option.getText().toLowerCase().trim();
				if(textToSelect.contains(currentOptionText)){
//				if(optionText.matches("(.*\\s+?\\W{0,1}|^\\W{0,1})"+rel+"(\\W{0,1}\\s+?.*|\\W{0,1}$)")){
					option.click();
					break;
				}
			}
		}
	}


	
	
	
	
	
	
	
	
	
	
	


	
	
	@Override
	//TODO open new window...
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
	public List<Link> getLinks(String method, Object value, Collection<String> LINK_TAG_NAME_LIST) {
		List<Link> linkList = new ArrayList<Link>();
		
		WebElement initialElement = null;
		try{
			initialElement = findElement(method, value);
		}catch(NoSuchElementException e){
			System.out.println(e.getMessage());
			return linkList;
		}
		
		for(String linkTagName:LINK_TAG_NAME_LIST){
			List<WebElement> elementsList = initialElement.findElements(By.tagName(linkTagName));
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
	public List<Frame> getFrames(Collection<String> FRAME_TAG_NAME_LIST) {
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
	

	
	private WebElement findElement(String method, Object value) throws NoSuchElementException,InvalidSelectorException{
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


	private void findFrame(String method, Object value) throws InvalidSelectorException, ClassCastException{		
		if(method.equalsIgnoreCase("index"))
			webDriver.switchTo().frame((Integer)value);
		else if(method.equalsIgnoreCase("nameOrId"))
			webDriver.switchTo().frame((String)value);
		else
			throw new InvalidSelectorException("Invalid method("+method+") to find a frame");
	}


	private VisualInfoOfHtmlElement getVisualInfoOfHtmlElement(WebElement webElement){
		return new VisualInfoOfHtmlElement(webElement.getSize(), webElement.getLocation(), webElement.isDisplayed(),webElement.getCssValue("font-size"),webElement.getCssValue("font-weight"),webElement.getCssValue("color"));
	}
	
	
	
	private String getAbsoluteXpathFromWebElement(WebElement element) {
		return (String) js.executeScript(
						"function absoluteXPath(element) {"
								+ "var comp, comps = [];"
								+ "var parent = null;"
								+ "var xpath = '';"
								+ "var getPos = function(element) {"
								+ "var position = 1, curNode;"
								+ "if (element.nodeType == Node.ATTRIBUTE_NODE) {"
								+ "return null;"
								+ "}"
								+ "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling) {"
								+ "if (curNode.nodeName == element.nodeName) {"
								+ "++position;"
								+ "}"
								+ "}"
								+ "return position;"
								+ "};"
								+

								"if (element instanceof Document) {"
								+ "return '/';"
								+ "}"
								+

								"for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {"
								+ "comp = comps[comps.length] = {};"
								+ "switch (element.nodeType) {"
								+ "case Node.TEXT_NODE:"
								+ "comp.name = 'text()';" + "break;"
								+ "case Node.ATTRIBUTE_NODE:"
								+ "comp.name = '@' + element.nodeName;"
								+ "break;"
								+ "case Node.PROCESSING_INSTRUCTION_NODE:"
								+ "comp.name = 'processing-instruction()';"
								+ "break;" + "case Node.COMMENT_NODE:"
								+ "comp.name = 'comment()';" + "break;"
								+ "case Node.ELEMENT_NODE:"
								+ "comp.name = element.nodeName;" + "break;"
								+ "}" + "comp.position = getPos(element);"
								+ "}" +

								"for (var i = comps.length - 1; i >= 0; i--) {"
								+ "comp = comps[i];"
								+ "xpath += '/' + comp.name.toLowerCase();"
								+ "if (comp.position !== null) {"
								+ "xpath += '[' + comp.position + ']';" + "}"
								+ "}" +

								"return xpath;" +

								"} return absoluteXPath(arguments[0]);",
						element);
	}

}
