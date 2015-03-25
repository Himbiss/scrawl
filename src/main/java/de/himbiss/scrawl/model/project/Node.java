package de.himbiss.scrawl.model.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public abstract class Node<T> {

	private StringProperty description;
	private StringProperty identifier;
	
	public Node(String identifier) {
		this.identifier = new SimpleStringProperty(identifier);
		this.description = new SimpleStringProperty();
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
	
	@Override
	public String toString() {
		return getIdentifier().getValue();
	}
}
