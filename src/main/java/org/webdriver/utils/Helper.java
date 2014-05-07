package org.webdriver.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class Helper {


	public static String getAbsoluteXpathFromWebElement0(WebElement webElement, JavascriptExecutor js){
		String xpath =  (String) js.executeScript(XPATH_FIREBUG_COMPUTATION_JAVASCRIPT, webElement);
		return xpath;
	}
	private static final String XPATH_FIREBUG_COMPUTATION_JAVASCRIPT  =
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
							"return getXPathExpression(arguments[0]);";

	
	
	
	/**
	 * 											VERSION 1
	 * @param webElement
	 * @param js
	 * @return
	 */
	public static String getAbsoluteXpathFromWebElement1(WebElement webElement, JavascriptExecutor js){
		String xpath =  (String) js.executeScript(XPATH_COMPUTATION_JAVASCRIPT, webElement);
		return xpath;
	}
	private static final String XPATH_COMPUTATION_JAVASCRIPT = 
										"function getXPathExpression(elementNode){"+
											"var parentNode = elementNode.parentNode;"+
											"if(!parentNode || parentNode.nodeName.indexOf(\"#document\") !== -1){"+
												"return \"/\" + elementNode.nodeName + \"[1]\";"+
											"}"+
    
											"var xpath = '';"+
											"if (parentNode !== elementNode) {"+
												"xpath += getXPathExpression(parentNode) + \"/\";"+
											"}"+
	
											"xpath += parentNode.nodeName;"+
											"var siblingsArray = getSiblings(parentNode, elementNode);"+
											"for (i=0; i<siblingsArray.length; i++){"+
												"var el = siblingsArray[i];"+
												"if (el === elementNode) {"+
													"xpath += \"[\" + (i + 1) + \"]\";"+
													"break;"+
												"}"+
											"}"+
											"return xpath;"+
										"}"+

										"function getSiblings(parentNode, elementNode){"+
											"var array = [];"+
											"var	childList = parentNode.childNodes;"+
											"for (i=0; i<childList.length; i++){"+
												"if(elementNode.nodeName === childList[i].nodeName){"+
													"array.push(childList[i]);"+
												"}"+
											"}"+
											"return array;"+
										"}"+
										"return getXPathExpression(arguments[0]);";

	
	/**
	 * 											VERSION 2
	 * @param webElement
	 * @param js
	 * @return
	 */
    public static String getAbsoluteXpathFromWebElement2(WebElement webElement, JavascriptExecutor js) { 
        String jscript = "function getPathTo(node) {" + 
            "  var stack = [];" + 
            "  while(node.parentNode !== null) {" + 
            "    stack.unshift(node.tagName);" + 
            "    node = node.parentNode;" + 
            "  }" + 
            "  return stack.join('/');" + 
            "}" + 
            "return getPathTo(arguments[0]);"; 
        return (String) js.executeScript(jscript, webElement); 
    } 
    
    
    
	public static String getAbsoluteXpathFromWebElement3(WebElement element, JavascriptExecutor js) throws WebDriverException{
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
