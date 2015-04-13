package de.himbiss.scrawl.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlSeeAlso({Folder.class, Location.class, Person.class, Object.class, Scene.class})
@XmlRootElement(name = "node")
public abstract class Node<T> implements Serializable {

	private static final long serialVersionUID = -748448656969685107L;
	
	private String description;
	private String identifier;
	private String content;
	
	private List<Consumer<Node<T>>> observers;
	
	private boolean isDeletable;
	private boolean isExpanded;
	
	private Folder<T> parent;

	private NodeType nodeType;
	
	public Node(String identifier, NodeType nodeType) {
		this.identifier =identifier;
		this.description = "";
		this.content = "";
		this.nodeType = nodeType;
		this.observers = new ArrayList<Consumer<Node<T>>>();
		setDeletable(true);
		setExpanded(false);
	}
	
	public Node(String identifier, NodeType nodeType, boolean isDeletable) {
		this.identifier =identifier;
		this.description = "";
		this.content = "";
		this.observers = new ArrayList<Consumer<Node<T>>>();
		this.nodeType = nodeType;
		setDeletable(isDeletable);
		setExpanded(false);
	}
	
	public void registerObserver(Consumer<Node<T>> observer) {
		observers.add(observer);
	}
	
	public void notifyObservers() {
		observers.forEach( (o) -> {o.accept(this);} );
	}
	
	public Node() {
		// for jaxb / serialization
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		notifyObservers();
	}
	
	@XmlAttribute(name = "name")
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
		notifyObservers();
	}

	@XmlAttribute
	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	@XmlAttribute(name = "is_folder")
	public abstract boolean isFolder();
	
	@Override
	public String toString() {
		return getIdentifier();
	}

	@XmlTransient
	public Folder<T> getParent() {
		return parent;
	}
	
	public void setParent(Folder<T> parent) {
		this.parent = parent;
	}

	@XmlAttribute(name = "is_deletable")
	public boolean isDeletable() {
		return isDeletable;
	}

	public void setDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}

	@XmlAttribute(name = "is_expanded")
	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	@XmlElement(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		notifyObservers();
	}
	
	@Override
	public boolean equals(java.lang.Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof Node<?>) {
			Node<?> node = (Node<?>) obj;
			return node.getNodeType().equals(getNodeType()) && node.getIdentifier().equals(getIdentifier());
		}
		return super.equals(obj);
	}
}
