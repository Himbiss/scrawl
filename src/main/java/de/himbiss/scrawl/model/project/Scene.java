package de.himbiss.scrawl.model.project;



public class Scene extends Node<Scene> {

	private static final long serialVersionUID = 1275773246226859464L;

	public Scene(String identifier) {
		super(identifier, NodeType.SCENE);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}