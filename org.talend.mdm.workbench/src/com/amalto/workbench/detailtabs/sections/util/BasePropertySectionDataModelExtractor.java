// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.workbench.detailtabs.sections.util;

import org.eclipse.xsd.XSDSchema;

import com.amalto.workbench.detailtabs.sections.BasePropertySection;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.models.infoextractor.IAllDataModelHolder;
import com.amalto.workbench.utils.Util;

public class BasePropertySectionDataModelExtractor extends BasePropertySectionGlobalInfoExtractor implements IAllDataModelHolder {

    protected String defaultDataModel = "";

    public BasePropertySectionDataModelExtractor(BasePropertySection propSection) {
        this(propSection, "");
    }

    public BasePropertySectionDataModelExtractor(BasePropertySection propSection, String defaultDataModel) {
        super(propSection, TreeObject.DATA_MODEL);

        this.defaultDataModel = defaultDataModel;
    }

    public String[] getAllDataModelNames() {
        return getGlobalInfos();
    }

    public boolean hasDataModel() {
        return getAllDataModelNames().length > 0;
    }

    public XSDSchema getDataModel(String dataModelName) {
        return Util.getXSDSchema(propSection.getTreeObject(), dataModelName);
    }

    public String getDefaultDataModel() {
        return defaultDataModel;
    }

    public void setDefaultDataModel(String defaultDataModel) {
        this.defaultDataModel = defaultDataModel;
    }
}
