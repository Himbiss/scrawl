package de.himbiss.scrawl.model.project;


public class Location extends Node<Location> {

	public Location(String identifier) {
		super(identifier, NodeType.LOCATION);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
