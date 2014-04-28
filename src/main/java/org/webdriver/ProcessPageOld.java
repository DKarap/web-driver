//package org.jobfinder.crawler.page;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.xpath.XPathExpressionException;
//
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.w3c.dom.Attr;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
///**
// * 
// * TODO 4-1.getPageLinksFromDOM - Extracts Links that don't have HREF but their
// * childred GOT!!!! Here probably we check for example tag 'a' which dont have
// * href and we skip them but these tags got ancestor with tags 'a' which include
// * href
// * 
// * @author mimis
// * 
// */
//
//public final class ProcessPageOld {
//
//	
//	
//		
//	
//	public List<LinkElement> getPageLinksFromDOM(Document dom, WebDriver driver)throws SAXException, IOException, ParserConfigurationException {
//
//		
//		// List to save the Extracted Links from the current DOM
//		List<LinkElement> results = new ArrayList<LinkElement>();
//
//		// for each element tag that probably got URL
//		for (String tagWithLink : PageConstants.tagsWithLinks) {
//
//			NodeList nodeList = dom.getElementsByTagName(tagWithLink);
//
//			int frameIndex = 0;
//			int iframeIndex = 0;
//			for (int k = 0; k < nodeList.getLength(); k++) {
//				Element element = (Element) nodeList.item(k);
//				// this should change in case that other html elements got SRC and are links
//				String URL = element.getAttribute("href");
//				String SRC = element.getAttribute("src").toLowerCase();
//				String ONCLICK = element.getAttribute("onclick");
//				/*
//				 * If it is frame then add its index to the result list 
//				 * we treat Iframes as new states ..@see robot.java
//				 */
//				String elementXPath = XPathHelper.getXPathExpression(element);
//				
////				String elementXPath = XPathHelper.getXPathExpressionFirebug(element);
////				String elementXPath = XPathHelper.getDetailedXPathExpression(element, true, true);
////				System.out.println("Link:"+element.getTextContent().trim()+"\txpath:"+elementXPath+"\tag:"+element.getNodeName());
//				if(isLinkValid(element)){
//
//					// Save (i)Frame details
//					if (element.getTagName().toLowerCase().equals("iframe"))
//						results.add(getFrameElementDetails(element, SRC, iframeIndex));
//					
//					else if (element.getTagName().toLowerCase().equals("frame"))
//						results.add(getFrameElementDetails(element, SRC, frameIndex));
//					
//					else if (!ONCLICK.equals("") && ONCLICK != null || !URL.equals("") && URL != null  || Helper.isSubmitElement(element)){
//						LinkRenderedInfo linkRenderedInfo = Helper.ifIsVisibleGetRenederedInfo(driver, elementXPath);
//						if(linkRenderedInfo!=null)
//							results.add(getDomElementDetails(element, elementXPath,linkRenderedInfo)); // Save webElements details
//					}
//					else if (!SRC.equals("") && SRC != null ){
//						LinkRenderedInfo linkRenderedInfo =  Helper.ifIsVisibleGetRenederedInfo(driver, elementXPath);
//						if(linkRenderedInfo!=null)
//							results.add(getDomElementDetails(element, elementXPath,linkRenderedInfo)); // Save webElements details
//					}
//				}
//				
//				//Increase the (i)frames indexes 
//				if (element.getTagName().toLowerCase().equals("iframe"))
//					iframeIndex++;
//				else if (element.getTagName().toLowerCase().equals("frame"))
//					frameIndex++;	
//			}
//		}
//		
//		return results;
//	}
//	
//	
//	public LinkElement getFrameElementDetails(Element frameElement,String src,int frameIndex){
//		
//		// Save all element's attributes if not null
//		StringBuilder allAttrText = new StringBuilder();
//		for (String tagAttr : PageConstants.frameTagAttributesWithText) {
//			String attrValue = frameElement.getAttribute(tagAttr);
//			allAttrText.append(attrValue+" ");
//		}
//		
//		
//		//List<Token> frameHreftokenList = tokenizer.TokenizeUsingTokenCriteria(frameElement.getAttribute("src"));
//		
//		//tokenize the attr text
//		List<Token> frameAttrtokenList = tokenizer.TokenizeUsingTokenCriteria(allAttrText.toString());
//		
//		//at the end return the LinkElement
//		return new LinkElement(frameAttrtokenList,src,frameIndex);
//	}
//
//
//
//	public Page getPage(Document dom,WebDriver driver,int pageId,String pageSource) throws SAXException, IOException,ParserConfigurationException, InterruptedException {
//		
//		
//		// Map to save the page features
//		Map<String, List<Token>> featureNameToTextMap = new HashMap<String, List<Token>>();
//
//		// 1. Extract all the pre-defined features of the given page dom into the given MAP!
//		extractPageFeaturesToMap(driver.getCurrentUrl(),dom, featureNameToTextMap);
//		
//		/*
//		 * 2. Get the TokenSequence of the page with all the Tokens - this is used for the extraction of the page template
//		 * This tkenSequence got wrapperTokens where we save the String of token together with their Type!!!
//		 */
//		//TokenSequence allPageTokens = this.tokenizer.getHtmlPageTokenSequence(pageSource);
//		
//		//3. Extract Out-Links from the page
//		List<LinkElement> outLinksList = getPageLinksFromDOM(dom, driver);
//		
//		
//		//5. return map with the features
//		return new Page(featureNameToTextMap,outLinksList,pageId,Helper.getDomHashCodeAsHex(pageSource),driver);
//	}
//
//	
//
//	/**
//	 * Extract all the pre-defined features of the given page dom into the given MAP! 
//	 * We get the TEXT from the title , body and meta part of the Page!!!
//	 * @param doc - document of the page
//	 * @param featureNameToTextMap
//	 * @param CompanyName
//	 */
//	private void extractPageFeaturesToMap(String url,Document doc, Map<String, List<Token>> featureNameToTextMap) {
//		
//		//url feature
//		List<Token> urlTokens = tokenizer.TokenizeUsingTokenCriteria(url);
//		featureNameToTextMap.put("urlTokens",urlTokens);
//
//		
//		// for each element tag that probably got TEXT that we want to keep -
//		// TITLE - BODY - META DESCRIPTION - META KEYWORDS
//		for (String tagWithText : PageConstants.pageTagsWithText) {
//			// TITLE - META - BODY
//			NodeList nodeList = doc.getElementsByTagName(tagWithText);
//
//			for (int k = 0; k < nodeList.getLength(); k++) {
//				Element element = (Element) nodeList.item(k);
//
//				// Check if the tag has one of the specified tagAttrNames -
//				// meta<attr or descri> TAG_NAME{KEYWORDS}{DESCRIPTION}
//				for (String tagAttrName : PageConstants.pageTagsAttrName) {
//					if (tagAttrName.equals(element.getAttribute("name").toLowerCase())) {
//						for (String tagAttrWithText : PageConstants.pageTagsAttrWithText) {
//							String text = element.getAttribute(tagAttrWithText);
//							List<Token> featureTokens = tokenizer.TokenizeUsingTokenCriteria(text);
//							
//							/*
//							 *  Save feature tokens - 
//							 *  FRAME CASE:IN CASE THAT EXIST TWO FEATURES IN THIS HTML WITH THE SAME NAME THEN APPEND
//							 */
//							if (!featureTokens.isEmpty())
//								if (featureNameToTextMap.containsKey(tagAttrName)) {
//									List<Token> previousTokens = featureNameToTextMap.get(tagAttrName);
//									previousTokens.addAll(featureTokens);
//								} else
//									featureNameToTextMap.put(tagAttrName,featureTokens);
//						}
//					}
//				}
//				//append title tag with the h1 and meta tags text
//				if(tagWithText.equals("meta"))
//					tagWithText = "title";
//				// CHECK IF GOT TEXT IN CASE THAT IS TITLE OR DESCRIPTION and 
//				// ADD IT
//				String elementText = element.getTextContent();
//				List<Token> elementTokens = tokenizer.TokenizeUsingTokenCriteria(elementText);
//				// check if its not null before to add it
//				if (!elementTokens.isEmpty()) {
//					// Save feature tokens - IN CASE THAT EXIST TWO FEATURES IN
//					// THIS HTML WITH THE SAME NAME THEN APPEND
//					if (featureNameToTextMap.containsKey(tagWithText)) {
//						List<Token> previousTokens = featureNameToTextMap.get(tagWithText);
//						previousTokens.addAll(elementTokens);
//					} else
//						featureNameToTextMap.put(tagWithText, elementTokens);
//				}
//			}
//		}
//
//	}
//
//	/**
//	 * Check if the given element is a valid one in order to be a Link
//	 * @param element
//	 * @return true if the element is a valid one to be a Navigation Link
//	 */
//	private boolean linkGotValidTypeIn(Element element){
//		if(element.getNodeName().toLowerCase().equals("input")){
//			if(element.hasAttribute("type")){
//				String attrValue = element.getAttribute("type").toLowerCase();
//				if(NavigatorContstants.invlalidTypeOfLinks.contains(attrValue))
//					return false;
//			}
//		}
//		return true;
//	}
//	
//
//	/**
//	 * We check here if the link is valid - Skip href that are: 
//	 * 1)Email - includes the string "mailto:" 
//	 * 2)CSS file - ends with ".css" 
//	 * 3)PDF file - ends with ".pdf" 
//	 * 4)Links that open pages for print
//	 * 5)InValide  links: produce an alert box in firefox which block webdriver
//	 * 	i.e.:	 <a href="http://">here</a>.</p><hr>
//	 * 
//	 * @param Href
//	 * @return true if it is valid or false otherwise
//	 */
//	public boolean isLinkValid(Element element) {
//
//		//if is INOUT and ype CHeckBoz then is not vaild
//		if(!linkGotValidTypeIn(element)){
//			return false;
//		}
//		
//		NamedNodeMap attrs = element.getAttributes();
//		for (int i = 0; i < attrs.getLength(); i++) {
//			Attr attribute = (Attr) attrs.item(i);
//			for (String filterT : LinkConstants.invalidElementTokens) 
//				if (attribute.getValue().toLowerCase().contains(filterT)){
//					//System.out.println(element.getTextContent()+"\tinvalid text:"+filterT);
//					return false;
//				}
//		}
//		String text = element.getTextContent();
//		// check text is invalid
//		for (String filterT : LinkConstants.invalidElementTokens){
//			if (text.toLowerCase().contains(filterT)){
//				//System.out.println(element.getTextContent()+"\tinvalid text:"+filterT);
//				return false;
//			}
//		}
//		// if Element doesnt have the invalid tokens then is OK
//		return true;
//	}
//
//	
//
//
//	
//	/**
//	 * TODO REFACTORING
//	 * Get WebElement info details (Attributes,Text,Xpath) based on the predefined list  and add them to LinkElement
//	 * if the link dom element got an img child element get its alt attr text and save it as "imgText" key and append it to anchoe text
//	 * @param DOM
//	 *            Element
//	 * @param xpath
//	 *            of element
//	 * @param Point - of the element
//	 * @return LinkElement
//	 *
//	 */
//	public LinkElement getDomElementDetails(Element element,String elementXPath,LinkRenderedInfo linkRenderedInfo) {
//		
//		
//		//Map where we save as key the attribute name and value its tokenized text
//		Map<String, List<Token>> attributeNameTextMap = new HashMap<String, List<Token>>();
//		//List where save each attribute text unTokenized - we use it for the NavigationString Detection function..
//		List<String> unTokenizedLinkList4NavigationLinkDetection= new ArrayList<String>();
//		
//		
//		/*
//		 *  ANCHOR TEXT:Save element's Text (if not null) and also the img child alt text
//		 *  TODO this returns the text content of the childs of this element without space: we should correct it
//		 */
//		String elementText = element.getTextContent();
//		List<Token> textTokens = tokenizer.TokenizeUsingTokenCriteria(elementText);
//		//save unTokenized text
//		unTokenizedLinkList4NavigationLinkDetection.add(elementText.trim());
//		
//		
//		
//		/*
//		 * Save Elements NodeName! i.e <a>,<input>....
//		 */
//		List<Token> nodeNameTokens = tokenizer.TokenizeAll(element.getNodeName());
//		attributeNameTextMap.put("nodeName", nodeNameTokens);
//
//		// Save all element's attributes if not null
//		for (String tagAttr : PageConstants.tagAttributesWithText) {
//			String attrValue = element.getAttribute(tagAttr);
//			
//			List<Token> attrValueTokens = tokenizer.TokenizeUsingTokenCriteria(attrValue);
//			//save unTokenized text except of the 'id' attr
//			if(!tagAttr.equals("id"))
//				unTokenizedLinkList4NavigationLinkDetection.add(attrValue.trim());
//			// add feature tokens
//			if (!attrValueTokens.isEmpty())
//				attributeNameTextMap.put(tagAttr, attrValueTokens);
//		}
//		
//		/*
//		 * Image Links: for every link element we check if got image childs and if yes we save its ALT attribute text into the Map
//		 */
//		String imgAltText = null;
//		if(element.hasChildNodes()){
//    		NodeList imageChild = element.getElementsByTagName("img");
//    	    for (int y=0; y<imageChild.getLength(); y++) {
//    	    	Element currentImgElement = (Element) imageChild.item(y);
//    	    	if(currentImgElement.hasAttribute("alt")  && !currentImgElement.getAttribute("alt").trim().isEmpty()){
//    	    		imgAltText = currentImgElement.getAttribute("alt").trim();
//    	    		List<Token> tokenList = tokenizer.TokenizeUsingTokenCriteria(imgAltText);
//    	    		if(!tokenList.isEmpty())
//    	    			attributeNameTextMap.put("imgText", tokenList);
//    	    		//save unTokenized text
//    	    		unTokenizedLinkList4NavigationLinkDetection.add(imgAltText);
//    	    	}
//    	    	else if(currentImgElement.hasAttribute("value") && !currentImgElement.getAttribute("value").trim().isEmpty()){
//    	    		imgAltText = currentImgElement.getAttribute("value").trim();
//    	    		List<Token> tokenList = tokenizer.TokenizeUsingTokenCriteria(imgAltText);
//    	    		if(!tokenList.isEmpty())
//    	    			attributeNameTextMap.put("imgText", tokenList);
//    	    		//save unTokenized text
//    	    		unTokenizedLinkList4NavigationLinkDetection.add(imgAltText);
//    	    	}
//    	    	else if(currentImgElement.hasAttribute("title") && !currentImgElement.getAttribute("title").trim().isEmpty()){
//    	    		imgAltText = currentImgElement.getAttribute("title").trim();
//    	    		List<Token> tokenList = tokenizer.TokenizeUsingTokenCriteria(imgAltText);
//    	    		if(!tokenList.isEmpty())
//    	    			attributeNameTextMap.put("imgText", tokenList);
//    	    		//save unTokenized text
//    	    		unTokenizedLinkList4NavigationLinkDetection.add(imgAltText);
//    	    	}
//    	    	else if(currentImgElement.hasAttribute("src")){
//    	    		imgAltText = currentImgElement.getAttribute("src").trim();
//    	    		List<Token> tokenList = tokenizer.TokenizeUsingTokenCriteria(imgAltText);
//    	    		if(!tokenList.isEmpty())
//    	    			attributeNameTextMap.put("imgText", tokenList);
//    	    		//save unTokenized text
//    	    		unTokenizedLinkList4NavigationLinkDetection.add(imgAltText);
//    	    	}
//    	    }
//    	    //see http://www.macquarie.com.au/mgl/au/about-macquarie-group/careers/working/privacy
//    	    NodeList insChild = element.getElementsByTagName("ins");
//    	    for (int y=0; y<insChild.getLength(); y++) {
//    	    	Element currentInsElement = (Element) insChild.item(y);
//    	    	String insText = currentInsElement.getTextContent();
//	    		List<Token> tokenList = tokenizer.TokenizeUsingTokenCriteria(insText);
//	    		if(!tokenList.isEmpty()){
//	    			attributeNameTextMap.put("insText", tokenList);
//	    			unTokenizedLinkList4NavigationLinkDetection.add(insText);
//	    		}
//    	    }
//    	}
//		
//		//Concat also the img alt text if exist TO THE ANCHOR TEXT
//		if(attributeNameTextMap.containsKey("imgText"))
//			textTokens.addAll(attributeNameTextMap.get("imgText"));
//		if(attributeNameTextMap.containsKey("insText"))
//			textTokens.addAll(attributeNameTextMap.get("insText"));
//		if(!textTokens.isEmpty())
//			attributeNameTextMap.put("anchor", textTokens);
//		
//		/*
//		 * Get Element's first child text! this is be done because we want the first Link's text, which normaly is the job title
//		 * if the anchor is empty then save as  anchor the img text
//		 */
//		String anchorText = Helper.getElementAndChildsTextAsString(element);
//		if(anchorText.isEmpty() && imgAltText!=null)
//			anchorText = imgAltText;
//		
//		
//		/*
//		 * Get the identification in order to use for equality for the links
//		 * TODO this must be done for each tag separatelly!!!now we use only the href or the whole attr values
//		 */
//		String identification = Helper.getElementIdentification(element);
//		
//		/*
//		 * save the tokesn of all the attributes of the element
//		 */
//		attributeNameTextMap.put("allAttr",tokenizer.TokenizeUsingTokenCriteria(Helper.getElementAttributesValues(element)));
//		
//		/*
//		 * NAVIGATION TEXT
//		 * if the link is a valid one to be a navigation link then
//		 * We check the text of the link if is a NEXT or PREVIOUS Navigation Text and if YEs then save it to the Link Element( A text link is a navigation one if got navigation text)
//		 * 
//		 */
//		String navigationString = getNavigationString(unTokenizedLinkList4NavigationLinkDetection);			
//		
//		
//		
//
//		/*
//		 * At the end we save all the attributes of the element and its parent together with parent's nodename and xpath 
//		 * 	..this is for link clustering
//		 * 1.we keep also all the attribute names of the link's parent element with KEY=parentAllAttributesNames
//		 * 2.the nodeName of the Parent node with KEY=parentNodeName
//		 * 3.the link element all attribute names with KEY=allAttributesNames
//		 */
//		Element parentElement = (Element)element.getParentNode();
//		String elementAllAttrNames = Helper.getAllElementAttributes(element);
//		String elementParentAttrNames = Helper.getAllElementAttributes(parentElement);
//		
//		attributeNameTextMap.put("allAttributesNames", tokenizer.TokenizeAll(elementAllAttrNames));
//		attributeNameTextMap.put("parentAllAttributesNames", tokenizer.TokenizeAll(elementParentAttrNames));
//		attributeNameTextMap.put("parentNodeName", tokenizer.TokenizeAll(parentElement.getNodeName()));
//		String elementParentXPath = XPathHelper.getXPathExpression(element.getParentNode());
//		
//		
//		//at the end return the LinkElement
//		return new LinkElement(anchorText.toLowerCase(),identification,elementXPath, attributeNameTextMap,navigationString,Helper.isSubmitElement(element),elementParentXPath,linkRenderedInfo);
//	}
//	
//	
//	
//	/**
//	 * Return the  Navigation Text of the link
//	 *  
//	 * A text link is a navigation one, if:
//	 * @ref Automatic generation of agents for collecting hidden Web pages for data extraction
//	 *  Heuristic 5. From all available text links, we look for those with at most two words, one of them being ‘‘next’’ or ‘‘more’’ or 
//	 *  links with no words containing only one or more Ô>Õ symbols. Then, when the link is found, we follow it and check whether the new 
//	 *  page is a data-rich one. If we did the right choice, a pattern expression to this link is kept in the agent. When the agent is 
//	 *  running, pages are collected until no more links that match the expression are found.
//	 *  
//	 *  Notice that when the link is an image, we use the text of the <ALT> HTML tag, when available.
//	 *  
//	 *  
//	 *  
//	 * 	 Here we allowed relative tokens to exist  inside words but there are nav links like:i.e cmdPrev OR cmdNext
//	 *       this produce problems because finds a lot of candidate links for navigation which are not 
//	 *   @seeTodo 191    
//	 * 	
//	 * @param linkTextList - List<String> of text that exist in every attr or image alt attr or in its text
//	 * @return The Navigation String if it is exist, otherwise null
//	 */
//	public String getNavigationString(List<String> textLinkList){
//		for(String textLink : textLinkList){
//			//if the text is not empty and got no more that N tokens then check if got navigation text
//			if(!textLink.isEmpty()){
//				textLink = textLink.toLowerCase().trim();
//
//				 //IGNORE CASE...for social links..
//				if(NavigatorContstants.nextInvvalidNavigationLinksText.contains(textLink))
//					continue;
//				
//				
//				String[] tokenizedLinkText = textLink.split("\\s+");
//				
//			
//				//if the text feature(attribute value or elements text) is at most FIVE words
//				if(tokenizedLinkText.length < NavigatorContstants.maxLengthOfNavigationString){
//					/*
//					 * Digit NAvigation String
//					 * i.e. "1","2", 
//					 * special case: "1-2"
//					 * special case: "1 to 2"
//					 */
//					if(textLink.matches("\\s*\\d+?\\s{0,1}((\\W{0,1}|to|tot|tot en met){1}\\s{0,1}\\d+?){0,1}\\s*"))
//						return textLink;
//					
//					
//
//					/*
//					 * Next Navigation Strings
//					 * 
//					 */
//					for(String relToken: NavigatorContstants.nextNavigationLinksText)						
//	//					if(textLink.matches("(^|.*\\s+?|\\w+?)"+relToken+"($|\\s+?.*$|\\W*$|\\w+?$)") ){
//						if(textLink.matches("(^|.*\\s+?|\\w+?|.*\\w+?|.*\\W*)"+relToken+"($|\\s+?.*$|\\W*$|\\w+?$|\\W*\\w*$)") ){
//							return textLink;
//						}
//					
//						
//					
//					for(String relToken: NavigatorContstants.previousNavigationLinksText)
////						if(textLink.matches("(.*\\s+?|^|^\\W*)"+relToken+"(\\s+?.*|$)") )
//						if(textLink.matches("(.*\\w+?|.*\\s+?|^|^\\W*)"+relToken+"(\\s+?.*|$)") )
//							return textLink;
//
//				}
//				//else if the text is bigger that we allow then check if exist a special big text case like the abn amro..
//				else{
//					/*
//					 * Digit NAvigation String
//					 * i.e. "1","2", 
//					 * special case: "1 tot en met 2"
//					 */
//					if(textLink.matches("\\s*\\d+?\\s{0,1}((\\W{0,1}|to|tot|tot en met){1}\\s{0,1}\\d+?){0,1}\\s*"))
//						return textLink;
//				
//				}
//			}
//		}
//		return null;
//	}
//
//
//}
