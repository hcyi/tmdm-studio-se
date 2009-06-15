package com.amalto.workbench.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class FileSelectWidget {
	FormToolkit factory=new WidgetFactory();
	
	Composite parent;

	private Text text;
	
	String[] fileExtents;
	String label;

	public FileSelectWidget(Composite parent,String label, String[] fileExtents){
		this.label=label;
		
		this.fileExtents=fileExtents;
		this.parent=factory.createComposite(parent);
		GridLayout layout=new GridLayout();
		layout.numColumns=3;
		this.parent.setLayout(layout);
		this.parent.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
		
		create();
	}
	
	public String getFileName(){
		return text.getText();
	}
	
	private void create(){
		GridData gd=new GridData(SWT.LEFT,SWT.CENTER,true,true,1,1);
		Label label=factory.createLabel(parent, this.label,SWT.BORDER);
		label.setLayoutData(gd);
		text=factory.createText(parent, "",SWT.READ_ONLY|SWT.BORDER);
		gd=new GridData(SWT.FILL,SWT.FILL,true,true,1,1);
		
		text.setLayoutData(gd);
				
		Button button=factory.createButton(parent, "...", SWT.PUSH);
		gd=new GridData(SWT.LEFT,SWT.FILL,false,true,1,1);
		button.setLayoutData(gd);
		button.setToolTipText("Select one file path...");
		
		button.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
			
				FileDialog fileDialog = new FileDialog (parent.getShell(), SWT.OPEN);				
				fileDialog.setFilterExtensions (fileExtents);		
				String name=fileDialog.open();
				if(name!=null)
				text.setText(name);
			}			
		});
	}
}