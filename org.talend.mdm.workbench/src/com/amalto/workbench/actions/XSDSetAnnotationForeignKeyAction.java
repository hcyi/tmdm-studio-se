// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.workbench.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDComponent;
import org.w3c.dom.Element;

import com.amalto.workbench.dialogs.SimpleXpathInputDialog;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.utils.XSDAnnotationsStructure;

public class XSDSetAnnotationForeignKeyAction extends UndoAction {

    private static Log log = LogFactory.getLog(XSDSetAnnotationForeignKeyAction.class);

    protected SimpleXpathInputDialog sxid = null;

    protected String dataModelName;

    public XSDSetAnnotationForeignKeyAction(DataModelMainPage page, String dataModelName) {
        super(page);
        setImageDescriptor(ImageCache.getImage(EImage.PRIMARYKEY.getPath()));
        setText("Set the Foreign Key");
        setToolTipText("Set the Foreign Key");
        this.dataModelName = dataModelName;
    }

    public IStatus doAction() {
        try {

            // add by ymli. fix the bug:0010293
            if (page.isDirty()) {
                // MessageDialog.openWarning(page.getSite().getShell(), "Worning", "Please save the Data Model first!");
                boolean save = MessageDialog.openConfirm(page.getSite().getShell(), "Save Resource", "'"
                        + page.getXObject().getDisplayName() + "' has been modified. Save changes?");
                if (save)
                    page.SaveWithForce(new NullProgressMonitor());
                else
                    return Status.CANCEL_STATUS;
            }
            IStructuredSelection selection = (TreeSelection) page.getTreeViewer().getSelection();
            XSDComponent xSDCom = null;
            if (selection.getFirstElement() instanceof Element) {
                TreePath tPath = ((TreeSelection) selection).getPaths()[0];
                for (int i = 0; i < tPath.getSegmentCount(); i++) {
                    if (tPath.getSegment(i) instanceof XSDAnnotation)
                        xSDCom = (XSDAnnotation) (tPath.getSegment(i));
                }
            } else
                xSDCom = (XSDComponent) selection.getFirstElement();
            XSDAnnotationsStructure struc = null;
            if (xSDCom != null)
                struc = new XSDAnnotationsStructure(xSDCom);
            if (struc == null || struc.getAnnotation() == null) {
                throw new RuntimeException("Unable to edit an annotation for object of type " + xSDCom.getClass().getName());
            }

            sxid = getNewSimpleXpathInputDlg(struc.getForeignKey());

            sxid.setBlockOnOpen(true);
            int ret = sxid.open();
            if (ret == Window.CANCEL) {
                return Status.CANCEL_STATUS;
            }

            String fk = "".equals(sxid.getXpath()) ? null : sxid.getXpath().replaceAll("'|\"", "");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            // keep the foreignkey in memory to improve performance
            if (Util.getForeignKeys() != null && fk != null) {
                if (struc.getForeignKey() != null)
                    Util.getForeignKeys().remove(Util.getConceptFromPath(struc.getForeignKey()));
                Util.getForeignKeys().add(Util.getConceptFromPath(fk));
            }
            struc.setForeignKey(fk);
            if (struc.hasChanged()) {
                page.refresh();
                page.getTreeViewer().expandToLevel(xSDCom, 2);
                page.markDirty();
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MessageDialog.openError(page.getSite().getShell(), "Error",
                    "An error occured trying to set a Foreign Key: " + e.getLocalizedMessage());
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    protected SimpleXpathInputDialog getNewSimpleXpathInputDlg(String foreignKey) {
        return new SimpleXpathInputDialog(page, "Set the Foreign Key",
                "Enter an xPath for the Foreign Key - Leave BLANK to delete the Foreign Key", foreignKey, new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        sxid.close();
                    }
                }, dataModelName

        );
    }
    public void runWithEvent(Event event) {
        super.runWithEvent(event);
    }

}
