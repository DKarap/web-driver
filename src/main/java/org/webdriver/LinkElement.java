//package org.jobfinder.crawler.link;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.jobfinder.constants.Config;
//import org.jobfinder.constants.NavigatorContstants;
//import org.jobfinder.tokenizer.Token;
//import org.jobfinder.util.EqualsUtil;
//import org.jobfinder.util.HashCodeUtil;
//import org.jobfinder.util.Helper;
//import org.openqa.selenium.Point;
//
//
//
///**
// * HTMl Element that got href attributes 
// */
//public final class LinkElement implements Comparable<LinkElement>{
//	/*
//	 * keys are the pre defined attributes @see list: tagAttributesWithText
//	 * UPDATE: 
//	 * 1.we keep also all the attribute names of the link's parent element with KEY=parentAllAttributesNames
//	 * 2.the nodeName of the Parent node with KEY=parentNodeName
//	 * 3.the link element all attribute names with KEY=allAttributesNames
//	 */
//	private final Map<String,List<Token>> attributeNameTextMap;
//	//the xpath of the link element
//	private final String xpath;
//	//in case that the Element is frame, otherwise is equal to -1
//	private final int frameIndex; 
//	
//	
//	//If it is a  navigation link then we keep the whole text without tokenization and stemming
//	private final String navigationLinkString; 
//	//The Next Navigation Link Type: INTEGER | STRING | SYMBOL
//	private final int navigationLinkType;
//	
//	
//	//is Submit Link: we collect also INPUT<type=SUBMIT> links in case taht are 
//	private final boolean isSubmitLink;
//	
//	/*
//	 * The parent element we use it during link clustering
//	 */
//	//xpath of the link's parent node
//	private final String xpathOfParentNode;
//	
//	//Point of th eLink Location
//	private final LinkRenderedInfo linkRenderedInfo;
//	
//	//the untokenized anchor text of the link's childs separated by tab
//	private  final String anchorText;
//	
//	//the string identification of the Link; HREF of link
//	private  final String identification;
//	
//	
//	//the tokens of the frame element's(if it is) attributes... 
//	private final List<Token> frameAttrTokenList;
//	
//	//LINK context graph probability
//	private double linkGraphModelscore = 0.0;
//	
//	/**
//	 * The class of the linkElement; 1 if its possitive or 0 if it is negative
//	 */
//	private int classOfLink;
//	
//	private int fHashCode;
//	
//	
//	
//	/**
//	 * Constructor
//	 * @param URL - the url that the element links to!
//	 * @param attributeNameTextMap - A map with key the element's attributes NAME  and values their TEXT
//	 * (i.e. {URL}=>{http://www.a...}, {TEXT}=>{SOUTH}, ..., {ALT}=>{ASIA})
//	 */
//	public LinkElement(String anchorText,String identification,String xpath,Map<String,List<Token>> attributeNameTextMap,String navigationLinkString,boolean isSubmitLink,String xpathOfParentNode,LinkRenderedInfo linkRenderedInfo){
//		this.xpath = xpath;
//		this.attributeNameTextMap = attributeNameTextMap;
//		this.frameIndex = -1;
//		this.navigationLinkString = navigationLinkString;
//		this.navigationLinkType = getTypeOfNavigationString(this.navigationLinkString);
//		this.isSubmitLink = isSubmitLink;
//		this.xpathOfParentNode = xpathOfParentNode;
//		this.linkRenderedInfo = linkRenderedInfo;
//		
//		this.anchorText = anchorText;
//		this.frameAttrTokenList = null;
//		this.identification = identification;
//		this.classOfLink=-1;
//
//	}
//	
//	/*
//	 * ADD FRAME ELEMENT
//	 * (i)frame got no valuable text and they are accessed via indexes....
//	 * We use the src attribute of the element in order to use it as a equality field for the equal function!!! (distinguish the frames durring removePreviousCandidate Out links )
//	 * Now we use also the anchor tokens of each element in order to check for equality..we should save the frameAttrTokenList as anchor tokens to the attributeNameTextMap
//	 * 
//	 * @see ProcessPage.java 
//	 * TODO this must be clear it..we add values to variables that its not true;i.e. a frame dont have an Xpath..
//	 */
//	public LinkElement(List<Token> frameAttrTokenList,String srcAttribute,int frameIndex){
//		this.xpath = srcAttribute;
//		this.xpathOfParentNode = null;
//		
//		//TODO this probably is not in use`!!!
//		//save the frameAttrTokenList for use in order to compute a score for that
//		this.attributeNameTextMap = new HashMap<String,List<Token>>();
//		this.attributeNameTextMap.put("anchor", frameAttrTokenList);
//		
//		
//		this.navigationLinkString = null;
//		this.navigationLinkType = -1;
//		this.isSubmitLink = false;
//		this.frameIndex = frameIndex;
//		this.linkRenderedInfo = null;
//		
//		this.anchorText = null;
//		//this is the field to use for equality
//		this.identification = srcAttribute;
//		this.frameAttrTokenList = frameAttrTokenList;
//		this.classOfLink=-1;
//	}
//	
//	
//	/**
//	 * Get function
//	 */
//	public int getClassificationOfLink(){
//		return this.classOfLink;
//	}
//	
//	/**
//	 * @return the score
//	 */
//	public double getLinkGraphModelscore() {
//		return linkGraphModelscore;
//	}
//
//	/**
//	 * Get the link identification
//	 * @return
//	 */
//	public String getIdentification(){
//		return this.identification;
//	}
//	
//	/**
//	 * @param score the score to set
//	 */
//	public void setLinkGraphModelscore(double linkGraphModelscore) {
//		this.linkGraphModelscore = linkGraphModelscore;
//	}
//
//	public Map<String,List<Token>> getAttrNameToTextMap(){
//		return attributeNameTextMap;
//	}
//	
//	public String getNavigationLinkString(){
//		return this.navigationLinkString;
//	}
//	public int getNavigationLinkType(){
//		return this.navigationLinkType;
//	}
//	public boolean isSubmitLink(){
//		return this.isSubmitLink;
//	}
//	public String getXpathOfParent(){
//		return this.xpathOfParentNode;
//	}
//	
//	public List<Token> getFrameAttrTokenList(){
//		return this.frameAttrTokenList;
//	}
//	
//	public String getAnchor(){
//		return this.anchorText;
//	}
//
//	
//	/**
//	 * Rendered info of the linkElement
//	 *  1. Top left point of the link Element
//	 *  2. Width
//	 *  3. Height
//	 */
//	public Point getTopLeftPoint(){
//		return this.linkRenderedInfo.getTopLeftPoint();
//	}
//	public int getWidth(){
//		return this.linkRenderedInfo.getDimension().getWidth();
//	}
//	public int getHeight(){
//		return this.linkRenderedInfo.getDimension().getHeight();
//	}
//	public int getBottomYCoord(){
//		return this.linkRenderedInfo.getTopLeftPoint().getY() +  this.linkRenderedInfo.getDimension().getHeight();
//	}
//	public int getRightXCoord(){
//		return this.linkRenderedInfo.getTopLeftPoint().getX() +  this.linkRenderedInfo.getDimension().getWidth();
//	}
//	
//	/**
//	 * Set class of link
//	 */
//	public void setClassOfLink(int classOfLink){
//		this.classOfLink = classOfLink;
//	}
//	
//	/**
//	 * Return the type of the given Navigation String (@see NavigationLinkType.java)
//	 * TODO this is not accurate and depends from the order of appearance
//	 * @param navigationLinkString
//	 * @return int - the NavigationLinkType
//	 */
//	public int getTypeOfNavigationString(String navigationLinkString){
//		if(navigationLinkString == null)
//			return -1;
//		
//		if(navigationLinkString.matches("(.*\\s*|^)"+"[a-zA-Z]+?"+"(\\s*.*|$)") )
//			return NavigatorContstants.STRING;
//		else if(navigationLinkString.matches("\\s*\\d+?\\s*") )
//			return NavigatorContstants.INTEGER;
//		else if(navigationLinkString.matches("(.*\\s*|^)"+"\\W+?"+"(\\s*.*|$)") )
//			return NavigatorContstants.SYMBOL;
//		else{
//			System.out.println("\t\tUnknown Navigation:"+navigationLinkString);
//			return NavigatorContstants.UNKNOWN;
//		}
//	}
//	
//	/**
//	 * Check if the LINK is a Navigation one
//	 * @return true if got navigation text,otherwise false
//	 */
//	public boolean isNavigationLink(){
//		if(this.navigationLinkString == null)
//			return false;
//		else
//			return true;
//	}
//	
//	
//	
//	/**
//	 * 
//	 * @return boolean - true if link got anchor text,otherwise false
//	 */
//	public boolean gotAnchorText(){
//		if(this.attributeNameTextMap!=null && this.attributeNameTextMap.containsKey("anchor"))
//			return true;
//		else
//			return false;
//	}
//
//	/**
//	 * Get the number of anchor tokens
//	 * @return number of anchor tokens - int
//	 */
//	public int getNumberOfAnchorTokens(){
//		if(this.attributeNameTextMap!=null && this.attributeNameTextMap.containsKey("anchor"))
//			return this.attributeNameTextMap.get("anchor").size();
//		else
//			return 0;
//	}
//	
//	/**
//	 * 
//	 * @param featureName
//	 * @return the value of the given Feature
//	 */
//	public List<Token> getFeatureTokens(String featureName){
//		if(this.attributeNameTextMap!=null && this.attributeNameTextMap.containsKey(featureName))
//			return this.attributeNameTextMap.get(featureName);
//		else
//			return null;
//	}
//	
//	/**
//	 * If the link is frame then return true
//	 * 	we check the frame index for that,,
//	 * @return
//	 */
//	public boolean isFrame(){
//		if(this.frameIndex == -1)
//			return false;
//		else
//			return true;
//	}
//	
//	/** 
//	 * @return Frame index
//	 */
//	public int getFrameIndex(){
//		return this.frameIndex;
//	}
//	
//	/** 
//	 * @return XPath text
//	 */
//	public String getXPath(){
//		return this.xpath;
//	}
//	
//	/** 
//	 * @return Link's token list
//	 */
//	public List<Token> getAnchorTokens(){
//		if(attributeNameTextMap!=null)
//			return attributeNameTextMap.get("anchor");
//		else
//			return null;
//	}
//	
//	/** 
//	 * @return Link's href
//	 */
//	public List<Token> getHrefTokens(){
//		return attributeNameTextMap.get("href");
////			return attributeNameTextMap.get("src");
//	}
//	
//	
//	/**
//	 * override toString the default toString display the detail link Model features
//	 */
//	public String toStringDetailLinkModelFeatures(){
//		StringBuilder buf = new StringBuilder();
//		//we save different featuyres than we use for classification
////		for(String linkClassifierFeature:Config.detailLinkModelFeaturesForClassifier){
//		for(String linkClassifierFeature:Config.detailLinkModelFeaturestoSaveInDB){
//			List<Token> featureTokens = getFeatureTokens(linkClassifierFeature);
//			if(featureTokens!=null && !featureTokens.isEmpty()){
//				buf.append(linkClassifierFeature+":");
//				String tokenCodes = Helper.joinTokens(featureTokens);
//				buf.append(tokenCodes+ " ");
//			}
//		}
//		return buf.toString().trim();
//	}
//	
//	
//	/**
//	 * override toString the default toString display the link Graph Model features
//	 * @seeHelper.getLinkElementListFromInitialPageToFormPageAsString()
//	 */
//	public String toLinkModelFeatures(){
//		StringBuilder buf = new StringBuilder();
//		//we save different featuyres than we use for classification
//		for(String linkClassifierFeature:Config.linkModelFeaturestoSaveInDB){
////		for(String linkClassifierFeature:Config.linkModelFeaturesForClassifier){
//			List<Token> featureTokens = getFeatureTokens(linkClassifierFeature);
//			if(featureTokens!=null && !featureTokens.isEmpty()){
//				buf.append(linkClassifierFeature+":");
//				String tokenCodes = Helper.joinTokens(featureTokens);
//				buf.append(tokenCodes+ " ");
//			}
//		}
//		return buf.toString().trim();
//	}
//	
////	@Override
////	public String toString(){
////		StringBuilder buf = new StringBuilder();
////		//we save different featuyres than we use for classification
////		for(String linkClassifierFeature:Config.linkModelFeaturestoSaveInDB){
//////		for(String linkClassifierFeature:Config.linkModelFeaturesForClassifier){
////			List<Token> featureTokens = getFeatureTokens(linkClassifierFeature);
////			if(featureTokens!=null && !featureTokens.isEmpty()){
////				buf.append(linkClassifierFeature+":");
////				String tokenCodes = Helper.joinTokens(featureTokens);
////				buf.append(tokenCodes+ " ");
////			}
////		}
////		return buf.toString().trim();
////	}
//		
//	@Override
//	 public boolean equals(Object other) {
//		
//		if (this == other)
//			return true;    
//		if (!(other instanceof LinkElement))
//			return false;
//		LinkElement otherState = (LinkElement) other;
//		return   EqualsUtil.areEqual(this.getIdentification(),otherState.getIdentification());//EqualsUtil.areEqual(this.xpath,otherState.xpath) && EqualsUtil.areEqual(this.getAnchorTokens(),otherState.getAnchorTokens());
//	}
//	
//	@Override 
//	public int hashCode() {
//		//this style of lazy initialization is 
//	    //suitable only if the object is immutable
//	    if ( fHashCode == 0) {
//	      int result = HashCodeUtil.SEED;
//	      result = HashCodeUtil.hash( result, this.identification );
////	      result = HashCodeUtil.hash( result, this.xpath );
////	      result = HashCodeUtil.hash( result, this.getAnchorTokens() );
//	      fHashCode = result;
//	    }
//	    return fHashCode;
//	}
//	
//	@Override 
//	public int compareTo(LinkElement linkElement) {
//		
//		/*
//		 * if belongs to Possitive Job Title model..
//		 */
//		if(getLinkGraphModelscore() > linkElement.getLinkGraphModelscore() )
//			return -1;
//		else if(getLinkGraphModelscore() < linkElement.getLinkGraphModelscore() )
//			return 1;
//		else 
//			return 0;
//	}
//}
