// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSRoleSpecificationInstance {
    protected java.lang.String instanceName;
    protected boolean writable;
    protected java.lang.String[] parameter;
    
    public WSRoleSpecificationInstance() {
    }
    
    public WSRoleSpecificationInstance(java.lang.String instanceName, boolean writable, java.lang.String[] parameter) {
        this.instanceName = instanceName;
        this.writable = writable;
        this.parameter = parameter;
    }
    
    public java.lang.String getInstanceName() {
        return instanceName;
    }
    
    public void setInstanceName(java.lang.String instanceName) {
        this.instanceName = instanceName;
    }
    
    public boolean isWritable() {
        return writable;
    }
    
    public void setWritable(boolean writable) {
        this.writable = writable;
    }
    
    public java.lang.String[] getParameter() {
        return parameter;
    }
    
    public void setParameter(java.lang.String[] parameter) {
        this.parameter = parameter;
    }
}