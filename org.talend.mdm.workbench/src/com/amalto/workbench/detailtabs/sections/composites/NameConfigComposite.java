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
package com.amalto.workbench.detailtabs.sections.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.amalto.workbench.detailtabs.sections.model.INameEditable;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

public class NameConfigComposite extends Composite {

    private INameEditable target;

    private Text txtName;

    private ModifyListener lTxtNameListener;

    private Label lblNameErrIndicator;

    private Image errIcon = ImageCache.getImage(EImage.ERROR.getPath()).createImage();

    public NameConfigComposite(Composite parent, int style) {
        super(parent, style);

        setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 0;
        gridLayout.numColumns = 3;
        setLayout(gridLayout);

        final Label lblName = new Label(this, SWT.NONE);
        lblName.setText("Name");
        lblName.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        lblNameErrIndicator = new Label(this, SWT.NONE);
        lblNameErrIndicator.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        lblNameErrIndicator.setImage(errIcon);
        lblNameErrIndicator.setVisible(false);

        txtName = new Text(this, SWT.BORDER);
        final GridData gd_txtName = new GridData(SWT.FILL, SWT.CENTER, true, false);
        txtName.setLayoutData(gd_txtName);

        initUIListener();

        addUIListener();
    }

    public void setTarget(INameEditable target) {
        this.target = target;

        remvoeUIListener();

        txtName.setText("");
        lblNameErrIndicator.setVisible(false);
        lblNameErrIndicator.setToolTipText("");

        if (target != null) {
            txtName.setText(target.getName());
        }

        addUIListener();
    }

    private void initUIListener() {

        lTxtNameListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {

                if (target == null)
                    return;

                String errMsg = target.validNewName(txtName.getText().trim());

                lblNameErrIndicator.setVisible(errMsg != null);
                lblNameErrIndicator.setToolTipText(errMsg == null ? "" : errMsg);

                target.setName(txtName.getText().trim());
            }
        };
    }

    private void addUIListener() {
        txtName.addModifyListener(lTxtNameListener);
    }

    private void remvoeUIListener() {
        txtName.removeModifyListener(lTxtNameListener);
    }

    @Override
    public void dispose() {
        super.dispose();

        errIcon.dispose();
    }
}
