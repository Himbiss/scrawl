package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "scene")
@XmlRootElement(name = "scene")
public class Scene extends Node<Scene> {

	private static final long serialVersionUID = 1275773246226859464L;

	public Scene() {
		super(Constants.NEW_SCENE, NodeType.SCENE);
		// for jaxb / serialization
	}
	
	public Scene(String identifier) {
		super(identifier, NodeType.SCENE);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}