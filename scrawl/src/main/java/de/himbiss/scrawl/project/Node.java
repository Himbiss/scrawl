package de.himbiss.scrawl.project;

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
public abstract class Node {

	private static final long serialVersionUID = -748448656969685107L;
	
	private String identifier;
	private String description;
	private boolean isDeletable;
	private boolean isExpanded;
	
	
	private List<Consumer<Node>> observers;
	
	private Folder parent;

	private ContentType nodeType;
	
	public Node() {
		// for jaxb / serialization
	}
	
	public Node(String identifier, boolean isExpanded, boolean isDeletable) {
		this.identifier = identifier;
		this.description = "";
		this.observers = new ArrayList<Consumer<Node>>();
		this.isExpanded = isExpanded;
		this.isDeletable = isDeletable;
	}
	
	public void registerObserver(Consumer<Node> observer) {
		observers.add(observer);
	}
	
	public void notifyObservers() {
		observers.forEach( (o) -> {o.accept(this);} );
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		notifyObservers();
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

	@XmlAttribute(name = "name")
	public String getIdentifier() {
		return identifier;
	}

	@XmlAttribute
	public ContentType getNodeType() {
		return nodeType;
	}

	public void setNodeType(ContentType nodeType) {
		this.nodeType = nodeType;
	}

	@XmlAttribute(name = "is_folder")
	public abstract boolean isFolder();
	
	@Override
	public String toString() {
		return getIdentifier();
	}

	@XmlTransient
	public Folder getParent() {
		return parent;
	}
	
	public void setParent(Folder parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean equals(java.lang.Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof Node) {
			Node node = (Node) obj;
			return node.getNodeType().equals(getNodeType()) && node.getIdentifier().equals(getIdentifier());
		}
		return super.equals(obj);
	}
}
