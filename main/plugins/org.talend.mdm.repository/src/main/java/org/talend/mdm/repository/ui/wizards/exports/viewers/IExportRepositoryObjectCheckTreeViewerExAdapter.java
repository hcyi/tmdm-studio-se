// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.repository.ui.wizards.exports.viewers;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.repository.IRepositoryViewObject;

import com.amalto.workbench.exadapter.IExAdapter;

/**
 * created by HHB on 2014-1-9 Detailled comment
 * 
 */
public interface IExportRepositoryObjectCheckTreeViewerExAdapter extends IExAdapter<ExportRepositoryObjectCheckTreeViewer> {

    void addSortedViewObjects(List<IRepositoryViewObject> sortedViewObjs);

    void appendDependencies(Object[] checkedElements);

    Composite createSelectionButton(Composite itemComposite);

    void createOtherControl(Composite itemComposite);

    void updateCountStatus();
}
