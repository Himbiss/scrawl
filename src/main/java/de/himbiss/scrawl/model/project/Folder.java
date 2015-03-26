package de.himbiss.scrawl.model.project;

import java.util.ArrayList;
import java.util.List;

public class Folder <T> extends Node<T> {

	private static final long serialVersionUID = 2807640596345882562L;
	
	private List<Node<T>> components;
	
	public Folder(String identifier) {
		super(identifier, NodeType.FOLDER);
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
	
	public List<Node<T>> getComponents() {
		return components;
	}
	
	@Override
	public boolean isFolder() {
		return true;
	}
}
