package de.himbiss.scrawl.model.project;


public class Object extends Node<Object> {

	public Object(String identifier) {
		super(identifier, NodeType.OBJECT);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
