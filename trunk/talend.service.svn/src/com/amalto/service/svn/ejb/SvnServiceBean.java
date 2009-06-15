package com.amalto.service.svn.ejb;

import java.io.Serializable;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import com.amalto.connector.svn.util.HistoryInfos;
import com.amalto.core.ejb.ItemPOJOPK;
import com.amalto.core.objects.versioning.ejb.VersioningServiceCtrlBean;
import com.amalto.core.util.XtentisException;
import com.amalto.core.webservice.WSVersioningHistoryEntry;
import com.amalto.service.svn.bean.SvnConfiguration;
import com.amalto.service.svn.handler.SvnHandler;



/**
 * @author dleniniven
 *
 * @ejb.bean name="Svn"
 *           display-name="Svn Service"
 *           description="Svn Service"
 * 		  local-jndi-name = "amalto/local/service/svn"
 *           type="Stateless"
 *           view-type="local"
 *           local-business-interface="com.amalto.core.objects.versioning.util.VersioningServiceCtrlLocalBI"
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


public class SvnServiceBean extends VersioningServiceCtrlBean implements SessionBean {

	private static final long serialVersionUID = 198929949874567582L;
	private static final String JNDI_NAME= "amalto/local/service/svn";

//    private SessionContext context;
    private SvnConfiguration configuration;


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
		return JNDI_NAME;
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
			return "Service de gestion de Svn";
		return "Svn Service";
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
		return new SvnHandler(getConfigurationLocal()).getStatus();
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
		new SvnHandler(getConfigurationLocal()).start();
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
		new SvnHandler(getConfigurationLocal()).stop();
	}





	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#receiveFromOutbound(java.util.HashMap, java.lang.String)
	 */
    /**
     * A file is being pulled by one of the File System Listeners
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public Serializable receiveFromOutbound(HashMap<String, Serializable> map) throws XtentisException {

		return null;
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
		String err = "The SVN service does not send messages out at this stage.";
		org.apache.log4j.Logger.getLogger(this.getClass()).error("receiveFromInbound() "+err);
		throw new XtentisException(err);
	}

    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String testCheckOut(String [] input) throws com.amalto.core.util.XtentisException {
		String filename= null;
		String tag = null;
		if (input.length >0) {
			filename = input[0];
			if (input.length >1)
			tag = input[1];
		}

		byte [] content = new SvnHandler(getConfigurationLocal()).checkoutFile(filename, tag);
		String contentS = new String(content);
		org.apache.log4j.Logger.getLogger(this.getClass()).info("checkoutFile("+filename+","+tag+") => "+contentS);
		return contentS;
	}

    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String getHistory(String [] input) throws com.amalto.core.util.XtentisException {
		String filename= null;
		if (input.length >0) {
			filename = input[0];
			org.apache.log4j.Logger.getLogger(this.getClass()).debug("getHistory "+filename);
		}

		HistoryInfos infos = new SvnHandler(getConfigurationLocal()).getHistory(filename);

		org.apache.log4j.Logger.getLogger(this.getClass()).debug(infos.toString());


		return "SUCCESS";
	}


    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String testVersions(String [] input) throws com.amalto.core.util.XtentisException {
		String filename= null;
		if (input.length >0) {
			filename = input[0];
			org.apache.log4j.Logger.getLogger(this.getClass()).debug("getVersions "+filename);
		}

		org.apache.log4j.Logger.getLogger(this.getClass()).debug("Configuration URL =  "+getConfigurationLocal().getUrl());

		HistoryInfos infos = new SvnHandler(getConfigurationLocal()).getVersions(filename);

		infos.display();

		//org.apache.log4j.Logger.getLogger(this.getClass()).debug(infos.toString());


		return "SUCCESS";
	}

    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public String testBranch(String [] input) throws com.amalto.core.util.XtentisException {
		org.apache.log4j.Logger.getLogger(this.getClass()).debug("SvnService call branch");
		if (input.length <4) {
			org.apache.log4j.Logger.getLogger(this.getClass()).debug("branch usage => path tag comment username");
			return "ERROR";
		}
		String path = input[0];
		String tag = input[1];
		String comment = input[2];
		String username = input[3];

		org.apache.log4j.Logger.getLogger(this.getClass()).debug("branch "+path);
		if (username!=null && !"".equals(username)) {
    		String authorAndComment = "Author : "+username+" Comment : "+comment;
    		comment = authorAndComment;
    	}
    	new SvnHandler(getConfigurationLocal()).branch(path, tag, comment);

		return "SUCCESS";
	}

    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
    public String testCommit(String [] input) throws XtentisException {
    	if (input.length < 4) {
    		org.apache.log4j.Logger.getLogger(this.getClass()).debug("commit usage => path xml comment username");
    		return "ERROR";
    	}
        String path = input[0];
        String xml = input[1];
        String comment = input[2];
        String username = input[3];

        String normalizedPath = normalizePath(path);

    	if (username!=null && !"".equals(username)) {
    		String authorAndComment = "Author : "+username+" Comment : "+comment;
    		comment = authorAndComment;
    	}

    	new SvnHandler(getConfigurationLocal()).commit(normalizedPath, xml, comment);

    	return "SUCCESS";
    }

    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
    public String testDelete(String [] input) throws XtentisException {
    	if (input.length < 2) {
    		org.apache.log4j.Logger.getLogger(this.getClass()).debug("delete usage => path tag");
    		return "ERROR";
    	}
        String path = input[0];
        String tag = input[1];

        String normalizedPath = normalizePath(path);


    	new SvnHandler(getConfigurationLocal()).delete(normalizedPath, tag);

    	return "SUCCESS";
    }

    /**
     * Sets the default/current versioning system configuration
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public String testSetConfiguration(String [] input) throws XtentisException {
    	if (input.length < 3) {
    		org.apache.log4j.Logger.getLogger(this.getClass()).debug("delete usage => url username password");
    		return "ERROR";
    	}
    	String url = input[0];
    	String username = input[1];
    	String password = input[2];

		SvnConfiguration config = new SvnConfiguration();

		config.setUrl(url);
		config.setPassword(username);
		config.setUsername(password);

		putConfiguration(config.serialize());

		new SvnHandler(getConfigurationLocal()).start();
		return "SUCCESS";
    }

    private String getDefaultConfiguration() throws XtentisException{
    	 org.apache.log4j.Logger.getLogger(this.getClass()).warn("SERVICE SVN GET DEFAULT CONFIGURATION");
    	try {
    		SvnConfiguration config = new SvnConfiguration();
    		config.setUrl("http://192.168.0.188/");
    		config.setPassword("b2box");
    		config.setUsername("b2box");

    		return config.serialize();
    	} catch (XtentisException e) {
    		throw(e);
	    } catch (Exception e) {
    	    String err = "Unable to serialize the default configuration of the File System Service"
    	    		+": "+e.getClass().getName()+": "+e.getLocalizedMessage();
    	    org.apache.log4j.Logger.getLogger(this.getClass()).error(err);
    	    throw new XtentisException(err);
	    }
    }



	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#getConfiguration(java.lang.String)
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
    public String getConfiguration(String optionalParameters) throws XtentisException{
    	try {
    		String marshalledConfiguration = loadConfiguration();
    		if (marshalledConfiguration == null) {
    			marshalledConfiguration = getDefaultConfiguration();
    		}
    		//configuration = SvnConfiguration.parse(marshalledConfiguration);
	        configuration =  (SvnConfiguration)
			Unmarshaller.unmarshal(SvnConfiguration.class, new InputSource(new StringReader(marshalledConfiguration)));

    		return marshalledConfiguration;
        } catch (XtentisException e) {
    		throw (e);
	    } catch (Exception e) {
    	    String err = "Unable to deserialize the configuration of the File System Service"
    	    		+": "+e.getClass().getName()+": "+e.getLocalizedMessage();
    	    org.apache.log4j.Logger.getLogger(this.getClass()).error(err);
    	    throw new XtentisException(err);
	    }
    }

    private SvnConfiguration getConfigurationLocal() throws XtentisException{
    	if (configuration == null) getConfiguration(null);
    	else org.apache.log4j.Logger.getLogger(this.getClass()).debug(
				"getConfigurationLocal() conf not null");
    	return configuration;
    }

	/* (non-Javadoc)
	 * @see com.amalto.core.ejb.ServiceCtrlBean#getConfiguration(java.lang.String)
	 */
    /**
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "local"
     * @ejb.facade-method
     */
	public void putConfiguration(String marshalledConfiguration) throws XtentisException {
    	org.apache.log4j.Logger.getLogger(this.getClass()).debug("putConfiguration() ");

    	//update the cached configuration
    	try {
    		if (marshalledConfiguration == null) {
    			marshalledConfiguration = getDefaultConfiguration();
    		}
    		org.apache.log4j.Logger.getLogger(this.getClass()).debug(
					"putConfiguration() "+marshalledConfiguration);
	        configuration =  (SvnConfiguration)
				Unmarshaller.unmarshal(SvnConfiguration.class, new InputSource(new StringReader(marshalledConfiguration)));
   			//configuration = SvnConfiguration.parse(marshalledConfiguration);

	    } catch (Exception e) {
    	    String err = "Unable to deserialize the configuration of the Svn Service"
    	    		+": "+e.getClass().getName()+": "+e.getLocalizedMessage();
    	    org.apache.log4j.Logger.getLogger(this.getClass()).error(err);
    	    throw new XtentisException(err);
	    }

	    try {
	    	super.putConfiguration(marshalledConfiguration);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new XtentisException(e.getLocalizedMessage());
	    }

	}

	/**
     * Returns the Versioning History of an item or object
     * The path is constitued of the clustername/instancename
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public WSVersioningHistoryEntry[] getHistory(String path) throws XtentisException {
    	String normalizedPath = normalizePath(path);

    	HistoryInfos infos = new SvnHandler(getConfigurationLocal()).getVersions(normalizedPath);

    	String [] authors = infos.getAuthors();
    	String [] comments = infos.getComments();
    	String [] dates = infos.getDates();
    	String [] revisions = infos.getRevisions();
    	String [] tags = infos.getTagNames();
    	int size = authors.length;
    	WSVersioningHistoryEntry[] entries = new WSVersioningHistoryEntry[size];
    	for (int i=0; i<size; i++) {
    		WSVersioningHistoryEntry entry = new WSVersioningHistoryEntry();

    		String author = authors[i];
    		String comment = comments[i];

    		String authorAndComment = comments[i];
    		int indexAuthorBegin = authorAndComment.indexOf("Author : ");
    		int indexAuthorEnd = authorAndComment.indexOf(" Comment : ");

    		if ((indexAuthorBegin == 0) && (indexAuthorEnd!=-1)) {
    			author = authorAndComment.substring(9, indexAuthorEnd);
    			comment = authorAndComment.substring(indexAuthorEnd+11);
    		}

    		entry.setAuthor(author);
    		entry.setComments(comment);
    		entry.setDate(dates[i]);
    		entry.setRevision(revisions[i]);
    		entry.setTag(tags[i]);
    		entries[i] = entry;
    		org.apache.log4j.Logger.getLogger(this.getClass()).debug("Entry => Author : "+author+" - Comment : "+comment+" - Date : "+dates[i]+" - Revision : "+revisions[i]+" - Tag : "+tags[i]);
    	}
    	return entries;
    }

    /**
     * Checkouts
     * The path is constitued of the clustername/instancename
     *
     * @return the checked out xml
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public String[] checkOut(String path, String tag) throws XtentisException{
    	byte [] content = new SvnHandler(getConfigurationLocal()).checkoutFile(path, tag);
    	if (content == null)
    		return null;

    	String contentS = new String(content);

    	String [] result = new String [1];
    	result[0] = contentS;

    	return result;
    }

    /**
     * Commit to the head of the repository
     * The path is constitued of the clustername/instancename
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public void commit(String path, String xml, String comment, String username) throws XtentisException {

    	if (username!=null && !"".equals(username)) {
    		String authorAndComment = "Author : "+username+" Comment : "+comment;
    		comment = authorAndComment;
    	}

    	new SvnHandler(getConfigurationLocal()).commit(path, xml, comment);

    	return;
    }

    /**
     * Branch the head to a particular tag
     * The path is constitued of the clustername/instancename
     * If the path is a clustername only, all instances will be branched
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public void branch(String path, String tag, String comment, String username) throws XtentisException {
    	if (username!=null && !"".equals(username)) {
    		String authorAndComment = "Author : "+username+" Comment : "+comment;
    		comment = authorAndComment;
    	}
    	new SvnHandler(getConfigurationLocal()).branch(path, tag, comment);

    	return;
    }


    /**
     * Sets the default/current versioning system configuration
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public void setCurrentVersioningSystemConfiguration(String name, String url, String username, String password) throws XtentisException {
		SvnConfiguration config = new SvnConfiguration();

		config.setUrl(url);
		config.setPassword(username);
		config.setUsername(password);

		putConfiguration(config.serialize());

		new SvnHandler(getConfigurationLocal()).start();
    }

    /**
     * Clean the head by keeping on the listed instances of the particular cluster
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public String[] getInstances(String clustername, String instancename) throws XtentisException {

    	SvnHandler handler = new SvnHandler(getConfigurationLocal());
    	String [] entriesOnSvn = handler.list(normalizePath(clustername),instancename);
    	for (int i=0; i<entriesOnSvn.length; i++) {
    		entriesOnSvn[i] = this.unnormalizePath(entriesOnSvn[i]);
    	}
    	return entriesOnSvn;
    }

    /**
     * Clean the head by keeping on the listed instances of the particular cluster
     *
     * @throws EJBException
     *
     * @ejb.interface-method view-type = "both"
     * @ejb.facade-method
     */
    public void clean(String clustername, String instancenames[]) throws XtentisException {

    	SvnHandler handler = new SvnHandler(getConfigurationLocal());
    	String [] entriesOnSvn = handler.list(normalizePath(clustername),null);

    	HashMap<String,String> instances = new HashMap<String,String>();
    	for (int j=0; j<instancenames.length; j++) {
			String tmp = normalizePath(instancenames[j]);
			instances.put(tmp,tmp);
		}

    	for(int i=0;i<entriesOnSvn.length; i++) {
   			if (instances.get(entriesOnSvn[i])==null) {
   				handler.delete(entriesOnSvn[i],null);
   			}
    	}
    }

    /*
     * Normalize path to be compliant with Svn name specifications.
     */
   private String normalizePath(String path) {
	   return path;
   }

   private String unnormalizePath(String path) {
	   return path;
   }

}