package de.himbiss.scrawl.model.project;


public class Location extends Node<Location> {

	private static final long serialVersionUID = 5158405417755517140L;

	public Location(String identifier) {
		super(identifier, NodeType.LOCATION);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
