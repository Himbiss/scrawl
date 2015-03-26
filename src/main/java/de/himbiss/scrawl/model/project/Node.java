package de.himbiss.scrawl.model.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public abstract class Node<T> {

	private StringProperty description;
	private StringProperty identifier;
	
	private Folder<T> parent;

	private NodeType nodeType;
	
	public Node(String identifier, NodeType nodeType) {
		this.identifier = new SimpleStringProperty(identifier);
		this.description = new SimpleStringProperty();
		this.nodeType = nodeType;
	}
	
	public StringProperty getDescription() {
		return description;
	}
	
	public void setDescription(StringProperty description) {
		this.description = description;
	}
	
	public StringProperty getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(StringProperty identifier) {
		this.identifier = identifier;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}
	
	public abstract boolean isFolder();
	
	@Override
	public String toString() {
		return getIdentifier().getValue();
	}

	public Folder<T> getParent() {
		return parent;
	}
	
	public void setParent(Folder<T> parent) {
		this.parent = parent;
	}
}
