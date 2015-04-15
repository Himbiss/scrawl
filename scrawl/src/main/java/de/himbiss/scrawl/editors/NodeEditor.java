package de.himbiss.scrawl.editors;

import java.util.Set;

import javafx.scene.layout.AnchorPane;
import de.himbiss.scrawl.project.Node;

public abstract class NodeEditor {
	
	private EditorManager editorManager;
	
	
	protected abstract void initialize(Node<?> content);
	
	protected abstract void createContent(AnchorPane anchorPane);
	
	protected abstract boolean validate(Node<?> nodeType);
	
	protected abstract Node<?> getNode();
	
	public EditorManager getEditorManager() {
		return editorManager;
	}

	public void setEditorManager(EditorManager editorManager) {
		this.editorManager = editorManager;
	}

	public void setDirty() {
		editorManager.setDirty(this);
	}
	
	public void setClean() {
		editorManager.setClean(this);
	}
	
	public abstract Set<Class<? extends Node<?>>> getAssociatedNodes();

	public abstract void save();
}
