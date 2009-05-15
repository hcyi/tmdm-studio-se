// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.xsd.XSDConstants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class WSRoutingOrderV2Array_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final QName ns1_wsRoutingOrder_QNAME = new QName("", "wsRoutingOrder");
    private static final QName ns2_WSRoutingOrderV2_TYPE_QNAME = new QName("urn-com-amalto-xtentis-webservice", "WSRoutingOrderV2");
    private CombinedSerializer ns2_myWSRoutingOrderV2_LiteralSerializer;
    
    public WSRoutingOrderV2Array_LiteralSerializer(QName type, String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public WSRoutingOrderV2Array_LiteralSerializer(QName type, String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myWSRoutingOrderV2_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", com.amalto.webapp.util.webservices.WSRoutingOrderV2.class, ns2_WSRoutingOrderV2_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSRoutingOrderV2Array instance = new com.amalto.webapp.util.webservices.WSRoutingOrderV2Array();
        Object member=null;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_wsRoutingOrder_QNAME))) {
            values = new ArrayList();
            for(;;) {
                elementName = reader.getName();
                if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_wsRoutingOrder_QNAME))) {
                    value = ns2_myWSRoutingOrderV2_LiteralSerializer.deserialize(ns1_wsRoutingOrder_QNAME, reader, context);
                    if (value == null) {
                        throw new DeserializationException("literal.unexpectedNull");
                    }
                    values.add(value);
                    reader.nextElementContent();
                } else {
                    break;
                }
            }
            member = new com.amalto.webapp.util.webservices.WSRoutingOrderV2[values.size()];
            member = values.toArray((Object[]) member);
            instance.setWsRoutingOrder((com.amalto.webapp.util.webservices.WSRoutingOrderV2[])member);
        }
        else if(!(reader.getState() == XMLReader.END)) {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSRoutingOrderV2Array instance = (com.amalto.webapp.util.webservices.WSRoutingOrderV2Array)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSRoutingOrderV2Array instance = (com.amalto.webapp.util.webservices.WSRoutingOrderV2Array)obj;
        
        if (instance.getWsRoutingOrder() != null) {
            for (int i = 0; i < instance.getWsRoutingOrder().length; ++i) {
                ns2_myWSRoutingOrderV2_LiteralSerializer.serialize(instance.getWsRoutingOrder()[i], ns1_wsRoutingOrder_QNAME, null, writer, context);
            }
        }
    }
}