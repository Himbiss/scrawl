package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "scene")
@XmlRootElement
public class Scene extends Node<Scene> {

	private static final long serialVersionUID = 1275773246226859464L;

	public Scene() {
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