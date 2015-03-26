package de.himbiss.scrawl.model.project;



public class Scene extends Node<Scene> {

	public Scene(String identifier) {
		super(identifier, NodeType.SCENE);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}