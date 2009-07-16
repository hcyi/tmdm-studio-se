package com.amalto.service.itemdispatcher.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.resource.cci.Connection;
import javax.resource.cci.Interaction;
import javax.resource.cci.MappedRecord;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.amalto.connector.jca.InteractionSpecImpl;
import com.amalto.connector.jca.RecordFactoryImpl;
import com.amalto.core.ejb.ItemPOJO;
import com.amalto.core.ejb.ItemPOJOPK;
import com.amalto.core.ejb.ObjectPOJO;
import com.amalto.core.ejb.ServiceCtrlBean;
import com.amalto.core.objects.transformers.v2.ejb.TransformerV2POJOPK;
import com.amalto.core.objects.transformers.v2.util.TransformerContext;
import com.amalto.core.util.IServiceConstants;
import com.amalto.core.util.ITransformerConstants;
import com.amalto.core.util.Util;
import com.amalto.core.util.XtentisException;
import com.amalto.service.itemdispatcher.util.JSONException;
import com.amalto.service.itemdispatcher.util.JSONObject;


/**
 * @author Starkey Shu
 * 
 * @ejb.bean 	name="ItemDispatcher"
 *           	display-name="Name for ItemDispatcher"
 *           	description="Description for ItemDispatcher"
 * 		  		local-jndi-name = "amalto/local/service/itemdispatcher"
 *           	type="Stateless"
 *           	view-type="local"
 * 
 * @ejb.remote-facade
 * 
 * @ejb.permission
 * 	view-type = "remote"
 * 	role-name = "administration"
 * @ejb.permission
 * 	view-type = "local"
 * 	unchecked = "true"
 * 
 * 
 * 
 */
public class ItemDispatcherBean extends ServiceCtrlBean  implements SessionBean{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5488640142359437870L;

	
	public static final String VARIABLE_THIS_ITEM = "{this}";
	

    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public String getJNDIName() throws XtentisException {
		return "amalto/local/service/itemdispatcher";
	}
	

    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public String getDescription(String twoLetterLanguageCode) throws XtentisException {
		return "This service dispatch an item to target systems automatically, according to the DataModel annotation.";
	}
	

    /**
     * @throws XtentisException
     * 
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method 
     */
    public String getDocumentation(String twoLettersLanguageCode) throws XtentisException{
    	return "There are two cases of using parameters,\n\n" +
		"One(Example) :\n"+
		"<parameters>\n"+
		"	<transformer>\n"+
		"	  <!-- OPTIONAL DEFAULT 'true'-->\n"+
		"	  <allInOne>true</allInOne>\n"+
		"	  <assignTo>transformer1</assignTo>\n"+
		"	</transformer>\n"+
		"</parameters>\n"+
		"Two(Example) :\n"+
		"<parameters>\n"+
		"	<transformer>\n"+
		"	  <allInOne>false</allInOne>\n"+
		"	  <assignTo>\n"+
		"			  {\n"+
		"			  0:transformer1," +"\n"+
		"			  1:transformer2" +"\n"+
		"			  }\n"+
		"	  </assignTo>\n"+
		"	</transformer>\n"+
		"</parameters>\n";
    }
    
    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public String getStatus() throws XtentisException {
		return IServiceConstants.JCA_RESPONSE_STATUS_CODE_OK; 
	}

    
    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public void start() throws XtentisException {
		return;
	}



    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public void stop() throws XtentisException {
		return;
	}
	


    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public Serializable receiveFromOutbound(HashMap<String, Serializable> map) throws XtentisException {
		//N/A
		//TODO receive from out bound of 'ItemDispatcher service'
		return null;
	}




    /**
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
	public String receiveFromInbound(ItemPOJOPK itemPK, String routingOrderID, String parameters) throws XtentisException {
		//if runtime parameters is invalid, will print stacktrace in Jboss console, and return nill 
		Object bindingTransformerNames = parseParameters(parameters);
		try {
			ItemPOJO itemPOJO = Util.getItemCtrl2Local().getItem(itemPK);
			dispatch(itemPOJO,bindingTransformerNames);
    	} catch (XtentisException e) {
    		throw (e);
	    } catch (Exception e) {
    	    String err = "Unable to dispatch the item"
    	    		+": "+e.getClass().getName()+": "+e.getLocalizedMessage();
    	    org.apache.log4j.Logger.getLogger(this.getClass()).error(err, e);
    	    throw new XtentisException(err);
	    }
	    return "Item was successfully pushed to target systems";
		
	}


	private Object parseParameters(String parameters){
	
		Object bindingTransformerNames=null;
		if(parameters==null||parameters.trim().length()==0)return bindingTransformerNames;
		try {
			Element root = Util.parse(parameters).getDocumentElement();
			String allInOne=Util.getFirstTextNode(root, "//transformer/allInOne");
			String assignTo=Util.getFirstTextNode(root, "//transformer/assignTo");
			
			boolean isAllInOne=true;
			if(allInOne!=null&&allInOne.trim().length()>0){
				isAllInOne=Boolean.parseBoolean(allInOne);
			}
			if(isAllInOne){
				if(assignTo!=null&&assignTo.length()>0)bindingTransformerNames=assignTo;
			}else{
				if(assignTo!=null&&assignTo.length()>0)bindingTransformerNames=new JSONObject(assignTo);
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		};
		return bindingTransformerNames;
	}
	
	

	private void dispatch(ItemPOJO itemPOJO,Object bindingTransformerNames) throws XtentisException,Exception {
		Logger.getLogger(ItemDispatcherBean.class).info("[Begin dispatch item]");
		String itemProjectAsString=itemPOJO.getProjectionAsString();
		String schema=ItemPOJO.getBindingSchema(itemPOJO);
		String[] targetSystems=null;
		
		targetSystems=Util.getTargetSystemsFromSchema(Util.parse(schema), itemPOJO.getConceptName());
		
		for (int i = 0; i < targetSystems.length; i++) {
			String fullPath=targetSystems[i];
			fullPath=Util.xmlDecode(fullPath);
			Logger.getLogger(ItemDispatcherBean.class).debug("[Start Processing]:"+fullPath);
			Connection conx=null;
			try {
	            
				//parse JNDI name
				String jndiName="";
				String parameters="";
				int pos=fullPath.indexOf("?");
	            if(pos==-1){
	            	jndiName=fullPath;
	            }else{
	            	jndiName=fullPath.substring(0,pos);
	            	parameters=fullPath.substring(pos+1);
	            }
	            //parse runtime Parameters
	            HashMap<String,Serializable> paramMap = new HashMap<String,Serializable>();
	            if(parameters.length()>0){
	            	//TODO validate parameter format
	            	String[] paramArray=parameters.split("&");
	            	for (int j = 0; j < paramArray.length; j++) {
	            		String tmp=paramArray[j];
	            		String key=tmp.split("=")[0];
	            		String value=tmp.split("=")[1];
	            		//map inner variable 
	            		if(value.trim().equals(VARIABLE_THIS_ITEM)){
	            			value=value.replace(VARIABLE_THIS_ITEM, itemProjectAsString);
	            			
	            			try {
								//TODO call item transformer
								//TODO maybe we need transformers or functions(of xslt) called "encode" and "compact"
								String currentBindingTransformerName=null;
								if(bindingTransformerNames!=null){
									if(bindingTransformerNames instanceof String){
										currentBindingTransformerName=(String) bindingTransformerNames;
									}else if(bindingTransformerNames instanceof JSONObject){
										JSONObject bindingTransformerNamesJSONObject=(JSONObject) bindingTransformerNames;
										if(bindingTransformerNamesJSONObject.has(i+"")){
											currentBindingTransformerName=bindingTransformerNamesJSONObject.getString(i+"");
										}
									}
								}
								Logger.getLogger(ItemDispatcherBean.class).debug("[Binding Transformer Name]:"+currentBindingTransformerName);
								//TODO check transformer exist
								TransformerContext ctx = new TransformerContext(new TransformerV2POJOPK(currentBindingTransformerName));
								ctx = Util.getTransformerV2CtrlLocal().executeUntilDone(
										ctx,
										new com.amalto.core.objects.transformers.v2.util.TypedContent(
												value.getBytes("UTF8"),
												"text/xml; charset=utf-8"
										)
								);
								if(ctx.getFromPipeline(ITransformerConstants.VARIABLE_OUTPUT_TO_ITEMDISPATCHERSERVICE)!=null){
									value = new String(ctx.getFromPipeline(ITransformerConstants.VARIABLE_OUTPUT_TO_ITEMDISPATCHERSERVICE).getContentBytes(),"UTF-8");
								}
							} catch (Exception e) {
								String err = "Unable to call binding transformer of this item: "+e.getClass().getName()+": "+e.getLocalizedMessage();
			    	            org.apache.log4j.Logger.getLogger(this.getClass()).error(err, e);
							}
	            		}
	            		
	            		paramMap.put(key, value);
					}
	            }
	            Logger.getLogger(ItemDispatcherBean.class).debug("[Parameters]:"+paramMap);
	            //TODO default get jca configuration from db
	            //Before call connect
	              
	            //TODO check jca is exist&start
				//Get Connection
            	conx = getConnection(jndiName);
				Interaction interaction = conx.createInteraction();
				InteractionSpecImpl interactionSpec = new InteractionSpecImpl();
				//Create the Record
				MappedRecord recordIn = new RecordFactoryImpl()
						.createMappedRecord(RecordFactoryImpl.RECORD_IN);
				recordIn.put(RecordFactoryImpl.PARAMS_HASHMAP_IN, paramMap);
				//Process the post
				interactionSpec
						.setFunctionName(InteractionSpecImpl.FUNCTION_PUSH);
				MappedRecord result = (MappedRecord) interaction.execute(
						interactionSpec, recordIn);
				String statusCode = (String) result
						.get(RecordFactoryImpl.STATUS_CODE_OUT);
				//Parse the result
				if (!IServiceConstants.JCA_RESPONSE_STATUS_CODE_OK.equals(statusCode)) {
					String err = "Item Dispatcher Service: could not push item to "
							+ fullPath + "! ";
					org.apache.log4j.Logger.getLogger(this.getClass()).debug(
							err);
					throw new XtentisException(err);
				} else {
					org.apache.log4j.Logger.getLogger(this.getClass()).debug(
							"Item has been pushed to " + fullPath
									+ " successfully! ");
					//TODO handle logging Event

				}
			}catch (Exception e) {
				String err = "Unable to dispatch the item to "+fullPath
    	    		+": "+e.getClass().getName()+": "+e.getLocalizedMessage();
    	        org.apache.log4j.Logger.getLogger(this.getClass()).error(err, e);
			}finally {
				try {
					if(conx!=null)conx.close();
					} 
				catch (Exception e) {}
		    }
		}
		Logger.getLogger(ObjectPOJO.class).info("[End dispatch item]");
	}
	
	/**
     * 
     * @throws EJBException
     * 
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method 
     */
    public String getConfiguration(String optionalParameters) throws XtentisException{
    	return "";
    }

    
}