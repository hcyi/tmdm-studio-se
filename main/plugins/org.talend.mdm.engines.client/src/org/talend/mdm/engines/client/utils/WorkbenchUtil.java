package org.talend.mdm.engines.client.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.RepositoryNode;

public class WorkbenchUtil {

	public static Collection<IEditorPart> getSelectDirtyEditor(
			IStructuredSelection selection, ERepositoryObjectType etype) {
		List<?> nodes = selection.toList();
		Collection<IEditorPart> dirtyEditors = new LinkedList<IEditorPart>();
		for (Object obj : nodes) {
			if (obj instanceof RepositoryNode) {
				RepositoryNode node = (RepositoryNode) obj;
				ERepositoryObjectType type = node.getContentType();
				if (etype.equals(type)) {
					IEditorPart editor = getRepositoryEditor(node);
					if (null != editor) {
						dirtyEditors.add(editor);
					}
				}
			}
		}
		return dirtyEditors;
	}

	public static IEditorPart getRepositoryEditor(RepositoryNode node) {
		IEditorPart[] parts = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getDirtyEditors();
		for (IEditorPart part : parts) {
			IEditorInput inputObj = part.getEditorInput();
			if (inputObj instanceof RepositoryEditorInput) {
				RepositoryEditorInput input = (RepositoryEditorInput) inputObj;
				if (equals(input.getRepositoryNode(), node)) {
					return part;
				}
			}
		}
		return null;
	}

	static boolean equals(RepositoryNode node1, RepositoryNode node2) {
		if (null == node1 || null == node2) {
			return false;
		}
		if(null == node1.getId() || !node1.getId().equals(node2.getId())){
			return false;
		}
		return true;
	}
}
