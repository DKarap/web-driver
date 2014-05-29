package org.webdriver.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import org.webdriver.domain.FindElementBy;
import org.webdriver.domain.FindFrameBy;
import org.webdriver.domain.Frame;
import org.webdriver.domain.Link;
import org.webdriver.domain.VisualInfoOfHtmlElement;
import org.webdriver.domain.WebPage;

public class SeleniumImpl implements Driver {

	
	private WebDriver webDriver;
    private JavascriptExecutor js; //TODO setScriptTimeout
	private StringBuilder log_buf;//whatever catches exception happened save it to this buffer
	
	//TODO these hardcoded settings must move out
	private final int THREAD_SLEEP_AFTER_STATE_CHANGE = 1000;

	
	public SeleniumImpl(org.openqa.selenium.WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
		js = (JavascriptExecutor) webDriver;
		this.log_buf = new StringBuilder();
	}

	

	@Override
	public String getLog() {
		return this.log_buf.toString();
	}


	@Override
	public String getDescription() {
		String description = null;
		try{
			description = (String) js.executeScript(
					"function GetMetaValue(element){"+
					  "var description;"+
					  "var metas = element.getElementsByTagName('meta');"+
					  "for (var x=0,y=metas.length; x<y; x++) {"+
						  "if (metas[x].name.toLowerCase() == \"description\") {"+
						  	"description = metas[x];"+
					      "}"+
					  "}"+
					   "return  description.content;"+
					"}"+
					"return GetMetaValue(arguments[0]);", webDriver.findElement(By.tagName("html")));
		}catch(NoSuchElementException e){
			log_buf.append("Fail to find the html initial element in order to get the page description via js:"+e.getLocalizedMessage()+"\n");
			return description;
		}catch(WebDriverException exception){
			log_buf.append("Fail to find the description of the page:"+exception.getLocalizedMessage()+"\n");
			return description;
		}
		return description;
	}

	
	
	
	
	@Override
	public WebPage getCurrentWebPage(int id, Collection<String> FRAME_TAG_NAME_LIST, Collection<String> LINK_TAG_NAME_LIST, Collection<String> IMG_ATTR_WITH_TEXT_LIST) throws WebDriverException {
		String url = getCurrentUrl();
		String title = getTitle();
		String description = getDescription();
		String sourceCode = getPageSource();
		return new WebPage(id, url, title, description, sourceCode, getFrames(FRAME_TAG_NAME_LIST), getLinks(FindElementBy.tagName, "body", LINK_TAG_NAME_LIST, IMG_ATTR_WITH_TEXT_LIST));
	}

	
	@Override
	public boolean get(String url) throws WebDriverException{
		if(url == null || url.isEmpty()){
			log_buf.append("url null or empty...\n");
			return false;
		}
		
		try{
			webDriver.get(url);
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
		}catch(TimeoutException timeoutException){
			log_buf.append("Timeout during page loading:"+url+"\tException:"+timeoutException.getMessage()+"\n");
			return false;
		} catch (InterruptedException e) {
			log_buf.append("InterruptedException during page loading:"+url+"\tException:"+e.getMessage()+"\n");
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
	public String getTitle() {
//		return webDriver.getTitle();
		String title = null;
		try{
			title = (String) js.executeScript(" return document.title;", webDriver.findElement(By.tagName("html")));
		}catch(NoSuchElementException e){
			log_buf.append("Fail to find the html initial element in order to get the page title via js:"+e.getLocalizedMessage()+"\n");
			return title;
		}catch(WebDriverException exception){
			log_buf.append("Fail to find the title of the page:"+exception.getLocalizedMessage()+"\n");
			return title;
		}
		return title;
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
	public void selectOptions(FindElementBy by, String value, Collection<String> textToSelect) throws WebDriverException{
		WebElement initialElement = null;
		List<WebElement> selectElementList = null; 
		try{
			initialElement = findElement(by, value);
			selectElementList	= initialElement.findElements(By.tagName("select"));
		}catch(NoSuchElementException e){
			log_buf.append("Fail to find the initial element or page desnt include select elements:"+e.getLocalizedMessage()+"\n");
			return;
		}
		
		 
		for(WebElement selectElement : selectElementList){
			
			List<WebElement> allCurrentOptions = null;
			try{
				allCurrentOptions = selectElement.findElements(By.tagName("option"));
			}catch(NoSuchElementException e){
				log_buf.append("Current select element doesnt include options..continue with next select element:"+e.getMessage()+"\n");
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
						log_buf.append("Exception with the selected option element:"+e.getMessage()+"\n");
						continue;
					}
					break;
				}
			}
		}
	}

	
	
	
	@Override
	public boolean switchToFrame(FindFrameBy by, Object value) throws WebDriverException{
		try{
			
			if(by.equals(FindFrameBy.index))
				webDriver.switchTo().frame((Integer)value);
			else if(by.equals(FindFrameBy.nameOrId))
				webDriver.switchTo().frame((String)value);
			else{
				log_buf.append("Invalid method("+by+") to find a frame\n");
				return false;
			}
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
			
			
		}catch(NoSuchFrameException e){
			log_buf.append("Exception durring switchToFrame:"+e.getMessage()+"\n");
			return false;
		}catch(ClassCastException e){
			log_buf.append("Exception durring switchToFrame:"+e.getMessage()+"\n");
			return false;
		} catch (InterruptedException e) {
			log_buf.append("InterruptedException during switchToFrame:"+e.getMessage()+"\n");
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
				log_buf.append("Current page doesnt include "+frameTagName+" tags - continue with the next frame tag:"+e.getMessage()+"\n");
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
	public boolean clickElement(FindElementBy by, String value, boolean openInNewWindow) throws WebDriverException{
		try{
			
			WebElement we = findElement(by, value);
			if(openInNewWindow){
				js.executeScript("arguments[0].setAttribute('target', '_blank');", we);
			}
			we.click();
			//wait some time to load..
			Thread.sleep(THREAD_SLEEP_AFTER_STATE_CHANGE);
			
		}catch(NoSuchElementException e){
			log_buf.append("NoSuchElementException durring clickLink:"+e.getMessage()+"\n");
			return false;
		}catch(StaleElementReferenceException e){
			log_buf.append("StaleElementReferenceException durring clickLink:"+e.getMessage()+"\n");
			return false;
		} catch (InterruptedException e) {
			log_buf.append("InterruptedException during clickLink:"+e.getMessage()+"\n");
			return false;
		} catch (ElementNotVisibleException e) {
			log_buf.append("ElementNotVisibleException during clickLink:"+e.getMessage()+"\n");
			return false;
		}catch(TimeoutException timeoutException){
			log_buf.append("Timeout during clickLink\tException:"+timeoutException.getMessage()+"\n");
			return false;
		}

		return true;
	}

	
	
	
	@Override
	public List<Link> getLinks(FindElementBy by, Object value, Collection<String> LINK_TAG_NAME_LIST, Collection<String> IMG_ATTR_WITH_TEXT_LIST) throws WebDriverException{
		List<Link> linkList = new ArrayList<Link>();
		
		WebElement initialElement = null;
		try{
			initialElement = findElement(by, value);
		}catch(NoSuchElementException e){
			log_buf.append("Fail to find the initial element:"+e.getLocalizedMessage()+"\n");
			return linkList;
		}
		
		for(String linkTagName:LINK_TAG_NAME_LIST){
			List<WebElement> elementsList = null;
			try{
				elementsList = initialElement.findElements(By.tagName(linkTagName));
			}catch(NoSuchElementException e){
				log_buf.append("Current page doesnt include "+linkTagName+" tag - continue with next tag:"+e.getMessage()+"\n");
				continue;
			}
			
			for(WebElement webElement:elementsList){
				try{
					//TODO issue #21 - skip invisible elements 
					if(!webElement.isDisplayed() || !webElement.isEnabled()){
						//System.out.println("is not displayed:"+webElement.getText()+"\t"+webElement.getAttribute("id"));
						continue;
					}
					
					
					String tagName = webElement.getTagName();
					String anchorText = webElement.getText();
					
					//visual info of current element
					VisualInfoOfHtmlElement visualInfoOfHtmlElement = getVisualInfoOfHtmlElement(webElement);
					
					//get attributes Map
					Map<String,String> elementAttrMap = getElementAttributes(webElement);
					
					//get the fucking XPATHS!!
					String xpath = getAbsoluteXpathFromWebElement(webElement);
					String xpath_by_id = getWebElementXpathById(webElement);
					xpath_by_id = xpath_by_id != xpath ? xpath_by_id : null;
					
					
					// issue #12
					getImgChildElementTextAtributesValue(webElement, elementAttrMap,IMG_ATTR_WITH_TEXT_LIST);
					
					
					//construct and add link to final output list
					linkList.add(new Link(tagName, elementAttrMap, anchorText, xpath, xpath_by_id, visualInfoOfHtmlElement));
				}catch(StaleElementReferenceException e){
					log_buf.append("Current element changed..."+e.getMessage()+"\n");
					continue;
				}
			}
		}
		return linkList;
	}
	
	@Override
    public List<String> getWebElementChildsText(WebElement webElement)throws WebDriverException{
    	List<String> clids_text_list = new ArrayList<String>();
		try{
			List<WebElement> child_elements = webElement.findElements(By.xpath(".//*"));
			for(WebElement child_element : child_elements){
				if(!child_element.getText().isEmpty())
					clids_text_list.add(StringUtils.chomp(child_element.getText()).replaceAll("\\s+", " ").trim());
			}
		}catch(NoSuchElementException e){
			return clids_text_list;
		}
		return clids_text_list;
    }

	@Override
	public Link getLink(FindElementBy by, String value, Collection<String> IMG_ATTR_WITH_TEXT_LIST) throws WebDriverException{
		try{
			WebElement webElement = findElement(by, value);
			if(!webElement.isDisplayed() || !webElement.isEnabled())
				return null;
			String tagName = webElement.getTagName();
			String anchorText = webElement.getText();
			Map<String,String> elementAttrMap = getElementAttributes(webElement);
			getImgChildElementTextAtributesValue(webElement, elementAttrMap,IMG_ATTR_WITH_TEXT_LIST);
			return new Link(tagName, elementAttrMap, anchorText, null, null, null);			
		}catch(StaleElementReferenceException e){
			log_buf.append("Current element changed..."+e.getMessage()+"\n");
			return null;
		}catch(NoSuchElementException e){
			log_buf.append("Fail to find the  element:"+e.getLocalizedMessage()+"\n");
			return null;
		}
	}



	@Override
	public WebElement getWebElement(FindElementBy by, String value) throws WebDriverException {
		try{
			WebElement webElement = findElement(by, value);
			if(!webElement.isDisplayed() || !webElement.isEnabled())
				return null;
			return webElement;			
		}catch(StaleElementReferenceException e){
			log_buf.append("Current element changed..."+e.getMessage()+"\n");
			return null;
		}catch(NoSuchElementException e){
			log_buf.append("Fail to find the  element:"+e.getLocalizedMessage()+"\n");
			return null;
		}
	}

	
	//check the child elements which one is an img, and then retrieve and save the predefined attributes
	private void getImgChildElementTextAtributesValue(WebElement webElement, Map<String,String> elementAttrMap, Collection<String> IMG_ATTR_WITH_TEXT_LIST)throws WebDriverException{
		try{
			List<WebElement> child_elements = webElement.findElements(By.xpath(".//*"));
			for(WebElement img_child_element : child_elements){
				if(img_child_element.getTagName().equalsIgnoreCase("img")){
					for(String img_attr_with_text:IMG_ATTR_WITH_TEXT_LIST){
						String attr_value = img_child_element.getAttribute(img_attr_with_text);
						if( attr_value!= null && !attr_value.isEmpty() && !elementAttrMap.containsKey(img_attr_with_text))
							elementAttrMap.put(img_attr_with_text, img_child_element.getAttribute(img_attr_with_text));
					}
				}
			}
		}catch(NoSuchElementException e){
			return;
		}

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
	//http://stackoverflow.com/questions/3454526/how-to-calculate-the-xpath-position-of-an-element-using-javascript
	private String getWebElementXpathById(WebElement element) throws WebDriverException{
		return (String) js.executeScript(
				
						"function getElementXpath(element){"+
							"if (element && element.id)"+
								"return '//*[@id=\"' + element.id + '\"]';"+
							"else {"+
								"var paths = [];"+
								"for (; element && element.nodeType == 1; element = element.parentNode){"+
									"var index = 0;"+
														            
						            "if (element && element.id) {"+
						                "paths.splice(0, 0, '/*[@id=\"' + element.id + '\"]');"+
						                "break;"+
						            "}"+
							
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
						"}"+
						"return getElementXpath(arguments[0]);",
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
        	log_buf.append("Exception durring getElementAttributes:"+e.getMessage()+"\n");
        	return elementAttrMap;
        }
    	return elementAttrMap;	        
	}


	private WebElement findElement(FindElementBy by, Object value) throws WebDriverException, NoSuchElementException,InvalidSelectorException{
		WebElement we = null;
		if(by.equals(FindElementBy.xpath))
			we = webDriver.findElement(By.xpath((String) value));
		else if(by.equals(FindElementBy.tagName))
			we = webDriver.findElement(By.tagName((String) value));
		else if(by.equals(FindElementBy.name))
			we = webDriver.findElement(By.name((String)value));
		else if(by.equals(FindElementBy.className))
			we = webDriver.findElement(By.className((String)value));
		else if(by.equals(FindElementBy.linkText))
			we = webDriver.findElement(By.linkText((String)value));
		else
			throw new InvalidSelectorException("Invalid method("+by+") to find an element");
		
		return we;
	}




}
