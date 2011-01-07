package com.amalto.workbench.widgets.composites.property;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;

import com.amalto.workbench.providers.CommonTableCellModifier;

public class PropertyModifier extends CommonTableCellModifier<IPropertySource> {

    public static final String COL_PROP_NAME = "name";

    public static final String COL_PROP_VALUE = "value";

    public static final String[] COLPROPS = new String[] { COL_PROP_NAME, COL_PROP_VALUE };

    private ColumnViewer viewer;

    public PropertyModifier(ColumnViewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public boolean canModify(Object element, String property) {

        if (!COL_PROP_VALUE.equals(property))
            return false;

        if (viewer.getCellEditors().length < 2)
            return false;

        boolean canModify = (element instanceof IPropertySource) && (((IPropertySource) element).getCellEditor() != null);

        if (canModify) {
            viewer.getCellEditors()[1] = ((IPropertySource) element).getCellEditor();
            prop2ValueIndexExtractor.put(COL_PROP_VALUE, ((IPropertySource) element).getCellEditorValueExtractor());
            prop2ValueModifier.put(COL_PROP_VALUE, ((IPropertySource) element).getCellEditorValueModifier());
        }

        return canModify;
    }

    @Override
    protected Viewer getViewer() {
        return viewer;
    }

}
