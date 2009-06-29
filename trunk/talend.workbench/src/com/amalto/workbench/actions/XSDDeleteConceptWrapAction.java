package com.amalto.workbench.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDIdentityConstraintCategory;
import org.eclipse.xsd.XSDIdentityConstraintDefinition;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDXPathDefinition;
import org.eclipse.xsd.XSDXPathVariety;

import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.providers.XSDTreeContentProvider;
import com.amalto.workbench.utils.EImage;
import com.amalto.workbench.utils.ImageCache;

public class XSDDeleteConceptWrapAction extends Action{

	private DataModelMainPage page = null;
	private TreeViewer viewer = null;
	private XSDDeleteConceptAction deleteConceptAction = null;
	
	private List<XSDConcreteComponent> delObjs = new ArrayList<XSDConcreteComponent>();
	private Map<Class, UndoAction> clsAction = new HashMap<Class, UndoAction>();
	
	public XSDDeleteConceptWrapAction(DataModelMainPage page) {
		super();
		this.page = page;
		viewer = page.getTreeViewer();
		setImageDescriptor(ImageCache.getImage(EImage.DELETE_OBJ.getPath()));

		
	}
	
	public void regisDelAction(Class cls, UndoAction action)
	{
		clsAction.put(cls, action);
	}
	
	public void run() {
		try {
			
			IStructuredSelection selection = (IStructuredSelection)page.getTreeViewer().getSelection();
			if(delObjs.isEmpty()){
				return;
			}else{
				boolean sameType = checkInSameClassType(delObjs.toArray(), delObjs.get(0).getClass());
				String deleteLabel = "Are you sure you want to ";
				String elemDesc = ((Action)clsAction.get(delObjs.get(0).getClass())).getText();
				int backPos = elemDesc.indexOf(" ");

				if (delObjs.size() > 1)
					deleteLabel += elemDesc.substring(0, backPos)
							+ " these "
							+ delObjs.size() + " "
							+ (!sameType ? "Objects" : elemDesc
									.substring(backPos + 1)
									+ "s");
				else
					deleteLabel += elemDesc.substring(0, backPos)
							+ " the selected "
							+ (!sameType ? "Objects" : elemDesc
									.substring(backPos + 1));


				if (!MessageDialog.openConfirm(page.getSite().getShell(), "Delete Model", deleteLabel))
					return;
			}
			
			for (Iterator iterator = delObjs.iterator(); iterator.hasNext();) {
				Object toDel = (Object) iterator.next();
				UndoAction delExecute = null;
				boolean isElem = true;
				if (toDel instanceof XSDElementDeclaration) {
					XSDElementDeclaration decl = (XSDElementDeclaration) toDel;

					EList l = decl.getIdentityConstraintDefinitions();
					for (Iterator iter = l.iterator(); iter.hasNext();) {
						XSDIdentityConstraintDefinition icd = (XSDIdentityConstraintDefinition) iter
								.next();
						if (icd.getIdentityConstraintCategory().equals(
								XSDIdentityConstraintCategory.UNIQUE_LITERAL)) {
							isElem = false;
							break;
						}
					}
				}
				if (toDel instanceof XSDXPathDefinition)
				{
					XSDXPathDefinition xpath = (XSDXPathDefinition) toDel;
					if (!xpath.getVariety().equals(XSDXPathVariety.FIELD_LITERAL))
						continue;
				}
				
				delExecute = clsAction.get(toDel.getClass());
				if (isElem && toDel instanceof XSDElementDeclaration) {
					delExecute = clsAction.get(null);
				}
				delExecute.run(toDel);
			}
       
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(
					page.getSite().getShell(),
					"Error", 
					"An error occured trying to remove Concept: "+e.getLocalizedMessage()
			);
		}		
	}
	
	private void wrapDelObj(Object[] toDels) {
		clearDelData();
		for (Object del: toDels)
			delObjs.add((XSDConcreteComponent)del);
		if (delObjs != null)
		{
			refreshAction();
		}
	}


    /**
     * author: Fliu
	 * it is meant to support multiple deletions on data modules on key press
     * @param selections: tree node picking up in the data module view
     */
	public void prepareToDelSelectedItems(IStructuredSelection selections, TreeViewer treeView) {
		viewer = treeView;
		Object[] objs = selections.toArray();
		objs = filterSelectedItemsToDel(selections);
		wrapDelObj(objs);
	}
	/**
	 * Author: Fliu
	 * this fun is to filter out all the children listed in the selections, 
	 * all left is the top parent level ones in the selections
	 * @param selections
	 * @return all parent array with no corresponding children in the selection list
	 */
	private Object[] filterSelectedItemsToDel(IStructuredSelection selections) {
		Object[] objs = selections.toArray();
		List lst = new ArrayList();

		for (Object obj : objs) {
			for (Object objOther : objs) {
				if (obj == objOther) {
					continue;
				}
				Object[] offsprings = populateAllOffspring(objOther,
						new ArrayList());
				for (Object offspring : offsprings) {
					if (offspring == obj) {
						lst.add(obj);
					}
				}
			}
		}

		for (Object ca : objs) {
			if (lst.indexOf(ca) >= 0) {
				lst.remove(ca);
			} else {
				lst.add(ca);
			}
		}
		return lst.toArray();
	}
	
	/**
	 * Author: Fliu
	 * this fun is to populate all offsprings for a specific object
	 */
	private Object[] populateAllOffspring(Object obj, ArrayList offspringList) {
		XSDTreeContentProvider provider = (XSDTreeContentProvider) viewer
				.getContentProvider();
		Object[] offersprings = provider.getChildren(obj);

		for (Object subObj : offersprings) {
			if (!offspringList.contains(subObj))
			{
				offspringList.add(subObj);
				if (provider.hasChildren(subObj)) {
					populateAllOffspring(subObj, offspringList);
				}
			}
			else
			{
				continue;
			}

		}
		return offspringList.toArray();
	}
	
	private void refreshAction()
	{
		XSDConcreteComponent comp = (XSDConcreteComponent)delObjs.get(0);
		if (checkInSameClassType(delObjs.toArray(), comp.getClass()))
			setText(clsAction.get(comp.getClass()).getText() + (delObjs.size() > 1 ? "s" : ""));
		else
			setText("Delete Objects");
		setToolTipText("Delete Business Concepts");
	}
	
	private void clearDelData() {
		delObjs.clear();
	}
	
	private boolean checkInSameClassType(Object[] selects, Class type) {
		for (Object obj : selects) {
			if (obj.getClass() != type) {
				return false;
			}
		}

		if (selects[0] instanceof XSDElementDeclaration) {
			boolean isConcept = checkConcept((XSDElementDeclaration) selects[0]);
			for (Object obj : selects) {
				if (checkConcept((XSDElementDeclaration) obj) != isConcept) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkInDeletableType(Object[] selects)
	{
		clearDelData();
		
		for (Object obj : selects) {
			if (obj instanceof XSDElementDeclaration
					|| obj instanceof XSDParticle
					|| obj instanceof XSDIdentityConstraintDefinition) {
				continue;
			}
			else if (obj instanceof XSDXPathDefinition) {
				XSDXPathDefinition xpath = (XSDXPathDefinition) obj;
				if (xpath.getVariety().equals(XSDXPathVariety.FIELD_LITERAL))
					continue;
				else
					return false;
			}
			else
				return false;
		}
		return true;
	}
	
	public boolean checkConcept(XSDElementDeclaration decl) {
		boolean isConcept = false;
		EList l = decl.getIdentityConstraintDefinitions();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			XSDIdentityConstraintDefinition icd = (XSDIdentityConstraintDefinition) iter
					.next();
			if (icd.getIdentityConstraintCategory().equals(
					XSDIdentityConstraintCategory.UNIQUE_LITERAL)) {
				isConcept = true;
				break;
			}
		}
		return isConcept;
	}
	
	public Action outPutDeleteActions() {
		if (delObjs.size() == 0)
			return null;

		return this;
	}
	
	public void runWithEvent(Event event) {
		super.runWithEvent(event);
	}
	

}