package de.himbiss.scrawl.model.project;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Folder <T> extends Node<T> {

	private ObservableList<Node<T>> components;
	
	public Folder(String identifier) {
		super(identifier, NodeType.FOLDER);
		components = FXCollections.observableArrayList();
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
