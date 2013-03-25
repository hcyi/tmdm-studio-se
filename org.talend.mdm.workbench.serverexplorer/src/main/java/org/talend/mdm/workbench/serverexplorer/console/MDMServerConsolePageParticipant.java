package org.talend.mdm.workbench.serverexplorer.console;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

/**
 *
 * created by Karelun Huang on Mar 20, 2013 Detailled comment
 *
 */
public class MDMServerConsolePageParticipant implements IConsolePageParticipant {

    public void init(IPageBookViewPage page, IConsole console) {
        MDMServerMessageConsole mdmConsole = (MDMServerMessageConsole) console;
        IToolBarManager toolBar = page.getSite().getActionBars().getToolBarManager();
        toolBar.appendToGroup(IConsoleConstants.LAUNCH_GROUP, mdmConsole.getReloadAction());
        toolBar.appendToGroup(IConsoleConstants.LAUNCH_GROUP, mdmConsole.getResumeAction());
        toolBar.appendToGroup(IConsoleConstants.LAUNCH_GROUP, mdmConsole.getDownloadAction());
    }

    public void dispose() {
        // TODO Auto-generated method stub

    }

    public void activated() {
        // TODO Auto-generated method stub
    }

    public void deactivated() {
        // TODO Auto-generated method stub
    }

    public Object getAdapter(Class adapter) {
        return null;
    }
}
