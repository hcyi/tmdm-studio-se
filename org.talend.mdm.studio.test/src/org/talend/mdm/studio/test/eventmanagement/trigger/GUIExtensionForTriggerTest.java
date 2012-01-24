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
package org.talend.mdm.studio.test.eventmanagement.trigger;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.mdm.studio.test.TalendSWTBotForMDM;

/**
 * DOC rhou class global comment. Detailled comment
 */
public class GUIExtensionForTriggerTest extends TalendSWTBotForMDM {
	private SWTBotTreeItem triggerParentNode;

	private SWTBotTreeItem eventManagementItem;

	@Before
	public void runBeforeEveryTest() {
		eventManagementItem = serverItem.getNode("Event Management");
		eventManagementItem.expand();
		initTrigger();

	}

	private void initTrigger() {
		triggerParentNode = eventManagementItem.getNode("Trigger [HEAD]");
		triggerParentNode.contextMenu("New").click();
		bot.text().setText("TriggerDemo");
		bot.button("OK").click();
	}

	@After
	public void runAfterEveryTest() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				bot.activeEditor().save();
			}
		});
		SWTBotMenu deleteMenu = triggerParentNode.getNode("TriggerDemo")
				.contextMenu("Delete");
		deleteMenu.click();
		sleep();
		bot.button("OK").click();
	}

	// new feature in 4.2,see bug 0017974:
	@Test
	public void classProcessTest() {
		bot.comboBoxWithLabel("Service JNDI Name").setSelection("callprocess");
		String process = bot.tree().select(1).getText();
		sleep(2);
		bot.tabItem(1).activate();
		// Assert.assertEquals("process=" + process,
		// bot.styledText().getText());
		bot.activeEditor().save();
		triggerParentNode.expand();
		sleep();
	}

	// new feature in 4.2,see bug 0017975:
	@Test
	public void smtpTest() {
		bot.comboBoxWithLabel("Service JNDI Name").setSelection("smtp");
		bot.textWithLabel("From").setText("smtp@talend.com");
		bot.textWithLabel("To").setText("smtp@talend.com");
		bot.textWithLabel("CC").setText("");
		bot.textWithLabel("BCC").setText("");
		bot.textWithLabel("Subject").setText("Test");
		bot.textWithLabel("LogFile").setText("");
		sleep();
	}

}
