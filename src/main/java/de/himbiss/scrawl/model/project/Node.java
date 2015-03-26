package de.himbiss.scrawl.model.project;

import java.io.Serializable;

public abstract class Node<T> implements Serializable {

	private static final long serialVersionUID = -748448656969685107L;
	
	private String description;
	private String identifier;
	
	private boolean isDeletable;
	private boolean isExpanded;
	

	private Folder<T> parent;

	private NodeType nodeType;
	
	public Node(String identifier, NodeType nodeType) {
		this.identifier =identifier;
		this.description = "";
		this.nodeType = nodeType;
		setDeletable(true);
		setExpanded(false);
	}
	
	public Node(String identifier, NodeType nodeType, boolean isDeletable) {
		this.identifier =identifier;
		this.description = "";
		this.nodeType = nodeType;
		setDeletable(isDeletable);
		setExpanded(false);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}
	
	public abstract boolean isFolder();
	
	@Override
	public String toString() {
		return getIdentifier();
	}

	public Folder<T> getParent() {
		return parent;
	}
	
	public void setParent(Folder<T> parent) {
		this.parent = parent;
	}

	public boolean isDeletable() {
		return isDeletable;
	}

	public void setDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
}
