/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.synchronization.ejb.remote;

/**
 * Remote interface for SynchronizationObjectCtrl.
 * @xdoclet-generated at 14-04-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface SynchronizationObjectCtrl
   extends javax.ejb.EJBObject
{
   /**
    * Creates or updates a SynchronizationObject
    * @throws XtentisException
    */
   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK putSynchronizationObject( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO synchronizationObject )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get SynchronizationObject
    * @throws XtentisException
    */
   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO getSynchronizationObject( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get a SynchronizationObject - no exception is thrown: returns null if not found
    * @throws XtentisException
    */
   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO existsSynchronizationObject( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Remove an item
    * @throws XtentisException
    */
   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK removeSynchronizationObject( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Retrieve all SynchronizationObject PKS
    * @throws XtentisException
    */
   public java.util.Collection getSynchronizationObjectPKs( java.lang.String regex )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

}