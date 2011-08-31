// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.repository.ui.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.model.mdmserverobject.MDMServerObject;
import org.talend.mdm.repository.utils.Bean2EObjUtil;
import org.talend.repository.model.IProxyRepositoryFactory;

import com.amalto.workbench.editors.AFormPage;
import com.amalto.workbench.editors.DataClusterMainPage;
import com.amalto.workbench.editors.JobMainPage;
import com.amalto.workbench.editors.MenuMainPage;
import com.amalto.workbench.editors.XObjectEditor;
import com.amalto.workbench.models.TreeObject;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class XObjectEditor2 extends XObjectEditor {

    static Logger log = Logger.getLogger(XObjectEditor2.class);

    public static final String EDITOR_ID = "org.talend.mdm.repository.ui.editors.XObjectEditor2"; //$NON-NLS-1$

    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    @Override
    public void doSave(IProgressMonitor monitor) {

        this.saveInProgress = true;
        // For the XMLEditor(the schema editor for the data model),it should be saved and then just refresh the data
        // model page and do nothing else if there are some changes.
        if (xmlEditor != null && this.getCurrentPage() == 1) {
            xmlEditor.doSave(monitor);
            ((AFormPage) (formPages.get(0))).refreshPage();
            return;
        }
        int numPages = formPages.size();
        monitor.beginTask(Messages.bind(Messages.XObjectEditor2_saving, this.getEditorInput().getName()), numPages + 1);
        for (int i = 0; i < numPages; i++) {
            if ((formPages.get(i)) instanceof AFormPage) {
                if (!((AFormPage) (formPages.get(i))).beforeDoSave())
                    return;
            }
            (formPages.get(i)).doSave(monitor);
            monitor.worked(1);
            if (monitor.isCanceled()) {
                this.saveInProgress = false;
                return;
            }
        }
        // if(xmlEditor!=null)xmlEditor.doSave(monitor);
        // perform the actual save
        boolean saved = saveResourceToRepository();
        if (xmlEditor != null && saved) {
            xmlEditor.refresh();
        }
        monitor.done();
    }

    private boolean saveResourceToRepository() {
        XObjectEditorInput2 editorInput = (XObjectEditorInput2) this.getEditorInput();
        TreeObject xobject = (TreeObject) editorInput.getModel();
        MDMServerObjectItem serverObjectItem = (MDMServerObjectItem) editorInput.getInputItem();
        MDMServerObject serverObject = serverObjectItem.getMDMServerObject();
        EObject eObj = Bean2EObjUtil.getInstance().convertFromBean2EObj(xobject.getWsObject(), serverObject);
        if (eObj != null) {
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            try {
                factory.save(serverObjectItem);
                // TODO should call the following,but the page in editor has many call to remote webService ,it will
                // search ServerRoot which cause a NPE
                // xobject.fireEvent(IXObjectModelListener.SAVE, xobject.getParent(), xobject);
                editorDirtyStateChanged();
                return true;
            } catch (PersistenceException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    protected void addPageForXObject(TreeObject xobject) {
        try {
            switch (xobject.getType()) {
            case TreeObject.DATA_MODEL:
                // addPage(new DataModelMainPage(this));
                //
                // // addPage(new DataModelEditorPage(this));
                // WSDataModel wsObject = (WSDataModel) (xobject.getWsObject());
                // Document doc = new Document(Util.formatXsdSource(wsObject.getXsdSchema()));
                // xmlEditor = new XMLEditor(this, xobject);
                // addPage(xmlEditor, new XMLEditorInput(doc));
                // this.setPageText(1, "Schema");

                break;

            case TreeObject.INBOUND_PLUGIN:
                break;
            case TreeObject.OUTBOUND_PLUGIN:
                break;
            case TreeObject.VIEW:
                addPage(new ViewMainPage2(this));
                break;
            case TreeObject.DATA_CLUSTER:
                addPage(new DataClusterMainPage(this));
                break;
            case TreeObject.STORED_PROCEDURE:
                addPage(new StoredProcedureMainPage2(this));
                break;

            case TreeObject.MENU:
                addPage(new MenuMainPage(this));
                break;
            case TreeObject.SERVICE_CONFIGURATION:
                addPage(new MDMServiceConfigrationMainPage(this));
                break;
            /*
             * case TreeObject.RESOURCES: case TreeObject.DATA_MODEL_RESOURCE: case
             * TreeObject.DATA_MODEL_TYPES_RESOURCE: case TreeObject.CUSTOM_TYPES_RESOURCE: case
             * TreeObject.PICTURES_RESOURCE: addPage(new ResourceMainPage(this)); break;
             */
            case TreeObject.CUSTOM_TYPE:
                // addPage(new CustomTypeMainPage(this));
                break;
            case TreeObject.ROUTING_RULE:
                addPage(new RoutingRuleMainPage2(this));
                break;
            case TreeObject.TRANSFORMER:
                addPage(new TransformerMainPage2(this));
                break;
            case TreeObject.JOB:
                addPage(new JobMainPage(this));
                break;
            case TreeObject.UNIVERSE:
                addPage(new UniverseVersionMainPage(this));
                break;

            case TreeObject.ROLE:
                addPage(new MDMRoleMainPage(this));
                break;

            case TreeObject.SYNCHRONIZATIONPLAN:
                try {
                    addPage(new MDMSynchronizationMainPage(this));
                } catch (PartInitException e) {
                    log.error(e.getMessage(), e);
                }
                break;
            default:
                // MessageDialog.openError(this.getSite().getShell(), "Error",
                // "Unknown "+IConstants.TALEND+" Object Type: "+xobject.getType());
                return;
            }// switch

        } catch (PartInitException e) {
            log.error(e.getMessage(), e);
            MessageDialog.openError(this.getSite().getShell(), Messages.Common_Error,
                    Messages.bind(Messages.XObjectEditor2_unableOpenEditor, e.getLocalizedMessage()));
        }
    }

    @Override
    public boolean isLocalInput() {
        return true;
    }

}
