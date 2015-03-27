package de.himbiss.scrawl.project;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "folder")
@XmlRootElement(name = "folder")
public class Folder <T> extends Node<T> {

	private static final long serialVersionUID = 2807640596345882562L;
	
	private List<Node<T>> components;
	
	public Folder(String identifier, NodeType nodeType) {
		super(identifier, nodeType);
		components = new ArrayList<Node<T>>();
	}
	
	public Folder(String identifier, NodeType nodeType, boolean isDeletable) {
		super(identifier, nodeType, isDeletable);
		components = new ArrayList<Node<T>>();
	}

	public Folder() {
		super(Constants.NEW_FOLDER, null);
		components = new ArrayList<Node<T>>();
	}

	public void add(Node<T> component) {
		component.setParent(this);
		components.add(component);
	}
	
	public void remove(Node<T> component) {
		component.setParent(null);
		components.remove(component);
	}
	
	@XmlElementWrapper(name = "content")
	@XmlElement
	public List<Node<T>> getComponents() {
		return components;
	}
	
	public void setComponents(List<Node<T>> components) {
		this.components = components;
	}
	
	@Override
	public boolean isFolder() {
		return true;
	}

}
