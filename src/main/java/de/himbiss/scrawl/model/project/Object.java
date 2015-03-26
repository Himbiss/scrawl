package de.himbiss.scrawl.model.project;


public class Object extends Node<Object> {

	private static final long serialVersionUID = -1248837597572107378L;

	public Object(String identifier) {
		super(identifier, NodeType.OBJECT);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
