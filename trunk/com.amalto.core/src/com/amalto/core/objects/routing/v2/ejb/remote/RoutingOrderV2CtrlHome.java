/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.routing.v2.ejb.remote;

/**
 * Home interface for RoutingOrderV2Ctrl.
 * @xdoclet-generated at 25-06-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface RoutingOrderV2CtrlHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/RoutingOrderV2Ctrl";
   public static final String JNDI_NAME="amalto/remote/core/RoutingOrderCtrlV2";

   public com.amalto.core.objects.routing.v2.ejb.remote.RoutingOrderV2Ctrl create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
