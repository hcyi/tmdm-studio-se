package com.amalto.workbench.dialogs;

import java.awt.Panel;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFacet;
import org.eclipse.xsd.XSDIdentityConstraintCategory;
import org.eclipse.xsd.XSDIdentityConstraintDefinition;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTerm;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.models.TreeParent;
import com.amalto.workbench.providers.XSDTreeContentProvider;
import com.amalto.workbench.providers.XSDTreeLabelProvider;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.utils.XtentisException;
import com.amalto.workbench.webservices.WSDataModel;
import com.amalto.workbench.webservices.WSDataModelPK;
import com.amalto.workbench.webservices.WSGetDataModel;
import com.amalto.workbench.webservices.XtentisPort;


public class XpathSelectDialog extends Dialog {
	protected Label schemaLabel = null;
	protected Combo languagesCombo;
	protected TreeViewer domViewer;
	private String title = "";
	protected TreeParent parent;
	protected Combo dataModelCombo ;
	protected TreeObject xobject;
	protected DrillDownAdapter drillDownAdapter;
	protected IWorkbenchPartSite site ;
	protected Panel panel;
	protected Text xpathText;
	public XpathSelectDialog(Shell parentShell,TreeParent parent,String title,IWorkbenchPartSite site) {
		// TODO Auto-generated constructor stub
		super(parentShell);
		this.title = title;
		this.parent = parent;
		this.site = site;
	
	}
	
	private String xpath="";
	
	public String getXpath(){
		return xpath;
	}

	private  String getXpath(StructuredSelection sel){
		getOKButton().setEnabled(false);
		XSDParticle particle = null;
		XSDElementDeclaration xSDElementDeclaration = null;
		
        IStructuredSelection selection = sel;//(IStructuredSelection)domViewer.getSelection();
        
        if(selection.getFirstElement() instanceof XSDElementDeclaration)
        	xSDElementDeclaration  = (XSDElementDeclaration) selection.getFirstElement();
        
        if(selection.getFirstElement() instanceof XSDParticle)
        	particle  = (XSDParticle) selection.getFirstElement();
        
        if(particle==null&& xSDElementDeclaration==null){
        	xpathText.setText("");
        	xpath = "";
        	return xpath;
        }
        XSDTerm term=null;
        if(!(particle==null))
        	term = particle.getTerm();
        String path ="";
        TreeItem item = domViewer.getTree().getSelection()[0];
        do {
        	XSDConcreteComponent component = (XSDConcreteComponent)item.getData();
        	
        	if(component instanceof XSDElementDeclaration){
        		path = ((XSDElementDeclaration)component).getName()+path;
        		 getOKButton().setEnabled(true);
        		return path;
        	}
        	
        	if (component instanceof XSDParticle) {
        		getOKButton().setEnabled(true);
        		if (((XSDParticle)component).getTerm() instanceof XSDElementDeclaration)
        			path = "/"+((XSDElementDeclaration)((XSDParticle)component).getTerm()).getName()+path;
        	} else if (component instanceof XSDElementDeclaration) {
        			path=((XSDElementDeclaration)component).getName()+path;
        	}
        		
        	item = item.getParentItem();
        } while (item!=null);
        
        return path;
	}
		
	protected Control createDialogArea (Composite parent) {
		
		parent.getShell().setText(this.title);
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout)composite.getLayout();
		layout.makeColumnsEqualWidth=false;
		layout.numColumns = 2;
		
		Label datamoelsLabel = new Label(composite, SWT.NONE);
		GridData dg= new GridData(SWT.FILL,SWT.FILL,false,true,1,1);
		datamoelsLabel.setLayoutData(
				dg
		);
		datamoelsLabel.setText("Data Models:");
		
		dg= new GridData(SWT.FILL,SWT.FILL,true,true,2,1);
		dg.widthHint=400;
		dataModelCombo = new Combo(composite,SWT.READ_ONLY |SWT.DROP_DOWN|SWT.SINGLE);
		dataModelCombo.setLayoutData(dg        );

		
		TreeParent grantTreeParent = this.parent.getParent();
		TreeParent tree = grantTreeParent.findServerFolder(TreeObject.DATA_MODEL);
		TreeObject[] trees = tree.getChildren();
		TreeObject treeobject;
		for (int i = 0; i < trees.length; i++) {
			treeobject = trees[i];
			String treeName = treeobject.getDisplayName();
			dataModelCombo.add(treeName);
		}
 
		  final TreeParent pObject =tree;
		  dataModelCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// TODO Auto-generated method stub
				String	modelDisplay = dataModelCombo.getText();
				xobject = pObject.findObject(TreeObject.DATA_MODEL, modelDisplay);
				XtentisPort	port = null;
				try {
						port = Util.getPort(
							new URL(xobject.getEndpointAddress()),
							xobject.getUniverse(),
							xobject.getUsername(),
							xobject.getPassword()
					);
				} catch (MalformedURLException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (XtentisException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
//			  WSDataModel wsObject = (WSDataModel) (xobject.getWsObject());
			  WSDataModel wsDataModel=null;
					try {
						 wsDataModel = port.getDataModel(new WSGetDataModel((WSDataModelPK)xobject.getWsKey()));
					} catch (RemoteException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			  try {
				XSDSchema xsdSchema =getXSDSchema(wsDataModel.getXsdSchema());
				
				provideViwerContent(xsdSchema);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			}
		  });
		  schemaLabel = new Label (composite,SWT.NONE); 
		  schemaLabel.setText("Xpath: ");
		  schemaLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,true,1,1));
		  ((GridData)schemaLabel.getLayoutData()).widthHint=10;
		  xpathText = new Text(composite,SWT.BORDER);
		  xpathText.setEditable(false);
		  xpathText.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		  domViewer = new TreeViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.BORDER);
          domViewer.getControl().setLayoutData(    
                  new GridData(SWT.FILL,SWT.FILL,true,true,2,1)
          );
          ((GridData)domViewer.getControl().getLayoutData()).heightHint=300;
          ((GridData)domViewer.getControl().getLayoutData()).widthHint=200;
         
		return composite;
	}
	

	private XSDSchema getXSDSchema(String schema) throws Exception{
   		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(false);
		InputSource source = new InputSource(new StringReader(schema));
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(source);
		return XSDSchemaImpl.createSchema(document.getDocumentElement());		
	}
	
	
	  protected void provideViwerContent(XSDSchema xsdSchema){
		drillDownAdapter = new DrillDownAdapter(domViewer);
		domViewer.setLabelProvider(new XSDTreeLabelProvider());
		domViewer.setContentProvider(new XSDTreeContentProvider(site, xsdSchema));
		
		domViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			public void selectionChanged(SelectionChangedEvent e) {
				StructuredSelection sel= (StructuredSelection)e.getSelection();
				xpath=getXpath(sel);	
//				schemaLabel.setText("Xpath: "+xpath);
				xpathText.setText(xpath);
			}
			
		});
//		domViewer.expandAll();
		domViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, false, true));
		domViewer.setSorter(new ViewerSorter() {
			public int category(Object element) {
				// we want facets before Base TypeDefinitions in
				// SimpleTypeDefinition
				if (element instanceof XSDFacet)
					return 100;
				// unique keys after element declarations and before other keys
				if (element instanceof XSDIdentityConstraintDefinition) {
					XSDIdentityConstraintDefinition icd = (XSDIdentityConstraintDefinition) element;
					if (icd.getIdentityConstraintCategory().equals(
							XSDIdentityConstraintCategory.UNIQUE_LITERAL))
						return 300;
					else if (icd.getIdentityConstraintCategory().equals(
							XSDIdentityConstraintCategory.KEY_LITERAL))
						return 301;
					else
						return 302;
				}
				return 200;
			}

			public int compare(Viewer theViewer, Object e1, Object e2) {
				int cat1 = category(e1);
				int cat2 = category(e2);
				return cat1 - cat2;
			}
		});
		domViewer.setInput(site);	
	
	  }

}