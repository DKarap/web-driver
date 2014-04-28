package org.webdriver.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class Helper {

	
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

}
