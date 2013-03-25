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
package org.talend.mdm.workbench.serverexplorer.plugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.mdm.workbench.serverexplorer.console.MDMServerMessageConsole;

/**
 * The activator class controls the plug-in life cycle
 */
public class MDMServerExplorerPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.mdm.workbench.serverexplorer"; //$NON-NLS-1$

    // The shared instance
    private static MDMServerExplorerPlugin plugin;

    private Map<String, MDMServerMessageConsole> serverToConsole = null;

    private Map<String, IConsoleView> serverToView = null;

    /**
     * The constructor
     */
    public MDMServerExplorerPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        serverToConsole = new HashMap<String, MDMServerMessageConsole>();
        serverToView = new HashMap<String, IConsoleView>();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (serverToConsole != null) {
            serverToConsole.clear();
            serverToConsole = null;
        }
        if (serverToView != null) {
            serverToView.clear();
            serverToView = null;
        }
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static MDMServerExplorerPlugin getDefault() {
        return plugin;
    }

    public Map<String, MDMServerMessageConsole> getServerToConsole() {
        return this.serverToConsole;
    }

    public Map<String, IConsoleView> getServerToView() {
        return this.serverToView;
    }
}
