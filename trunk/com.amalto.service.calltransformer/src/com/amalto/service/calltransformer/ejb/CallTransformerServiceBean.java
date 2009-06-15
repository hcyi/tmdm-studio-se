

package com.amalto.service.calltransformer.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.amalto.core.ejb.ItemPOJO;
import com.amalto.core.ejb.ItemPOJOPK;
import com.amalto.core.ejb.ServiceCtrlBean;
import com.amalto.core.ejb.local.ItemCtrl2Local;
import com.amalto.core.objects.transformers.v2.ejb.TransformerV2CtrlBean;
import com.amalto.core.objects.transformers.v2.ejb.TransformerV2POJOPK;
import com.amalto.core.objects.transformers.v2.ejb.local.TransformerV2CtrlLocal;
import com.amalto.core.objects.transformers.v2.util.TransformerContext;
import com.amalto.core.objects.transformers.v2.util.TypedContent;
import com.amalto.core.util.Util;
import com.amalto.core.util.XtentisException;



/**
 * <h1>Service Call Transformer</h1>
 *
 * <h3>Description</h3>
 * This service calls a transformer<br/>
 *
 * <h3>Parameters</h3>
 * This service takes a single parameter:<b>transformer</b>: the name of the transformer.<br/>
 * The transformer should expect to receive the content of the Item sent to the transformer
 * in the <code>DEFAULT</code> variable with a content-type of <code>text/xml</code>
 *
 * <h3>Configuration</h3>
 * No Configuration UI
 * <br/>
 * <br/>
 *
 * @author Bruno Grieder
 *
 * @ejb.bean name="CallTransformer"
 *           display-name="Call Transformer Service"
 *           description="Service d'intégration Call Transformer"
 * 		  local-jndi-name = "amalto/local/service/calltransformer"
 *           type="Stateless"
 *           view-type="local"
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
public class CallTransformerServiceBean extends ServiceCtrlBean  implements SessionBean{

	private static final long serialVersionUID = 1L;

//	private boolean configurationLoaded = false;

	//datamodels cach
	//protected HashMap<String, XSDKey> keys = new HashMap<String, XSDKey>();
	//protected HashMap<String, DataModelPOJO> dataModels = new HashMap<String, DataModelPOJO>();


//    private SessionContext context;

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext ctx)
        throws EJBException,
        RemoteException {
//    	context = ctx;
    }


	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#getJNDIName()
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String getJNDIName() throws XtentisException {
		return "amalto/local/service/calltransformer";
	}


	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#getDescription()
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String getDescription(String twoLetterLanguageCode) throws XtentisException {
		if ("fr".matches(twoLetterLanguageCode.toLowerCase()))
			return "Service qui appelle des transformateurs";
		return "The service call transformers";
	}



	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#getStatus()
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String getStatus() throws XtentisException {
		return "OK";
	}


	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#start()
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public void start() throws XtentisException {
	}


	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#stop()
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public void stop() throws XtentisException {
	}


	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#receiveFromOutbound(java.util.HashMap, java.lang.String)
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public Serializable receiveFromOutbound(HashMap<String, Serializable> map) throws XtentisException {
		throw new XtentisException("The Call Transformer service is not meant to interact with adapters");
	}


	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.IServiceBean#run(java.lang.String)
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String receiveFromInbound(ItemPOJOPK itemPK, String routingOrderID, String parameters) throws com.amalto.core.util.XtentisException {

		try {


			String transformer = null;

			if (parameters != null) {
				String kvs[] = parameters.split("&");
				if (kvs!=null) {
					for (int i = 0; i < kvs.length; i++) {
						String[] kv = kvs[i].split("=");
						String key = kv[0].trim().toLowerCase();

						if (("transformer".equals(key)) && (kv.length == 2)) {
							transformer = kv[1];
						}
					}

					if (transformer == null || "".equals(transformer)) {
		            	org.apache.log4j.Logger.getLogger(this.getClass()).debug("Service CallTransformer - mandatory parameter transformer name is missing");
		            	throw new XtentisException("Service CallTransformer - mandatory parameter transformer name is missing");
		    		}

					TransformerV2CtrlLocal tctrl = Util.getTransformerV2CtrlLocal();

					if (tctrl.existsTransformer(new TransformerV2POJOPK(transformer)) == null) {
		            	org.apache.log4j.Logger.getLogger(this.getClass()).debug("Service CallTransformer is unable to call transformer "+transformer+" - transformer doesn't exist");
		            	throw new XtentisException("Unable to find the transformer "+transformer);
		    		}

					ItemCtrl2Local ictrl  = Util.getItemCtrl2Local();
					ItemPOJO pojo = ictrl.getItem(itemPK);

					TransformerContext context = new TransformerContext(new TransformerV2POJOPK(transformer));
					context.putInPipeline(TransformerV2CtrlBean.DEFAULT_VARIABLE, new TypedContent(pojo.getProjectionAsString().getBytes(),"text/xml"));

//					TransformerContext contextResult =
					tctrl.executeUntilDone(context);

				}
			}

			return "CallTransformer Service successfully executed transformer '"+transformer+"'";
		} catch (Exception e) {
			String err =
				(new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss, SSS")).format(new Date(System.currentTimeMillis()))
				+": ERROR routing to Call Transformer Service "
				+": "+e.getLocalizedMessage();
			if (e instanceof XtentisException) {
				throw new XtentisException(e);
			} else {
				org.apache.log4j.Logger.getLogger(this.getClass()).error(err+" ("+e.getClass().getName()+")",e);
				throw new XtentisException(e);
			}
		}

	}



    private String getDefaultConfiguration(){

    	return
    		"<configuration/>";
        }


    /**
     *
     * @see com.amalto.core.ejb.ServiceCtrlBean#getConfiguration(java.lang.String)
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
    public String getConfiguration(String optionalParameters) throws XtentisException{
    	try {
    		String configuration = loadConfiguration();
    		if (configuration == null) {
    			configuration = getDefaultConfiguration();
    		}
//    		configurationLoaded = true;
    		return configuration;
        } catch (XtentisException e) {
    		throw (e);
	    } catch (Exception e) {
    	    String err = "Unable to deserialize the configuration of the Call Transformer Service"
    	    		+": "+e.getClass().getName()+": "+e.getLocalizedMessage();
    	    org.apache.log4j.Logger.getLogger(this.getClass()).error(err);
    	    throw new XtentisException(err);
	    }
    }



    /**
     * @see com.amalto.core.ejb.ServiceCtrlBean#putConfiguration(java.lang.String)
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public void putConfiguration(String configuration) throws XtentisException {
		org.apache.log4j.Logger.getLogger(this.getClass()).debug("putConfiguration() "+configuration);
//		configurationLoaded = false;
		super.putConfiguration(configuration);
	}


}