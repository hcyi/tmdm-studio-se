/*
 * Created on 27 oct. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.amalto.workbench.editors;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.exolab.castor.xml.Marshaller;

import com.amalto.workbench.dialogs.DOMViewDialog;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.models.IXObjectModelListener;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.providers.XObjectBrowserInput;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.webservices.WSDataClusterPK;
import com.amalto.workbench.webservices.WSDataModelPK;
import com.amalto.workbench.webservices.WSDeleteRoutingOrderV2;
import com.amalto.workbench.webservices.WSExecuteRoutingOrderV2Synchronously;
import com.amalto.workbench.webservices.WSGetRoutingOrderV2SByCriteria;
import com.amalto.workbench.webservices.WSPutItem;
import com.amalto.workbench.webservices.WSRoutingEngineV2Action;
import com.amalto.workbench.webservices.WSRoutingEngineV2ActionCode;
import com.amalto.workbench.webservices.WSRoutingEngineV2Status;
import com.amalto.workbench.webservices.WSRoutingOrderV2;
import com.amalto.workbench.webservices.WSRoutingOrderV2Array;
import com.amalto.workbench.webservices.WSRoutingOrderV2PK;
import com.amalto.workbench.webservices.WSRoutingOrderV2SearchCriteria;
import com.amalto.workbench.webservices.WSRoutingOrderV2Status;
import com.amalto.workbench.webservices.XtentisPort;
import com.amalto.workbench.widgets.CalendarSelectWidget;
import com.amalto.workbench.widgets.WidgetFactory;

public class SubscriptionEngineBrowserMainPage extends AMainPage implements IXObjectModelListener {

//	private boolean refreshing;
	
	protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	protected Label statusLabel;
	
	protected Text fromText;
	protected Text toText;
	protected Combo queueCombo;
	protected Text idText;
	protected Text serviceText;
	protected Text documentTypeText;
	protected TableViewer resultsViewer; 
	protected ListViewer wcListViewer; 
	protected Button suspendButton; 
	
	protected boolean[] ascending = {true,false,false,false}; 
		
    public SubscriptionEngineBrowserMainPage(FormEditor editor) {
        super(
        		editor,
        		SubscriptionEngineBrowserMainPage.class.getName(),
        		((XObjectBrowserInput)editor.getEditorInput()).getName()+" Manager"
        );        
        //listen to events
        ((XObjectBrowserInput)editor.getEditorInput()).addListener(this);
    }

    
    
	@Override
	protected void createFormContent(IManagedForm managedForm) {

        try {
 
        	//sets the title
        	managedForm.getForm().setText(this.getTitle());
        	
        	//get the toolkit
        	FormToolkit toolkit = managedForm.getToolkit();
        	
        	//get the body
        	Composite composite = managedForm.getForm().getBody();
        	composite.setLayout(new GridLayout(1,false));
        	
        	        	
        	//Create a Router status holder
        	Composite statusComposite = toolkit.createComposite(composite);
            statusComposite.setLayoutData(
                    new GridData(SWT.FILL,SWT.FILL,false,true,1,1)
            );
            statusComposite.setLayout(new GridLayout(5,false));
        	
//        	status
            Label descriptionLabel = toolkit.createLabel(statusComposite, "Subscription Engine Status: ", SWT.NULL);
            descriptionLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            statusLabel = toolkit.createLabel(statusComposite, "                                           ", SWT.NULL);
            statusLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            //start/stop/suspend/resume
            Button startButton = toolkit.createButton(statusComposite, "Start", SWT.CENTER);
            startButton.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            startButton.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                	startSubscriptionEngine();
            	};
            });    
            Button stopButton = toolkit.createButton(statusComposite, "Stop", SWT.CENTER);
            stopButton.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            stopButton.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                	stopSubscriptionEngine();
            	};
            });
            suspendButton = toolkit.createButton(statusComposite, "Suspend", SWT.CENTER | SWT.TOGGLE);
            suspendButton.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            suspendButton.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                	if (suspendButton.getSelection()) {
                		suspendSubscriptionEngine();
                		suspendButton.setText("Resume");
                		suspendButton.redraw();
                	} else {
                		resumeSubscriptionEngine();
                		suspendButton.setText("Suspend");
                		suspendButton.redraw();
                	}
            	};
            });    

        	
        	Composite separator = toolkit.createCompositeSeparator(composite);
        	separator.setLayoutData(    
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
        	((GridData)separator.getLayoutData()).heightHint = 2;
        	

        	//first Line of routing Orders
        	Composite firstLineComposite = toolkit.createComposite(composite);
        	firstLineComposite.setLayoutData(
                    new GridData(SWT.FILL,SWT.FILL,false,true,1,1)
            );
        	firstLineComposite.setLayout(new GridLayout(9,false));
        	
//        	Routing Orders Label
            Label routingOrdersLabel = toolkit.createLabel(firstLineComposite, "Routing Orders ", SWT.NULL);
            routingOrdersLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,9,1)
            );
        	        	
        	final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        	       	
        	//from
        	Label fromLabel = toolkit.createLabel(composite, "From", SWT.NULL);
        	fromLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
        	
            CalendarSelectWidget fromCalendar=new CalendarSelectWidget(toolkit,composite,true);
            fromText=fromCalendar.getText();
            //to
            Label toLabel = toolkit.createLabel(composite, "To", SWT.NULL);
            toLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            
            CalendarSelectWidget toCalendar=new CalendarSelectWidget(toolkit,composite,false);
            toText=toCalendar.getText();               
            

         	Label queueLabel = toolkit.createLabel(firstLineComposite, "Queue", SWT.NULL);
            queueLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            queueCombo = new Combo(firstLineComposite,SWT.READ_ONLY |SWT.DROP_DOWN|SWT.MULTI);
            queueCombo.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            queueCombo.addKeyListener(
            		new KeyListener() {
            			public void keyPressed(KeyEvent e) {}
            			public void keyReleased(KeyEvent e) {
        					if ((e.stateMask==0) && (e.character == SWT.CR)) {
        						SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
        					}
            			}//keyReleased
            		}//keyListener
            );
            queueCombo.add("ERRORS");
            queueCombo.add("SCHEDULED");
            queueCombo.add("COMPLETED");
            queueCombo.select(0);
            
            //to
        	Button bSearch = toolkit.createButton(firstLineComposite, "Search", SWT.CENTER);
            bSearch.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            bSearch.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
					SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
            	};
            });    
            
            
        	//Second Line of routing Orders
        	Composite secondLineComposite = toolkit.createComposite(composite);
        	secondLineComposite.setLayoutData(
                    new GridData(SWT.FILL,SWT.FILL,false,true,1,1)
            );
        	secondLineComposite.setLayout(new GridLayout(6,false));
            
        	//ID
        	Label idLabel = toolkit.createLabel(secondLineComposite, "Item IDs", SWT.NULL);
            idLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            idText = toolkit.createText(secondLineComposite, "",SWT.BORDER|SWT.MULTI);
            idText.setLayoutData(    
                    new GridData(SWT.FILL,SWT.FILL,true,true,1,1)
            );
            idText.addKeyListener(
            		new KeyListener() {
            			public void keyPressed(KeyEvent e) {}
            			public void keyReleased(KeyEvent e) {
        					if ((e.stateMask==0) && (e.character == SWT.CR)) {
        						SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
        					}
            			}//keyReleased
            		}//keyListener
            );
        	//document type
        	Label documentTypeLabel = toolkit.createLabel(secondLineComposite, "Document Type", SWT.NULL);
        	documentTypeLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            documentTypeText = toolkit.createText(secondLineComposite, "",SWT.BORDER|SWT.MULTI);
            documentTypeText.setLayoutData(    
                    new GridData(SWT.FILL,SWT.FILL,true,true,1,1)
            );
//         searchText.addModifyListener(this);
            documentTypeText.addKeyListener(
            		new KeyListener() {
            			public void keyPressed(KeyEvent e) {}
            			public void keyReleased(KeyEvent e) {
        					if ((e.stateMask==0) && (e.character == SWT.CR)) {
        						SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
        					}
            			}//keyReleased
            		}//keyListener
            );
        	//service
        	Label serviceLabel = toolkit.createLabel(secondLineComposite, "Service", SWT.NULL);
            serviceLabel.setLayoutData(
                    new GridData(SWT.FILL,SWT.CENTER,false,false,1,1)
            );
            serviceText = toolkit.createText(secondLineComposite, "",SWT.BORDER|SWT.MULTI);
            serviceText.setLayoutData(    
                    new GridData(SWT.FILL,SWT.FILL,true,true,1,1)
            );
//         searchText.addModifyListener(this);
            serviceText.addKeyListener(
            		new KeyListener() {
            			public void keyPressed(KeyEvent e) {}
            			public void keyReleased(KeyEvent e) {
        					if ((e.stateMask==0) && (e.character == SWT.CR)) {
        						SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
        					}
            			}//keyReleased
            		}//keyListener
            );
            
            
            final Table table = createTable(composite);
            
            resultsViewer = new TableViewer(table);
            
            resultsViewer.getControl().setLayoutData(    
                    new GridData(SWT.FILL,SWT.FILL,true,true,1,1)
            );
            ((GridData)resultsViewer.getControl().getLayoutData()).heightHint=500;
            resultsViewer.setContentProvider(new ArrayContentProvider());
            resultsViewer.setLabelProvider(new ClusterTableLabelProvider());
            resultsViewer.addDoubleClickListener(new IDoubleClickListener() {
            	public void doubleClick(DoubleClickEvent event) {
            		resultsViewer.setSelection(event.getSelection());
            		try {
            			new EditItemAction(
            					SubscriptionEngineBrowserMainPage.this.getSite().getShell(),
            					resultsViewer
            			).run();
            		} catch (Exception e) {
            			MessageDialog.openError(
            					SubscriptionEngineBrowserMainPage.this.getSite().getShell(), 
            					"Error", 
            					"Unable to display the element as a tree:\n"+
            					e.getClass().getName()+": "+e.getLocalizedMessage()
            			);
            		}
	            }
            });
               
    		hookContextMenu();
    		
    		managedForm.reflow(true); //nothing will show on the form if not called

            //adapt body add mouse/focus listener for child
    		WidgetFactory factory=new WidgetFactory();
    		factory.adapt(managedForm.getForm().getBody());
    		
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}//createFormContent
	
	
	/**
	 * Create the Table
	 */
	private Table createTable(Composite parent) {
		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		final Table table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1st column 
		final TableColumn column0 = new TableColumn(table, SWT.LEFT, 1);
		final TableColumn column1 = new TableColumn(table, SWT.LEFT, 1);
		final TableColumn column2 = new TableColumn(table, SWT.LEFT, 2);
		final TableColumn column3 = new TableColumn(table, SWT.LEFT, 3);
//		final TableColumn column4 = new TableColumn(table, SWT.LEFT, 4);
		
		column0.setText("Document");
		column0.setWidth(150);
		column0.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[0] = ! ascending[0];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(0,ascending[0]));
				
				
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[0] = ! ascending[0];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(0,ascending[0]));
				table.setSortColumn(column0);
				if(ascending[0])					
					table.setSortDirection(SWT.DOWN);
				else
					table.setSortDirection(SWT.UP);
			}
		});

		// 2nd column
		
		column1.setText("Date");
		column1.setWidth(150);
		// Add listener to column so tasks are sorted by description when clicked 
		column1.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[1] = ! ascending[1];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(1,ascending[1]));
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[1] = ! ascending[1];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(1,ascending[1]));
				table.setSortColumn(column1);
				if(ascending[1])					
					table.setSortDirection(SWT.DOWN);
				else
					table.setSortDirection(SWT.UP);
			}
		});

		
		// 3rd column
	
		column2.setText("Service");
		column2.setWidth(100);
		// Add listener to column so tasks are sorted by description when clicked 
		column2.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[2] = ! ascending[2];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(2,ascending[2]));
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[2] = ! ascending[2];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(2,ascending[2]));
				table.setSortColumn(column2);
				if(ascending[2])					
					table.setSortDirection(SWT.DOWN);
				else
					table.setSortDirection(SWT.UP);
			}
		});


		// 4th column
		
		column3.setText("Message");
		column3.setWidth(300);
		// Add listener to column so tasks are sorted by description when clicked 
		column3.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[3] = ! ascending[3];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(3,ascending[3]));
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				ascending[3] = ! ascending[3];
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setSorter(new TableSorter(3,ascending[3]));
				table.setSortColumn(column3);
				if(ascending[3])					
					table.setSortDirection(SWT.DOWN);
				else
					table.setSortDirection(SWT.UP);
			}
		});


		
		return table;
	}


	
	
	protected void createCharacteristicsContent(FormToolkit toolkit, Composite charComposite) {
		//Everything is implemented in createFormContent
	}//createCharacteristicsContent

	
	protected void refreshData() {
		try {
			
            //if (! this.String xml = (String)((IStructuredSelection)event.getSelection()).getFirstElement();equals(getEditor().getActivePageInstance())) return;
    		XtentisPort port = Util.getPort(getXObject());
            
    		WSRoutingEngineV2Status wsStatus = port.routingEngineV2Action(new WSRoutingEngineV2Action(WSRoutingEngineV2ActionCode.STATUS));
			statusLabel.setText(wsStatus.getValue());
			
    		idText.setFocus();
 
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(), "Error refreshing the page", "Error refreshing the page: "+e.getLocalizedMessage());
		}    	
	}
	
	protected void commit() {
		try {
			//Nothing to do
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(), "Error comtiting the page", "Error comitting the page: "+e.getLocalizedMessage());
		}    	
	}
		

	protected void createActions() {

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				//ViewBrowserMainPage.this.fillContextMenu(manager);
				manager.add(
						new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS)
				);
				manager.appendToGroup(
						IWorkbenchActionConstants.MB_ADDITIONS,
						new EditItemAction(
								SubscriptionEngineBrowserMainPage.this.getSite().getShell(),
								SubscriptionEngineBrowserMainPage.this.resultsViewer
						)
				);
				manager.appendToGroup(
						IWorkbenchActionConstants.MB_ADDITIONS,
						new DeleteItemsAction(
								SubscriptionEngineBrowserMainPage.this.getSite().getShell(),
								SubscriptionEngineBrowserMainPage.this.resultsViewer
						)
				);
				manager.appendToGroup(
						IWorkbenchActionConstants.MB_ADDITIONS,
						new ExecuteRoutingOrdersAction(
								SubscriptionEngineBrowserMainPage.this.getSite().getShell(),
								SubscriptionEngineBrowserMainPage.this.resultsViewer
						)
				);

			}
		});
		Menu menu = menuMgr.createContextMenu(resultsViewer.getControl());
		resultsViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, resultsViewer);
	}

	protected void fillContextMenu(IMenuManager manager) {
//		IStructuredSelection selection=((IStructuredSelection)viewer.getSelection());
		
		return;
	}
	

	
	protected WSRoutingOrderV2[] getResults() {
		
		Cursor waitCursor=null;		
		
		try {
			
			Display display = getEditor().getSite().getPage().getWorkbenchWindow().getWorkbench().getDisplay();
			waitCursor = new Cursor(display,SWT.CURSOR_WAIT);
			this.getSite().getShell().setCursor(waitCursor);
			
	 		XtentisPort port = Util.getPort(getXObject());
   		 
	 		long from = -1;
	 		long to = System.currentTimeMillis();
	 		
	 		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	 		Pattern pattern = Pattern.compile("^\\d{4}\\d{2}\\d{2} \\d{2}:\\d{2}:\\d{2}$");
	 		
	 		if (! "".equals(fromText.getText().trim())) {
	 			String dateTimeText=fromText.getText().trim();
	 			Matcher matcher = pattern.matcher(dateTimeText);
	 			if(!matcher.matches()){
	 				MessageDialog.openWarning(this.getSite().getShell(), "Warning", "Time format is illegal! ");
	 				return new WSRoutingOrderV2[0];
	 			}
	 			
	            try {
	                Date d = sdf.parse(fromText.getText());
	                from = d.getTime();
	            } catch (ParseException pe) {}
	 		}

	 		if (! "".equals(toText.getText().trim())) {
	 			String dateTimeText=toText.getText().trim();
	 			Matcher matcher = pattern.matcher(dateTimeText);
	 			if(!matcher.matches()){
	 				MessageDialog.openWarning(this.getSite().getShell(), "Warning", "Time format is illegal! ");
	 				return new WSRoutingOrderV2[0];
	 			}
	 			
	            try {
	                Date d = sdf.parse(toText.getText());
	                to = d.getTime();
	            } catch (ParseException pe) {}
	 		}
	 		
	 		String serviceJNDI=serviceText.getText().trim();
	 		if ("".equals(serviceJNDI) || "*".equals(serviceJNDI)) {
	 			serviceJNDI = null;
	 		} else if (! serviceJNDI.contains("/")) {
	 			serviceJNDI = "amalto/local/service/"+serviceJNDI;
	 		}

        	 WSRoutingOrderV2Array results = 
	 			port.getRoutingOrderV2SByCriteria(
 					new WSGetRoutingOrderV2SByCriteria(
						new WSRoutingOrderV2SearchCriteria(
							WSRoutingOrderV2Status.COMPLETED, //FIXME: Routing Order Status
							("*".equals(idText.getText()) || "".equals(idText.getText())) ? null : idText.getText(),
							null,
							from,
							to,
							-1L,
							-1L,
							-1L,
							-1L,
							-1L,
							-1L,
							("*".equals(documentTypeText.getText()) || "".equals(documentTypeText.getText())) ? null : documentTypeText.getText(),
							("*".equals(idText.getText()) || "".equals(idText.getText())) ? null : idText.getText(),
							serviceJNDI, //service
							null, //paramters,
							null //message
						)
					)
 				);
        	 
//        	 if (results==null) {
//              	MessageDialog.openInformation(this.getSite().getShell(), "Info", "Sorry, no result. ");
//              	return new WSRoutingOrderV2[0];
//            }
						
	 				 			 		
	 		return results.getWsRoutingOrder();
		} catch (Exception e) {
			e.printStackTrace();
			if (	(e.getLocalizedMessage()!=null) &&
					e.getLocalizedMessage().contains("10000")
				)
				MessageDialog.openError(this.getSite().getShell(), "Too Many Results", "More tha 10000 results returned by the search. \nPlease narrow your search.");
			else
				MessageDialog.openError(this.getSite().getShell(), "Unable to perform the search", e.getLocalizedMessage());
			return null;
		} finally {
			try {
				this.getSite().getShell().setCursor(null);
				waitCursor.dispose();} catch (Exception e) {}
		}
		
	}
	
	
	/*********************************
	 * IXObjectModelListener interface
	 */
	public void handleEvent(int type, TreeObject parent, TreeObject child) {
		refreshData();
	}	
	

	
	/***************************************************************
	 * Edit Item Action
	 * @author bgrieder
	 *
	 ***************************************************************/
	class EditItemAction extends Action{

		protected Shell shell = null;
		protected Viewer viewer;
		
		public EditItemAction(Shell shell, Viewer viewer) {
			super();
			this.shell = shell;
			this.viewer = viewer;
			setImageDescriptor(ImageCache.getImage( "icons/edit_obj.gif"));
			setText("Edit Item");
			setToolTipText("View as a DOM Tree or edit the XML source");
		}
		
		public void run() {
			try {
				super.run();
				
				IStructuredSelection selection=((IStructuredSelection)viewer.getSelection());
				WSRoutingOrderV2 routingOrder = (WSRoutingOrderV2) selection.getFirstElement();
				
				StringWriter sw = new StringWriter();
				Marshaller.marshal(routingOrder, sw);
				
        		final DOMViewDialog d =  new DOMViewDialog(
        				SubscriptionEngineBrowserMainPage.this.getSite().getShell(),
        				Util.parse(sw.toString())
        		);
        		d.addListener(new Listener() {
        			public void handleEvent(Event event) {
        				if (event.button == DOMViewDialog.BUTTON_SAVE) {
        					//attempt to save
        					try {
	        					Util.getPort(getXObject()).putItem(
	        							new WSPutItem(
	        									(WSDataClusterPK)getXObject().getWsKey(),
	        									d.getXML(),
	        									"".equals(d.getDataModelName()) ? null : new WSDataModelPK(d.getDataModelName()),false
	        							)
	        					);
        					}catch (Exception e) {
        						MessageDialog.openError(
        								shell,
        								"Error saving the Item", 
        								"An error occured trying save the Item:\n\n "+e.getLocalizedMessage()
        						);
        						return;
        					}
        				}//if
        				d.close();
        			}//handleEvent
        		});
        		
        		d.setBlockOnOpen(true);
        		d.open();
        		
	       
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(
						shell,
						"Error", 
						"An error occured trying to view the result as a DOM tree: "+e.getLocalizedMessage()
				);
			}		
		}
		public void runWithEvent(Event event) {
			super.runWithEvent(event);
		}

	}
	
	
	
	/***************************************************************
	 * Delete Items Action
	 * @author bgrieder
	 *
	 ***************************************************************/
	class DeleteItemsAction extends Action{

		protected Shell shell = null;
		protected Viewer viewer;
		
		public DeleteItemsAction(Shell shell, Viewer viewer) {
			super();
			this.shell = shell;
			this.viewer = viewer;
			setImageDescriptor(ImageCache.getImage( "icons/delete_obj.gif"));
			IStructuredSelection selection=((IStructuredSelection)viewer.getSelection());
			if (selection.size()==1)
				setText("Delete the selected Routing Order");
			else
				setText("Delete these "+selection.size()+" Routing Orders");
			setToolTipText("Delete the Selected Routing Order"+(selection.size()>1? "s":""));
		}
		
		public void run() {
			try {
				super.run();
								
				//retrieve the list of items
				IStructuredSelection selection=((IStructuredSelection)viewer.getSelection());
				List<WSRoutingOrderV2> lineItems = selection.toList();

				if (lineItems.size()==0) return;
				
				if (! MessageDialog.openConfirm(
						this.shell, 
						"Confirm Deletion", 
						"Are you sure you want to delete the selected "+(lineItems.size()>1 ? lineItems.size()+" " : "")+"Routing Order(s)?")
					)	return;

				//Instantiate the Monitor with actual deletes
				DeleteItemsWithProgress diwp = 
					new DeleteItemsWithProgress(
							getXObject(),
							lineItems,
							this.shell
					);
				//run
				new ProgressMonitorDialog(this.shell).run(
						false,	//fork 
						true, 	//cancelable
						diwp
				);
				//refresh the search
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
	       
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(
						shell,
						"Error", 
						"An error occured trying to delete the Routing Orders: "+e.getLocalizedMessage()
				);
			}		
		}
		public void runWithEvent(Event event) {
			super.runWithEvent(event);
		}
		
		//Progress Monitor that implements the actual delete
		class DeleteItemsWithProgress implements IRunnableWithProgress {
			TreeObject xObject;
			Collection<WSRoutingOrderV2> lineItems;
			Shell parentShell;

			public DeleteItemsWithProgress(TreeObject object, Collection<WSRoutingOrderV2> lineItems, Shell shell) {
				super();
				this.xObject = object;
				this.lineItems = lineItems;
				this.parentShell = shell;
			}

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask("Deleting items", lineItems.size());
					
					XtentisPort port = Util.getPort(getXObject());
					
					int i=0;
					for (Iterator<WSRoutingOrderV2> iter = lineItems.iterator(); iter.hasNext(); ) {
						monitor.subTask("Processing item "+(i++));
						if (monitor.isCanceled())  {
							MessageDialog.openWarning(
									this.parentShell,
									"User Canceled the delete",
									"The deletes were canceled by the user on item "+i+"\n"+
									"Some Items may have not been deleted"
							);
							return;
						}
						WSRoutingOrderV2 lineItem =  iter.next();
						port.deleteRoutingOrderV2(new WSDeleteRoutingOrderV2(new WSRoutingOrderV2PK(lineItem.getName(),lineItem.getStatus())));
						monitor.worked(1);
					}//for
					
					monitor.done();
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(
							shell,
							"Error Deleting", 
							"An error occured trying to delete the Routing Orders:\n\n "+e.getLocalizedMessage()
					);
				}//try				
				
			}//run
		}//class DeleteItemsWithProgress

	}//class DeletItemsAction
	
	
	
	/***************************************************************
	 * Execute Routing Orders
	 * @author bgrieder
	 *
	 ***************************************************************/
	class ExecuteRoutingOrdersAction extends Action{

		protected Shell shell = null;
		protected Viewer viewer;
		
		public ExecuteRoutingOrdersAction(Shell shell, Viewer viewer) {
			super();
			this.shell = shell;
			this.viewer = viewer;
			setImageDescriptor(ImageCache.getImage( "icons/execute.gif"));
			IStructuredSelection selection=((IStructuredSelection)viewer.getSelection());
			if (selection.size()==1)
				setText("Execute the selected Routing Order ");
			else
				setText("Execute the "+selection.size()+" selected Routing Order");
			setToolTipText("Execute the selected Routing Order"+(selection.size()>1? "s":""));
		}
		
		public void run() {
			try {
				super.run();
								
				//retrieve the list of items
				IStructuredSelection selection=((IStructuredSelection)viewer.getSelection());
				List<WSRoutingOrderV2> lineItems = selection.toList();

				if (lineItems.size()==0) return;
				
				if (! MessageDialog.openConfirm(
						this.shell, 
						"Confirm Execution", 
						"Are you sure you want to execute the selected "+lineItems.size()+" Routing Orders?")  
					)	return;

				//Instantiate the Monitor with actual deletes
				ExecuteRoutingOrdersWithProgress diwp = 
					new ExecuteRoutingOrdersWithProgress(
							getXObject(),
							lineItems,
							this.shell
					);
				//run
				new ProgressMonitorDialog(this.shell).run(
						false,	//fork 
						true, 	//cancelable
						diwp
				);
				//refresh the search
				SubscriptionEngineBrowserMainPage.this.resultsViewer.setInput(getResults());
	       
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(
						shell,
						"Error", 
						"An error occured trying to execute the Routing Order(s): "+e.getLocalizedMessage()
				);
			}		
		}
		public void runWithEvent(Event event) {
			super.runWithEvent(event);
		}
		
		//Progress Monitor that implements the actual delete
		class ExecuteRoutingOrdersWithProgress implements IRunnableWithProgress {
			TreeObject xObject;
			Collection<WSRoutingOrderV2> lineItems;
			Shell parentShell;

			public ExecuteRoutingOrdersWithProgress(TreeObject object, Collection<WSRoutingOrderV2> lineItems, Shell shell) {
				super();
				this.xObject = object;
				this.lineItems = lineItems;
				this.parentShell = shell;
			}

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				
				
				monitor.beginTask("Executing Routing Orders", lineItems.size());
				XtentisPort port = null;
				
				try {				
					 port = Util.getPort(getXObject());
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(
							shell,
							"Error Executing", 
							"An error occured trying to execute the Routing Order:\n\n "+e.getLocalizedMessage()
					);
				}//try				

				for (Iterator<WSRoutingOrderV2> iter = lineItems.iterator(); iter.hasNext(); ) {
					
					WSRoutingOrderV2 lineItem = iter.next();
					monitor.subTask("Executing Routing Order "+lineItem.getName());
					
					if (monitor.isCanceled())  {
						MessageDialog.openWarning(
								this.parentShell,
								"User Canceled the Execution",
								"The execution was canceled by the user on Routing Order "+lineItem.getName()+"\n"+
								"Some Routing Orders may have not been executed"
						);
						return;
					}


					try {
   						port.executeRoutingOrderV2Synchronously(new WSExecuteRoutingOrderV2Synchronously(new WSRoutingOrderV2PK(lineItem.getName(), lineItem.getStatus())));
						monitor.worked(1);
					} catch (Exception e) {
						e.printStackTrace();
						MessageDialog.openError(
								shell,
								"Error Executing", 
								"An error occured trying to execute the Routing Order:\n\n "+e.getLocalizedMessage()
						);
					}//try				

				}//for
				
				monitor.done();
				MessageDialog.openInformation(
					shell,
					"Execution Completed", 
					lineItems.size()+" Routing Orders were executed"
				);
				
			}//run
		}//class DeleteItemsWithProgress

	}//class DeletItemsAction
	
	
	
	
	
	/***************************************************************
	 * Table Label Provider
	 * @author bgrieder
	 *
	 ***************************************************************/
	class ClusterTableLabelProvider implements  ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			WSRoutingOrderV2 ro = (WSRoutingOrderV2) element;
			switch (columnIndex) {
			case 0:
				return ro.getWsItemPK().getConceptName()+"["+Util.joinStrings(ro.getWsItemPK().getIds(), ".")+"]";
			case 1:
				return sdf.format(new Date(ro.getTimeCreated()));
			case 2:
				return ro.getServiceJNDI().replaceFirst("amalto/local/service/", "");
			case 3:
				return ro.getMessage();
			default:
				return "???????";
			}
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}
	
	
	/***************************************************************
	 * Table Sorter
	 * @author bgrieder
	 *
	 ***************************************************************/
	class TableSorter extends ViewerSorter {
		
		int column=0;
		boolean asc = true;
				
		public TableSorter(int column, boolean ascending) {
			super();
			this.column = column;
			this.asc = ascending;
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			WSRoutingOrderV2 ro1 = (WSRoutingOrderV2) e1;
			WSRoutingOrderV2 ro2 = (WSRoutingOrderV2) e2;
			
			int res=0;
			
			switch (column) {
				case 0:	//id
					String d1 = ro1.getWsItemPK().getConceptName()+"["+Util.joinStrings(ro1.getWsItemPK().getIds(), ".")+"]";
					String d2 = ro2.getWsItemPK().getConceptName()+"["+Util.joinStrings(ro2.getWsItemPK().getIds(), ".")+"]";
					res= d1.compareToIgnoreCase(d2);
					break;	
				case 1:	//date
					res= (int)( ro1.getTimeCreated() - ro2.getTimeCreated());
					break;
				case 2:	//service
					res = ro1.getServiceJNDI().compareToIgnoreCase(ro2.getServiceJNDI());
					break;
				case 3:	//message
					res = ro1.getMessage().compareToIgnoreCase(ro2.getMessage());
					break;
				default:
					res=0;
			}
			if (asc) 
				return res;
			else
				return -res;
		}
		
		
	}
	
	
	public void startSubscriptionEngine() {
		try {
			XtentisPort port = Util.getPort(getXObject());
			WSRoutingEngineV2Status wsStatus = port.routingEngineV2Action(new WSRoutingEngineV2Action(WSRoutingEngineV2ActionCode.START));
			statusLabel.setText(wsStatus.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(
					this.getSite().getShell(),
					"Error", 
					"An error occured trying to start the router: "+e.getLocalizedMessage()
			);
		}		
	}
	
	public void stopSubscriptionEngine(){
		try {
			XtentisPort port = Util.getPort(getXObject());
			WSRoutingEngineV2Status wsStatus = port.routingEngineV2Action(new WSRoutingEngineV2Action(WSRoutingEngineV2ActionCode.STOP));
			statusLabel.setText(wsStatus.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(
					this.getSite().getShell(),
					"Error", 
					"An error occured trying to susend the router: "+e.getLocalizedMessage()
			);
		}
	}

	public void suspendSubscriptionEngine(){
		try {
			XtentisPort port = Util.getPort(getXObject());
			WSRoutingEngineV2Status wsStatus = port.routingEngineV2Action(new WSRoutingEngineV2Action(WSRoutingEngineV2ActionCode.SUSPEND));
			statusLabel.setText(wsStatus.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(
					this.getSite().getShell(),
					"Error", 
					"An error occured trying to suspend the router: "+e.getLocalizedMessage()
			);
		}
	}

	public void resumeSubscriptionEngine(){
		try {
			XtentisPort port = Util.getPort(getXObject());
			WSRoutingEngineV2Status wsStatus = port.routingEngineV2Action(new WSRoutingEngineV2Action(WSRoutingEngineV2ActionCode.RESUME));
			statusLabel.setText(wsStatus.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(
					this.getSite().getShell(),
					"Error", 
					"An error occured trying to resume the router: "+e.getLocalizedMessage()
			);
		}
	}
	
	



}
