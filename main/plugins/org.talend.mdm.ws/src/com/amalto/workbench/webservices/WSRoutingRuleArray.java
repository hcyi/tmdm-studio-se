
package com.amalto.workbench.webservices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WSRoutingRuleArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WSRoutingRuleArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsRoutingRules" type="{urn-com-amalto-xtentis-webservice}WSRoutingRule" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSRoutingRuleArray", propOrder = {
    "wsRoutingRules"
})
public class WSRoutingRuleArray {

    @XmlElement(required = true, nillable = true)
    protected List<WSRoutingRule> wsRoutingRules;

    /**
     * Default no-arg constructor
     * 
     */
    public WSRoutingRuleArray() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public WSRoutingRuleArray(final List<WSRoutingRule> wsRoutingRules) {
        this.wsRoutingRules = wsRoutingRules;
    }

    /**
     * Gets the value of the wsRoutingRules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wsRoutingRules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWsRoutingRules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSRoutingRule }
     * 
     * 
     */
    public List<WSRoutingRule> getWsRoutingRules() {
        if (wsRoutingRules == null) {
            wsRoutingRules = new ArrayList<WSRoutingRule>();
        }
        return this.wsRoutingRules;
    }

}
