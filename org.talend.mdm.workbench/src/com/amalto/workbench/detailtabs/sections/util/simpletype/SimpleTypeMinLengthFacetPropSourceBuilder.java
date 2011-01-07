package com.amalto.workbench.detailtabs.sections.util.simpletype;

import org.eclipse.xsd.XSDFacet;
import org.eclipse.xsd.XSDSimpleTypeDefinition;

import com.amalto.workbench.utils.IConstants;

public class SimpleTypeMinLengthFacetPropSourceBuilder extends SimpleTypeIntegerFacetPropSourceBuilder {

    @Override
    protected XSDFacet getSourceSimpleTypeFacet(XSDSimpleTypeDefinition simpleType) {
        return simpleType.getMinLengthFacet();
    }

    @Override
    protected String getPropName() {
        return IConstants.SIMPLETYPE_FACETNAME_MINLENGTH;
    }

}
