/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.configurationinfo.ejb.local;

/**
 * Local home interface for ConfigurationInfoCtrl.
 * @xdoclet-generated at 13-08-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface ConfigurationInfoCtrlLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/ConfigurationInfoCtrlLocal";
   public static final String JNDI_NAME="amalto/local/core/configurationinfoctrl";

   public com.amalto.core.objects.configurationinfo.ejb.local.ConfigurationInfoCtrlLocal create()
      throws javax.ejb.CreateException;

}