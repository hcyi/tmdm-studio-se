// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation ��1.1.2_01������� R40��
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSDeleteItems {
    protected com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK;
    protected java.lang.String conceptName;
    protected com.amalto.core.webservice.WSWhereItem wsWhereItem;
    protected int spellTreshold;
    
    public WSDeleteItems() {
    }
    
    public WSDeleteItems(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK, java.lang.String conceptName, com.amalto.core.webservice.WSWhereItem wsWhereItem, int spellTreshold) {
        this.wsDataClusterPK = wsDataClusterPK;
        this.conceptName = conceptName;
        this.wsWhereItem = wsWhereItem;
        this.spellTreshold = spellTreshold;
    }
    
    public com.amalto.core.webservice.WSDataClusterPK getWsDataClusterPK() {
        return wsDataClusterPK;
    }
    
    public void setWsDataClusterPK(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK) {
        this.wsDataClusterPK = wsDataClusterPK;
    }
    
    public java.lang.String getConceptName() {
        return conceptName;
    }
    
    public void setConceptName(java.lang.String conceptName) {
        this.conceptName = conceptName;
    }
    
    public com.amalto.core.webservice.WSWhereItem getWsWhereItem() {
        return wsWhereItem;
    }
    
    public void setWsWhereItem(com.amalto.core.webservice.WSWhereItem wsWhereItem) {
        this.wsWhereItem = wsWhereItem;
    }
    
    public int getSpellTreshold() {
        return spellTreshold;
    }
    
    public void setSpellTreshold(int spellTreshold) {
        this.spellTreshold = spellTreshold;
    }
}