// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.client.ServiceExceptionImpl;
import com.sun.xml.rpc.util.exception.*;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.client.HandlerChainImpl;
import javax.xml.rpc.*;
import javax.xml.rpc.encoding.*;
import javax.xml.rpc.handler.HandlerChain;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.namespace.QName;

public class XtentisService_Impl extends com.sun.xml.rpc.client.BasicService implements XtentisService {
    private static final QName serviceName = new QName("urn-com-amalto-xtentis-webservice", "XtentisService");
    private static final QName ns1_XtentisPort_QNAME = new QName("urn-com-amalto-xtentis-webservice", "XtentisPort");
    private static final Class xtentisPort_PortClass = com.amalto.webapp.util.webservices.XtentisPort.class;
    
    public XtentisService_Impl() {
        super(serviceName, new QName[] {
                        ns1_XtentisPort_QNAME
                    },
            new com.amalto.webapp.util.webservices.XtentisService_SerializerRegistry().getRegistry());
        
    }
    
    public java.rmi.Remote getPort(QName portName, Class serviceDefInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (portName.equals(ns1_XtentisPort_QNAME) &&
                serviceDefInterface.equals(xtentisPort_PortClass)) {
                return getXtentisPort();
            }
        } catch (Exception e) {
            throw new ServiceExceptionImpl(new LocalizableExceptionAdapter(e));
        }
        return super.getPort(portName, serviceDefInterface);
    }
    
    public java.rmi.Remote getPort(Class serviceDefInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (serviceDefInterface.equals(xtentisPort_PortClass)) {
                return getXtentisPort();
            }
        } catch (Exception e) {
            throw new ServiceExceptionImpl(new LocalizableExceptionAdapter(e));
        }
        return super.getPort(serviceDefInterface);
    }
    
    public com.amalto.webapp.util.webservices.XtentisPort getXtentisPort() {
        String[] roles = new String[] {};
        HandlerChainImpl handlerChain = new HandlerChainImpl(getHandlerRegistry().getHandlerChain(ns1_XtentisPort_QNAME));
        handlerChain.setRoles(roles);
        com.amalto.webapp.util.webservices.XtentisPort_Stub stub = new com.amalto.webapp.util.webservices.XtentisPort_Stub(handlerChain);
        try {
            stub._initialize(super.internalTypeRegistry);
        } catch (JAXRPCException e) {
            throw e;
        } catch (Exception e) {
            throw new JAXRPCException(e.getMessage(), e);
        }
        return stub;
    }
}