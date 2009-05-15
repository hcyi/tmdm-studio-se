 /*
 * Generated by XDoclet - Do not edit!
 * this class was prodiuced by xdoclet automagically...
 */
package com.amalto.core.ejb.remote;

import java.util.*;

/**
 * This class is remote adapter to ItemCtrl2. It provides convenient way to access
 * facade session bean. Inverit from this class to provide reasonable caching and event handling capabilities.
 *
 * Remote facade for ItemCtrl2.
 * @xdoclet-generated at 17-04-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */

public class ItemCtrl2Remote extends Observable
{
    static ItemCtrl2Remote _instance = null;
    public static ItemCtrl2Remote getInstance() {
        if(_instance == null) {
	   _instance = new ItemCtrl2Remote();
	}
	return _instance;
    }

  /**
   * cached remote session interface
   */
  com.amalto.core.ejb.remote.ItemCtrl2 _session = null;
  /**
   * return session bean remote interface
   */
   protected com.amalto.core.ejb.remote.ItemCtrl2 getSession() {
      try {
   	if(_session == null) {
	   _session = com.amalto.core.ejb.local.ItemCtrl2Util.getHome().create();
	}
	return _session;
      } catch(Exception ex) {
        // just catch it here and return null.
        // somebody can provide better solution
	ex.printStackTrace();
	return null;
      }
   }

   public com.amalto.core.ejb.ItemPOJOPK putItem ( com.amalto.core.ejb.ItemPOJO item,com.amalto.core.objects.datamodel.ejb.DataModelPOJO datamodel )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.ejb.ItemPOJOPK retval;
       retval =  getSession().putItem( item,datamodel );

      return retval;

   }

   public com.amalto.core.ejb.ItemPOJO getItem ( com.amalto.core.ejb.ItemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.ejb.ItemPOJO retval;
       retval =  getSession().getItem( pk );

      return retval;

   }

   public com.amalto.core.ejb.ItemPOJO existsItem ( com.amalto.core.ejb.ItemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.ejb.ItemPOJO retval;
       retval =  getSession().existsItem( pk );

      return retval;

   }

   public com.amalto.core.ejb.ItemPOJOPK deleteItem ( com.amalto.core.ejb.ItemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.ejb.ItemPOJOPK retval;
       retval =  getSession().deleteItem( pk );

      return retval;

   }

   public int deleteItems ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem search,int spellTreshold )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        int retval;
       retval =  getSession().deleteItems( dataClusterPOJOPK,conceptName,search,spellTreshold );

      return retval;

   }

   public java.util.ArrayList getItems ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().getItems( dataClusterPOJOPK,conceptName,whereItem,spellThreshold,start,limit );

      return retval;

   }

   public java.util.ArrayList getItems ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().getItems( dataClusterPOJOPK,conceptName,whereItem,spellThreshold,orderBy,direction,start,limit );

      return retval;

   }

   public java.util.ArrayList viewSearch ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().viewSearch( dataClusterPOJOPK,viewPOJOPK,whereItem,spellThreshold,start,limit );

      return retval;

   }

   public java.util.ArrayList viewSearch ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().viewSearch( dataClusterPOJOPK,viewPOJOPK,whereItem,spellThreshold,orderBy,direction,start,limit );

      return retval;

   }

   public java.util.ArrayList xPathsSearch ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String forceMainPivot,java.util.ArrayList viewablePaths,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().xPathsSearch( dataClusterPOJOPK,forceMainPivot,viewablePaths,whereItem,spellThreshold,start,limit );

      return retval;

   }

   public java.util.ArrayList xPathsSearch ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String forceMainPivot,java.util.ArrayList viewablePaths,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().xPathsSearch( dataClusterPOJOPK,forceMainPivot,viewablePaths,whereItem,spellThreshold,orderBy,direction,start,limit );

      return retval;

   }

   public long count ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        long retval;
       retval =  getSession().count( dataClusterPOJOPK,conceptName,whereItem,spellThreshold );

      return retval;

   }

   public java.util.ArrayList quickSearch ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,java.lang.String searchValue,boolean matchAllWords,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().quickSearch( dataClusterPOJOPK,viewPOJOPK,searchValue,matchAllWords,spellThreshold,orderBy,direction,start,limit );

      return retval;

   }

   public java.util.ArrayList getFullPathValues ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String businessElementPath,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().getFullPathValues( dataClusterPOJOPK,businessElementPath,whereItem,spellThreshold,orderBy,direction );

      return retval;

   }

   public com.amalto.core.util.TransformerPluginContext extractUsingTransformer ( com.amalto.core.ejb.ItemPOJOPK pojoPK,com.amalto.core.ejb.TransformerPOJOPK transformerPOJOPK )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.util.TransformerPluginContext retval;
       retval =  getSession().extractUsingTransformer( pojoPK,transformerPOJOPK );

      return retval;

   }

   public void extractUsingTransformer ( com.amalto.core.ejb.ItemPOJOPK pojoPK,com.amalto.core.ejb.TransformerPOJOPK transformerPOJOPK,com.amalto.core.util.TransformerPluginContext context,com.amalto.core.util.TransformerPluginCallBack globalCallBack )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
      getSession().extractUsingTransformer( pojoPK,transformerPOJOPK,context,globalCallBack );

   }

   public void extractUsingTransformerThroughView ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.transformers.v2.util.TransformerContext context,com.amalto.core.objects.transformers.v2.util.TransformerCallBack globalCallBack,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
      getSession().extractUsingTransformerThroughView( dataClusterPOJOPK,context,globalCallBack,viewPOJOPK,whereItem,spellThreshold,orderBy,direction,start,limit );

   }

   public com.amalto.core.objects.transformers.v2.util.TransformerContext extractUsingTransformerThroughView ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.transformers.v2.ejb.TransformerV2POJOPK transformerPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.transformers.v2.util.TransformerContext retval;
       retval =  getSession().extractUsingTransformerThroughView( dataClusterPOJOPK,transformerPOJOPK,viewPOJOPK,whereItem,spellThreshold,orderBy,direction,start,limit );

      return retval;

   }

   public java.util.ArrayList runQuery ( java.lang.String revisionID,com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String query,java.lang.String[] parameters )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().runQuery( revisionID,dataClusterPOJOPK,query,parameters );

      return retval;

   }

   public java.util.TreeMap getConceptsInDataCluster ( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.TreeMap retval;
       retval =  getSession().getConceptsInDataCluster( dataClusterPOJOPK );

      return retval;

   }

  /**
   * override this method to provide feedback to interested objects
   * in case collections were changed.
   */
  public void invalidate() {

  	setChanged();
	notifyObservers();
  }
}