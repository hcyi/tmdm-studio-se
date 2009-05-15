package com.amalto.webapp.v3.itemsbrowser.dwr;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.security.jacc.PolicyContextException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringEscapeUtils;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amalto.webapp.core.bean.Configuration;
import com.amalto.webapp.core.bean.UpdateReportItem;
import com.amalto.webapp.core.dwr.CommonDWR;
import com.amalto.webapp.core.util.Util;
import com.amalto.webapp.core.util.XtentisWebappException;
import com.amalto.webapp.util.webservices.WSConceptKey;
import com.amalto.webapp.util.webservices.WSCount;
import com.amalto.webapp.util.webservices.WSDataClusterPK;
import com.amalto.webapp.util.webservices.WSDataModelPK;
import com.amalto.webapp.util.webservices.WSDeleteItem;
import com.amalto.webapp.util.webservices.WSExistsItem;
import com.amalto.webapp.util.webservices.WSGetBusinessConceptKey;
import com.amalto.webapp.util.webservices.WSGetBusinessConcepts;
import com.amalto.webapp.util.webservices.WSGetItem;
import com.amalto.webapp.util.webservices.WSGetTransformerPKs;
import com.amalto.webapp.util.webservices.WSGetView;
import com.amalto.webapp.util.webservices.WSGetViewPKs;
import com.amalto.webapp.util.webservices.WSItem;
import com.amalto.webapp.util.webservices.WSItemPK;
import com.amalto.webapp.util.webservices.WSPutItem;
import com.amalto.webapp.util.webservices.WSRouteItemV2;
import com.amalto.webapp.util.webservices.WSStringArray;
import com.amalto.webapp.util.webservices.WSStringPredicate;
import com.amalto.webapp.util.webservices.WSTransformerPK;
import com.amalto.webapp.util.webservices.WSView;
import com.amalto.webapp.util.webservices.WSViewPK;
import com.amalto.webapp.util.webservices.WSWhereAnd;
import com.amalto.webapp.util.webservices.WSWhereCondition;
import com.amalto.webapp.util.webservices.WSWhereItem;
import com.amalto.webapp.util.webservices.WSWhereOperator;
import com.amalto.webapp.util.webservices.WSXPathsSearch;
import com.amalto.webapp.v3.itemsbrowser.bean.Restriction;
import com.amalto.webapp.v3.itemsbrowser.bean.TreeNode;
import com.amalto.webapp.v3.itemsbrowser.bean.View;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSParticle;

/**cluster
 * 
 * 
 * @author asaintguilhem
 *
 */

public class ItemsBrowserDWR {

	public ItemsBrowserDWR() {
		super();
	}
	
	
	/**
	 * return a list of "browse items" views
	 * @param language
	 * @return a map name->description
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<String,String> getViewsList(String language) throws RemoteException, Exception{
		Configuration config = Configuration.getInstance();
		String model = config.getModel();
		String [] businessConcept = Util.getPort().	getBusinessConcepts(
					new WSGetBusinessConcepts(
							new WSDataModelPK(model)
						)
					).getStrings();
		ArrayList<String> bc = new ArrayList<String>();
		for (int i = 0; i < businessConcept.length; i++) {
			bc.add(businessConcept[i]);
		}
		WSViewPK[] wsViewsPK = Util.getPort().getViewPKs(new WSGetViewPKs("Browse_items.*")).getWsViewPK();
		String[] names = new String[wsViewsPK.length];
		TreeMap<String,String> views = new TreeMap<String,String>();
		Pattern p = Pattern.compile(".*\\["+language.toUpperCase()+":(.*?)\\].*",Pattern.DOTALL);
		for (int i = 0; i < wsViewsPK.length; i++) {
			WSView wsview = Util.getPort().getView(new WSGetView(wsViewsPK[i]));
			String concept = wsview.getName().replaceAll("Browse_items_","").replaceAll("#.*","");
			names[i] = wsViewsPK[i].getPk();
			if(		//wsviews[i].getWsDataClusterPK().getPk().equals(cluster) 
					//&& wsviews[i].getWsDataModelPK().getPk().equals(model) && 
					bc.contains(concept)
					){
				
				views.put(wsview.getName(),p.matcher(wsview.getDescription()).replaceAll("$1"));
			}
		}	
		return CommonDWR.getMapSortedByValue(views);
	}
	
	public View getView(String viewPK, String language){
		try {
			WebContext ctx = WebContextFactory.get();
			String concept =  CommonDWR.getConceptFromBrowseItemView(viewPK);
			Configuration config = Configuration.getInstance();
			String model = config.getModel();
			View view = new View(viewPK, language);
			WSConceptKey key = Util.getPort().getBusinessConceptKey(
					new WSGetBusinessConceptKey(
							new WSDataModelPK(model),
							concept));
			String[] keys = key.getFields();
			for (int i = 0; i < keys.length; i++) {
				if(".".equals(key.getSelector()))
					keys[i] = "/"+concept+"/"+keys[i];					
				else
					keys[i] = key.getSelector()+keys[i];
			}
			view.setKeys(key.getFields());
			ctx.getSession().setAttribute("foreignKeys",key.getFields());
			return view;
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * return a list of searchable elements of a browse items list
	 * @param viewPK
	 * @param language
	 * @return a map xpath->label
	 */
	/* public HashMap<String,String> getSearchables(String viewPK,String language){
		try{
			String[] searchables = new View(viewPK,language).getSearchables();
			HashMap<String,String> labelSearchables = new HashMap<String,String>();
			HashMap<String,String> xpathToLabel = CommonDWR.getFieldsByBrowseItemsView(viewPK,language,true);	
			Configuration config = Configuration.getInstance();
			String concept = CommonDWR.getConceptFromBrowseItemView( viewPK);
			xpathToLabel.put(concept,CommonDWR.getConceptLabel(config.getModel(),concept,language));
			for (int i = 0; i < searchables.length; i++) {
				labelSearchables.put(searchables[i],xpathToLabel.get(searchables[i]));
			}
			return labelSearchables.put;			
		}
		catch(Exception e){
			return null;
		}
	}*/
	
	/**
	 * return a list of viewable elements o a browse items list
	 * used for column header of a grid
	 * @param viewPK
	 * @param language
	 * @return an array of label
	 */
	
	public String[] getViewables(String viewPK, String language){		
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().setAttribute("viewNameItems",null);
		try {
			Configuration config = Configuration.getInstance();
			String[] viewables = new View(viewPK,language).getViewables();
			String[] labelViewables = new String[viewables.length];
			HashMap<String,String> xpathToLabel = CommonDWR.getFieldsByDataModel(
					config.getModel(),
					CommonDWR.getConceptFromBrowseItemView(viewPK),
					language, true);
			for (int i = 0; i < viewables.length; i++) {
				labelViewables[i] = xpathToLabel.get(viewables[i]);
				//System.out.println(labelViewables[i]);
			}
			return labelViewables;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public TreeNode getRootNode(String concept, String language) throws RemoteException, Exception{
		Configuration config = Configuration.getInstance();
		String dataModelPK = config.getModel();
		Map<String,XSElementDecl> map = CommonDWR.getConceptMap(dataModelPK);    	
    	XSAnnotation xsa = map.get(concept).getAnnotation();    	
    	TreeNode rootNode = new TreeNode();
		ArrayList<String> roles = Util.getAjaxSubject().getRoles();
    	rootNode.fetchAnnotations(xsa,roles, language);
    	return rootNode;
	}
	
	
	
	/**
	 * start to parse the xsd.
	 *  set the maps : idToParticle, idToXpath and the list : nodeAutorization in the session
	 * @param concept
	 * @param ids
	 * @param nodeId the id of the root node in yui tree
	 * @return an error or succes message
	 */
	public String setTree(String concept, String[] ids, int nodeId, boolean foreignKey, int docIndex){
        WebContext ctx = WebContextFactory.get();	
		try {

			Configuration config = Configuration.getInstance();
			String dataModelPK = config.getModel();
			String dataClusterPK = config.getCluster();

			// get item
	        if(ids!=null){
				WSItem wsItem = Util.getPort().getItem(
						new WSGetItem(new WSItemPK(
								new WSDataClusterPK(dataClusterPK),
								concept, 
								ids
						))
				);
				Document document = Util.parse(wsItem.getContent());				
				if(foreignKey) ctx.getSession().setAttribute("itemDocumentFK",document);
				else ctx.getSession().setAttribute("itemDocument"+docIndex,document);
	        }
	        else{
	        	createItem(concept, docIndex);
	        }
			
			Map<String,XSElementDecl> map = CommonDWR.getConceptMap(dataModelPK);
        	
        	XSComplexType xsct = (XSComplexType)(map.get(concept).getType());
        	
        	HashMap<Integer,XSParticle> idToParticle;
			if(ctx.getSession().getAttribute("idToParticle") == null) {
				idToParticle = new HashMap<Integer,XSParticle>();
			}
			else {
				idToParticle = (HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
			}
			idToParticle.put(nodeId,xsct.getContentType().asParticle());
			ctx.getSession().setAttribute("idToParticle",idToParticle);
			
			HashMap<Integer,String> idToXpath;
			if(ctx.getSession().getAttribute("idToXpath") == null) {
				idToXpath = new HashMap<Integer,String>();
			}
			else {
				idToXpath = (HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
			}
			idToXpath.put(nodeId,"/"+concept);			
			ctx.getSession().setAttribute("idToXpath",idToXpath);
			
			HashMap<String,XSParticle> xpathToParticle = new HashMap<String,XSParticle>();					
			xpathToParticle.put("/"+concept,xsct.getContentType().asParticle());			
			ctx.getSession().setAttribute("xpathToParticle",xpathToParticle);
			
			ArrayList<String> nodeAutorization = new ArrayList<String>();
			ctx.getSession().setAttribute("nodeAutorization",nodeAutorization);
			
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}		
	}
		

	/**
	 * give the children of a node
	 * @param id the id of the node in yui
	 * @param nodeCount the internal count of nodes in yui tree
	 * @param language
	 * @return an array of TreeNode
	 */
	public TreeNode[] getChildren(int id, int nodeCount, String language, boolean foreignKey, int docIndex){
		WebContext ctx = WebContextFactory.get();	
		HashMap<Integer,XSParticle> idToParticle = 
			(HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		HashMap<String,XSParticle> xpathToParticle = 
			(HashMap<String,XSParticle>) ctx.getSession().getAttribute("xpathToParticle");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		String[] keys = (String[]) ctx.getSession().getAttribute("foreignKeys");
		
		if(foreignKey) d = (Document) ctx.getSession().getAttribute("itemDocumentFK");
		
		boolean choice = false;
		ArrayList<String> roles = new ArrayList<String>();
		try {
			roles = Util.getAjaxSubject().getRoles();
		} catch (PolicyContextException e1) {
			e1.printStackTrace();
		}
	
		XSParticle[] xsp = null;

		if(idToParticle.get(id)==null){//simple type case, no children
			return null;
		}
		xsp = idToParticle.get(id).getTerm().asModelGroup().getChildren();
		if("choice".equals(idToParticle.get(id).getTerm().asModelGroup().getCompositor().toString()))
			choice = true;

		
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();
		//iterate over children
    	for (int j = 0; j < xsp.length; j++) {
    		TreeNode treeNode = new TreeNode();    		
    		treeNode.setChoice(choice);
			String xpath = idToXpath.get(id)+"/"+xsp[j].getTerm().asElementDecl().getName();
			int maxOccurs = xsp[j].getMaxOccurs();   	
			//idToXpath.put(nodeCount,xpath);//keep map <node id -> xpath>  in the session
    		treeNode.setName(xsp[j].getTerm().asElementDecl().getName());
    		treeNode.setDocumentation("");
    		String typeNameTmp = "";
    		treeNode.setVisible(true);
    		
    		if(xsp[j].getTerm().asElementDecl().getType().getName()!=null)	
    			typeNameTmp = xsp[j].getTerm().asElementDecl().getType().getName();
    		/*else if(xsp[j].getTerm().asElementDecl().getType()
    				.asSimpleType().asRestriction().getBaseType()!=null)
    			xsp[j].getTerm().asElementDecl().getType()
				.asSimpleType().asRestriction().getBaseType();*/
    		
    		//annotation support
    		XSAnnotation xsa = xsp[j].getTerm().asElementDecl().getAnnotation();
    		try {
				treeNode.fetchAnnotations(xsa, roles, language);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("NO ANNOT");
			}
	

    		treeNode.setTypeName(typeNameTmp);
    		treeNode.setXmlTag(xsp[j].getTerm().asElementDecl().getName());
    		treeNode.setNodeId(nodeCount);
    		treeNode.setMaxOccurs(maxOccurs);
    		treeNode.setMinOccurs(xsp[j].getMinOccurs());
    		
			// this child is a complex type
    		if(xsp[j].getTerm().asElementDecl().getType().isComplexType()==true) {    			
    			XSParticle particle = xsp[j].getTerm().asElementDecl()
						.getType().asComplexType().getContentType().asParticle();
    			idToParticle.put(nodeCount, particle);    	
   			
    			treeNode.setType("complex");
    			if(maxOccurs<0 || maxOccurs>1){	//maxoccurs<0 is unbounded			
					try {
						NodeList nodeList = Util.getNodeList(d,xpath);
    					for (int i = 0; i < nodeList.getLength(); i++) { 
    						idToXpath.put(nodeCount,xpath+"["+(i+1)+"]");
    						xpathToParticle.put(xpath+"["+(i+1)+"]",particle);
							TreeNode treeNodeTmp = (TreeNode) treeNode.clone();
							treeNodeTmp.setNodeId(nodeCount);
							idToParticle.put(nodeCount, particle);
							// TODO check addThisNode
			    			list.add(treeNodeTmp);  
    						nodeCount++;
						}
    					if(nodeList.getLength() == 0){
    	    				idToXpath.put(nodeCount,xpath);
    	    				xpathToParticle.put(xpath,particle);
    	    				if(treeNode.isVisible()==true) {
    	    	    			list.add(treeNode);    			
    	    	    			nodeCount++; 
    	    	    		} 
    					}
					} catch (Exception e) {
						e.printStackTrace();
					}					
    			}
    			else {
    				idToXpath.put(nodeCount,xpath);
    				xpathToParticle.put(xpath,particle);
    				if(treeNode.isVisible()==true) {
    	    			list.add(treeNode);    			
    	    			nodeCount++; 
    	    		} 
    			}
    		}
    		// this child is a simple type
    		else {
    			idToParticle.put(nodeCount, null);
    			treeNode.setType("simple"); 

    			// restriction support
    			ArrayList<Restriction> restrictions = new ArrayList<Restriction>();
    			ArrayList<String> enumeration = new ArrayList<String>();
    			 Iterator<XSFacet> it = xsp[j].getTerm().asElementDecl().getType()
    				.asSimpleType().asRestriction().iterateDeclaredFacets();
    			 while (it.hasNext()) {
					XSFacet xsf = it.next();
					if("enumeration".equals(xsf.getName())) {
						enumeration.add(xsf.getValue().toString());
					}
					else{
						Restriction r = new Restriction(xsf.getName(),xsf.getValue().toString());
						restrictions.add(r);
					}					
				}
    			treeNode.setEnumeration(enumeration);
				treeNode.setRestrictions(restrictions);
    			
				// the user cannot edit any field when a foreign key is displayed
				if(foreignKey){
					treeNode.setReadOnly(true);
				}
				for (int i = 0; i < keys.length; i++) {
					if(xpath.equals(keys[i])){
						treeNode.setKey(true);
						treeNode.setKeyIndex(i);
						//treeNode.setReadOnly(true);
					}
						
				}

				
				// max occurs > 1 support
    			try { 
    				if(maxOccurs<0 || maxOccurs>1){
    					NodeList nodeList = Util.getNodeList(d,xpath);
    					for (int i = 0; i < nodeList.getLength(); i++) {
    						if(!treeNode.isReadOnly())
    							nodeAutorization.add(xpath+"["+(i+1)+"]");
    						idToXpath.put(nodeCount,xpath+"["+(i+1)+"]");
							TreeNode treeNodeTmp = (TreeNode) treeNode.clone();
							if(nodeList.item(i).getFirstChild()!=null)
								treeNodeTmp.setValue(nodeList.item(i).getFirstChild().getNodeValue());
							treeNodeTmp.setNodeId(nodeCount);
							// TODO check addThisNode
			    			list.add(treeNodeTmp);  
    						nodeCount++;
						}
    					if(nodeList.getLength() == 0){
    						if(!treeNode.isReadOnly())
    							nodeAutorization.add(xpath);
        					idToXpath.put(nodeCount,xpath);
        		    		if(treeNode.isVisible()==true){
        		    			list.add(treeNode);    			
        		    			nodeCount++; 
        		    		}  
    					}
    				}
    				else{
    					if(!treeNode.isReadOnly())
    						nodeAutorization.add(xpath);
    					idToXpath.put(nodeCount,xpath);
    					treeNode.setValue(StringEscapeUtils.escapeHtml(Util.getFirstTextNode(d,xpath)));
    		    		if(treeNode.isVisible()==true){
    		    			list.add(treeNode);    			
    		    			nodeCount++; 
    		    		}  
    				}
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}   	   		
		}		

		return list.toArray(new TreeNode[list.size()]); 
	}
	
	private void clearChildrenValue(Node node){
		if(node.getFirstChild()!=null && node.getFirstChild().getNodeType()==Node.TEXT_NODE){
			node.getFirstChild().setNodeValue("");
		}
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			clearChildrenValue(list.item(i));
		}
	}
	
	public String cloneNode(int siblingId, int newId, int docIndex) throws Exception{
		WebContext ctx = WebContextFactory.get();
		HashMap<Integer,XSParticle> idToParticle = 
			(HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		XSParticle xsp = idToParticle.get(siblingId);
		// associate the new id node to the particle of his sibling
		idToParticle.put(newId,xsp);
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);		
		try {
			Node node = Util.getNodeList(d,idToXpath.get(siblingId)).item(0);
			//System.out.println(Util.getNodeList(d,idToXpath.get(siblingId)).getLength()+" "+idToXpath.get(siblingId));
			Node nodeClone = node.cloneNode(true);
			clearChildrenValue(nodeClone);
			// simulate an "insertAfter()" which actually doesn't exist
			insertAfter(nodeClone,node);
/*			if(node.getNextSibling()!=null)
				node.getParentNode().insertBefore(nodeClone,node.getNextSibling());
			else
				node.getParentNode().appendChild(nodeClone);
*/			
			String siblingXpath = idToXpath.get(siblingId).replaceAll("\\[\\d+\\]$","");
			int id = Util.getNodeList(d,siblingXpath).getLength();
			idToXpath.put(newId,siblingXpath+"["+id+"]");
			nodeAutorization.add(siblingXpath+"["+id+"]");
			return "Cloned";
		} catch (Exception e) {
			e.printStackTrace();	
			return "Error";
		}
//		System.out.println("xml"+CommonDWR.getXMLStringFromDocument(d));
	}
	

	public String updateNode(int id, String content, int docIndex) throws TransformerException{
		WebContext ctx = WebContextFactory.get();
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		String xpath = idToXpath.get(id);
		return updateNode2(xpath,StringEscapeUtils.unescapeHtml(content),docIndex);
	}
	
	public static String updateNode2(String xpath, String content, int docIndex) throws TransformerException{
		WebContext ctx = WebContextFactory.get();
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		/*for (Iterator iter = nodeAutorization.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			System.out.println("autorisation "+element);
		}*/	

		//TODO
		if(!nodeAutorization.contains(xpath) 
				&& !nodeAutorization.contains(xpath.replaceAll("\\[.*\\]",""))){
			return "Not authorized";
		}
		try {			
			Document d2 = checkNode(xpath, d);
			String oldValue = Util.getFirstTextNode(d,xpath);
			if(content.equals(oldValue))
				return "Nothing to update";
			//Util.getNodeList(d, xpath).item(0).setTextContent(content);
			if(oldValue==null)
				Util.getNodeList(d, xpath).item(0).appendChild(d.createTextNode(content));
			else
				Util.getNodeList(d, xpath).item(0).getFirstChild().setNodeValue(content);
			//TODO add path to session
			HashMap<String,UpdateReportItem> updatedPath;
			if(ctx.getSession().getAttribute("updatedPath")!=null){
				updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
			}				
			else{
				updatedPath = new HashMap<String,UpdateReportItem>();
			}			
			if(updatedPath.get(xpath)!=null) {
				oldValue = updatedPath.get(xpath).getOldValue();
			}
			UpdateReportItem item = new UpdateReportItem(xpath,oldValue,content);
			updatedPath.put(xpath,item);
			ctx.getSession().setAttribute("updatedPath",updatedPath);
			return "Node updated";
		} 
		catch (Exception e2) {
				e2.printStackTrace();
				return "Error";
		}
	}
	
	private static Document checkNode(String xpath, Document d) throws Exception{
		// try each element of the xpath and check if it exists in datamodel
		if(xpath.charAt(0)=='/') {
			xpath = xpath.substring(1);
		}
		String[] elements = xpath.split("/");
		String xpathParent = "/";
		for (int i = 0; i < elements.length; i++) {				
			if(CommonDWR.getNodeList(d, xpathParent+"/"+elements[i]).getLength()==0){
				d = createNode(xpathParent, elements[i], d);
			}
			if(i==0) xpathParent = "/"+elements[i];
			else xpathParent += "/"+elements[i];
		}
		return d;
	}
	
	private static Document createNode(String xpathParent, String nodeToBeCreated, Document d) throws Exception {
		WebContext ctx = WebContextFactory.get();
		HashMap xpathToParticle = 
			(HashMap) ctx.getSession().getAttribute("xpathToParticle");
		
		Element el = d.createElement(nodeToBeCreated);
		XSParticle[] xsp = null;
		
		xsp = ((XSParticle) xpathToParticle.get(xpathParent)).getTerm().asModelGroup().getChildren();

		String elementAfter ="";
		for (int i = 0; i < xsp.length; i++) {
			String element = xsp[i].getTerm().asElementDecl().getName();
			if(nodeToBeCreated.equals(element)){
				//System.out.println("found");		
				if(i==xsp.length-1){
					//System.out.println("case append child 1");
					Node parent = Util.getNodeList(d,xpathParent).item(0);
					parent.appendChild(el);
					return d;
				}
				for (int j = 0; j < xsp.length-i-1; j++) {
					elementAfter = xpathParent+"/"+xsp[i+j+1].getTerm().asElementDecl().getName();
					//System.out.println("element after : "+elementAfter);
					Node node = Util.getNodeList(d,elementAfter).item(0);
					if(node!=null){
						node.getParentNode().insertBefore(el,node);
						return d;
					}	
				}
				
				
				//TODO
				{
					System.out.println("case append child 2");
					Node parent = Util.getNodeList(d,xpathParent).item(0);
					parent.appendChild(el);
				}
			}
		}
		return d;
	}
	

	public String removeNode(int id, int docIndex, String oldValue){ 
		WebContext ctx = WebContextFactory.get();
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		try{			
			Util.getNodeList(d, idToXpath.get(id)).item(0).getParentNode()
				.removeChild(Util.getNodeList(d, idToXpath.get(id)).item(0));
			HashMap<String,UpdateReportItem> updatedPath;
			if(ctx.getSession().getAttribute("updatedPath")!=null){
				updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
			}				
			else{
				updatedPath = new HashMap<String,UpdateReportItem>();
			}
			UpdateReportItem ri = new UpdateReportItem(idToXpath.get(id),oldValue,"");
			updatedPath.put(idToXpath.get(id),ri);
			ctx.getSession().setAttribute("updatedPath",updatedPath);
			return "Deleted";
		}
		catch(Exception e){
			e.printStackTrace();
			return "Error";
		}		
	}
	
	public static String saveItem(String[] ids, String concept, boolean newItem, int docIndex) throws Exception{
		WebContext ctx = WebContextFactory.get();		
		try {		
			Configuration config = Configuration.getInstance();
			String dataModelPK = config.getModel();
			String dataClusterPK = config.getCluster();
			Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
			String xml = CommonDWR.getXMLStringFromDocument(d);
			xml = xml.replaceAll("<\\?xml.*?\\?>","");	
			//<?xml version="1.0" encoding="UTF-8"?>
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).debug("saveItem() "+xml);
			WSItemPK wsi = Util.getPort().putItem(
					new WSPutItem(
							new WSDataClusterPK(dataClusterPK), 
							xml,
							new WSDataModelPK(dataModelPK)));	
			ctx.getSession().setAttribute("viewNameItems",null);
			String operationType = "";
			if(newItem==true) operationType = "CREATE";
			else operationType = "UPDATE";
			String result = pushUpdateReport(wsi.getIds(),concept,operationType);		
			return result;
		}
		catch(Exception e){			
			String err= "Unable to save item '"+concept+"."+Util.joinStrings(ids, ".")+"'";
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).error(err,e);
			throw new Exception(e.getLocalizedMessage());
		}		

	}
	
	
	public String deleteItem(String concept, String[] ids) {
		WebContext ctx = WebContextFactory.get();
		try {
			Configuration config = Configuration.getInstance();
			String dataClusterPK = config.getCluster();
			TreeNode rootNode = getRootNode(concept, "en");
	        if(ids!=null && !rootNode.isReadOnly()){
				WSItemPK wsItem = Util.getPort().deleteItem(
						new WSDeleteItem(new WSItemPK(
								new WSDataClusterPK(dataClusterPK),
								concept, ids
								)));
				if(wsItem!=null)
					pushUpdateReport(ids,concept, "DELETE");
				else
					return "ERROR";
				ctx.getSession().setAttribute("viewNameItems",null);
				return "OK";
	        }
	        else {
	        	return "OK - But no update report";
	        }
		}
		catch(Exception e){
			return "ERROR";
		}        
	}
	
	/**
	 * create an "empty" item from scratch, set every text node to empty
	 * @param viewPK
	 * @throws RemoteException
	 * @throws Exception
	 */
	private void createItem(String concept, int docIndex) throws RemoteException, Exception{
		WebContext ctx = WebContextFactory.get(); 
		Configuration config = Configuration.getInstance(); 
		String xml1 = "<"+concept+"></"+concept+">";
		Document d = Util.parse(xml1);				
		ctx.getSession().setAttribute("itemDocument"+docIndex,d);
		Map<String,XSElementDecl> map = CommonDWR.getConceptMap(config.getModel());
    	XSComplexType xsct = (XSComplexType)(map.get(concept).getType());
    	XSParticle[] xsp = xsct.getContentType().asParticle().getTerm().asModelGroup().getChildren();
    	for (int j = 0; j < xsp.length; j++) {  			
    		//setChilden(xsp[j], "/"+concept, docIndex);
    	}
	}

	
	private void setChilden(XSParticle xsp, String xpathParent, int docIndex) throws Exception{
		WebContext ctx = WebContextFactory.get();
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		Element el = d.createElement(xsp.getTerm().asElementDecl().getName());
		Node node = Util.getNodeList(d,xpathParent).item(0);
		node.appendChild(el);

		if(xsp.getTerm().asElementDecl().getType().isComplexType()==true ){
			XSParticle particle = xsp.getTerm().asElementDecl()
			.getType().asComplexType().getContentType().asParticle();
			XSParticle[] xsps = particle.getTerm().asModelGroup().getChildren();
			xpathParent = xpathParent+"/"+xsp.getTerm().asElementDecl().getName();
			for (int i = 0; i < xsps.length; i++) {
				setChilden(xsps[i],xpathParent, docIndex);
			}
		}		
	}
	
	
	public String countForeignKey(String xpathForeignKey) throws Exception{
		Configuration config = Configuration.getInstance();
		String conceptName = Util.getConceptFromPath(xpathForeignKey);
		return Util.getPort().count(
			new WSCount(
				new WSDataClusterPK(config.getCluster()),
				conceptName,
				null,
				-1
			)
		).getValue();
	}
	
	

	public TreeMap<String,String> getForeignKeyList(String xpathForeignKey, String xpathInfoForeignKey, String value) throws RemoteException, Exception{
		
		org.apache.log4j.Logger.getLogger(this.getClass()).trace("getForeignKeyList() xPath FK: '"+xpathForeignKey+"' xPath FK Info: '"+xpathInfoForeignKey+"' value: '"+value+"'");
		
		Configuration config = Configuration.getInstance();
		TreeMap<String,String> map = new TreeMap<String,String>();
		
		// foreign key set by business concept
		if(xpathForeignKey.split("/").length == 1){
			String conceptName = xpathForeignKey;

			//determine if we have xPath Infos: e.g. labels to display
			String[] xpathInfos = new String[0];
			if(!"".equals(xpathInfoForeignKey))	xpathInfos = xpathInfoForeignKey.split(",");
			
			// build query - add a content condition on the pivot if we search for a particular value
			String filteredConcept = conceptName;
			if(value!=null && !"".equals(value.trim())){
				filteredConcept+="[(descendant-or-self::* &= \""+value+"*\"),(descendant-or-self::*/attribute() &= \""+value+"*\")]";
			}
			
			//add the xPath Infos Path
			ArrayList<String> xPaths = new ArrayList<String>();
			for (int i = 0; i < xpathInfos.length; i++) {
				xPaths.add(xpathInfos[i].replaceFirst(conceptName, filteredConcept));
			}
			//add the key paths last, since there may be multiple keys
			xPaths.add(filteredConcept+"/../../i");
			
			//Run the query
			String [] results = Util.getPort().xPathsSearch(new WSXPathsSearch(
				new WSDataClusterPK(config.getCluster()),
				filteredConcept,
				new WSStringArray(xPaths.toArray(new String[xPaths.size()])),
				null,
				-1,
				0,
				-1,
				null,
				null
			)).getStrings();
			
			//parse the results - each result contains the xPathInfo values, followed by the keys
			for (int i = 0; i < results.length; i++) {
				Element root = Util.parse(results[i]).getDocumentElement();
				NodeList list = root.getChildNodes();

				//recover keys - which are last
				String keys = "";
				for (int j = xpathInfos.length; j<list.getLength(); j++) {
					Node textNode = list.item(j).getFirstChild();
					keys += "["+(textNode == null ? "" : textNode.getNodeValue())+"]";
				}
				
				//recover xPathInfos
				String infos = null;
				
				//if no xPath Infos given, use the key values
				if (xpathInfos.length == 0) {
					infos = keys;
				} else {
					//build a dash separated string of xPath Infos
    				for (int j = 0; j < xpathInfos.length; j++) {
    					infos = (infos == null ? "" : infos+" - ");
    					Node textNode = list.item(j).getFirstChild();
    					infos  += textNode == null ? "" : textNode.getNodeValue();
    				}
				}
				
				//update the map
				map.put(StringEscapeUtils.escapeXml(keys), infos);
			}
			return map;
		}
		

		//This is the "classic" case where not a Concept name but a full Path to a key is given
		String[] xpaths = null;
		
		//if we have xPaths Infos data, add them to the xPath Query to pull them
		if(!"".equals(xpathInfoForeignKey)){
			String[] xpathInfos = xpathInfoForeignKey.split(",");			
			xpaths = new String[xpathInfos.length+1];
			xpaths[0] = xpathForeignKey;
			System.arraycopy(xpathInfos, 0, xpaths, 1, xpathInfos.length);
		}else {
			xpaths = new String[1];
			xpaths[0] = xpathForeignKey;
		}
		 
		//run the search
		String[] results = Util.getPort().xPathsSearch(
				new WSXPathsSearch(
					new WSDataClusterPK(config.getCluster()),
					xpathForeignKey,//pivot
					new WSStringArray(xpaths),
					null,
					-1,		//spell Threshold
					0,		//start
					Integer.MAX_VALUE,
					null, 	//order By
					null	//direction
					)
				).getStrings();
		
		//parse the results to put them in the map
		for (int i = 0; i < results.length; i++) {
			if(results[i]!=null){
				Document d = Util.parse(results[i]);
				String tmp = "";
				for (int j = 0; j < xpaths.length; j++) {
					tmp += " - "+Util.getFirstTextNode(d,"//"+xpaths[j].split("/")[xpaths[j].split("/").length-1]);
				}
				if(Util.getFirstTextNode(d,"//"+xpathForeignKey.split("/")[xpathForeignKey.split("/").length-1])!=null)
					map.put(Util.getFirstTextNode(d,"//"+xpathForeignKey.split("/")[xpathForeignKey.split("/").length-1]),tmp.substring(3));	
			}		
		}
		
		return map;
	}
	
	
	private static String pushUpdateReport(String[] ids, String concept, String operationType){
		org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).trace("pushUpdateReport() concept "+concept+" operation "+operationType);
		WebContext ctx = WebContextFactory.get();
		HashMap<String,UpdateReportItem> updatedPath = new HashMap<String,UpdateReportItem>();
		updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
		if(!"DELETE".equals(operationType) && updatedPath==null){
			return "ERROR_2";
		}
		String key = "";
		if(ids!=null){
			for (int i = 0; i < ids.length; i++) {
				key+=ids[i];
				if(i!=ids.length-1) key+=".";
			}
		}
		String xml2 = "" +
			"<Update>"+
            "<Source>genericUI</Source>"+
            "<TimeInMillis>"+System.currentTimeMillis()+"</TimeInMillis>"+
            "<OperationType>"+StringEscapeUtils.escapeXml(operationType)+"</OperationType>"+
            "<Concept>"+StringEscapeUtils.escapeXml(concept)+"</Concept>"+
            "<Key>"+StringEscapeUtils.escapeXml(key)+"</Key>";
		if("UPDATE".equals(operationType)){
			Collection<UpdateReportItem> list = updatedPath.values();
			for (Iterator<UpdateReportItem> iter = list.iterator(); iter.hasNext();) {
				UpdateReportItem item = iter.next();
		            xml2 += 
		            "<Item>"+
		            "   <path>"+StringEscapeUtils.escapeXml(item.getPath())+"</path>"+
		            "   <oldValue>"+StringEscapeUtils.escapeXml(item.getOldValue())+"</oldValue>"+
		            "   <newValue>"+StringEscapeUtils.escapeXml(item.getNewValue())+"</newValue>"+
		           "</Item>"; 		
				}     
		}
        xml2 += "</Update>";

		ctx.getSession().setAttribute("updatedPath",null);
		ctx.getSession().setAttribute("viewNameItems",null);
		try {
			WSItemPK itemPK = Util.getPort().putItem(
					new WSPutItem(
							new WSDataClusterPK("UpdateReport"), 
							xml2,
							new WSDataModelPK("UpdateReport")));
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).debug(
					"pushUpdateReport() "+xml2);
			Util.getPort().routeItemV2(new WSRouteItemV2(itemPK));
			return "OK";
		} catch (RemoteException e) {			
			e.printStackTrace();
			return "ERROR";
		} catch (XtentisWebappException e) {
			e.printStackTrace();
			return "ERROR";
		}			
	}
	
	private void insertAfter(Node newNode, Node node){
		if(node.getNextSibling()!=null)
			node.getParentNode().insertBefore(newNode,node.getNextSibling());
		else
			node.getParentNode().appendChild(newNode);
	}	
	
	public static boolean checkIfTransformerExists(String concept, String language) {
		try{
			WSTransformerPK[] wst = Util.getPort().getTransformerPKs(new WSGetTransformerPKs("*")).getWsTransformerPK();
			for (int i = 0; i < wst.length; i++) {
				if(wst[i].getPk().equals("Smart_view_"+concept+"_"+language.toUpperCase())){
					return true;
				}
			}
			return false;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public boolean checkIfDocumentExists(String[] ids, String concept) throws Exception{
		Configuration config = Configuration.getInstance();
		 return  Util.getPort().existsItem(
				 new WSExistsItem(new WSItemPK(new WSDataClusterPK(config.getCluster()),concept, ids))
				 ).is_true();
	}
	
	public int countItems(String criteria, String dataObjet) throws Exception{
		Configuration config = Configuration.getInstance();
		ArrayList<WSWhereItem> conditions=new ArrayList<WSWhereItem>();
		WSWhereItem wi;
		String[] filters = criteria.split(",");
		String[] filterXpaths = new String[filters.length];
		String[] filterOperators = new String[filters.length];
		String[] filterValues = new String[filters.length];

		
		for (int i = 0; i < filters.length; i++) {
			if(filters[i].split("#").length>2){
				filterXpaths[i] = filters[i].split("#")[0];
				filterOperators[i] = filters[i].split("#")[1];
				filterValues[i] = filters[i].split("#")[2];
			}					
		}
		for(int i=0;i<filterValues.length;i++){
			if ((filterValues[i]==null) || ("*".equals(filterValues[i])) || "".equals(filterValues[i])) {
				continue;
			}
			WSWhereCondition wc=new WSWhereCondition(
					filterXpaths[i],
					getOperator(filterOperators[i]),
					filterValues[i],
					WSStringPredicate.NONE,
					false
					);
			//System.out.println("iterator :"+i+"field - getErrors- : " + fields[i] + " " + operator[i]);
			//System.out.println("Xpath field - getErrors- : " + giveXpath(fields[i]) + " - values : "+ regexs[i]);
			WSWhereItem item=new WSWhereItem(wc,null,null);
			conditions.add(item);
		}				
		if(conditions.size()==0) { 
			wi=null;
		} else {
			WSWhereAnd and=new WSWhereAnd(conditions.toArray(new WSWhereItem[conditions.size()]));
			wi=new WSWhereItem(null,and,null);
		}
		
		//count items 
		int count = Integer.parseInt(Util.getPort().count(new WSCount(
				new WSDataClusterPK(config.getCluster()),
				dataObjet,
				wi,
				0
		)).getValue());
		return count;
		
	}
	
	private WSWhereOperator getOperator(String option){
		WSWhereOperator res = null;
		if (option.equalsIgnoreCase("CONTAINS"))
			res = WSWhereOperator.CONTAINS;
		else if (option.equalsIgnoreCase("EQUALS"))
			res = WSWhereOperator.EQUALS;
		else if (option.equalsIgnoreCase("GREATER_THAN"))
			res = WSWhereOperator.GREATER_THAN;
		else if (option.equalsIgnoreCase("GREATER_THAN_OR_EQUAL"))
			res = WSWhereOperator.GREATER_THAN_OR_EQUAL;
		else if (option.equalsIgnoreCase("JOIN"))
			res = WSWhereOperator.JOIN;
		else if (option.equalsIgnoreCase("LOWER_THAN"))
			res = WSWhereOperator.LOWER_THAN;
		else if (option.equalsIgnoreCase("LOWER_THAN_OR_EQUAL"))
			res = WSWhereOperator.LOWER_THAN_OR_EQUAL;
		else if (option.equalsIgnoreCase("NOT_EQUALS"))
			res = WSWhereOperator.NOT_EQUALS;
		else if (option.equalsIgnoreCase("STARTSWITH"))
			res = WSWhereOperator.STARTSWITH;
		else if (option.equalsIgnoreCase("STRICTCONTAINS"))
			res = WSWhereOperator.STRICTCONTAINS;
		return res;											
	}
}