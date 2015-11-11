package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.project.content.IContent;
import de.himbiss.scrawl.util.Constants;

@XmlType(name = "scene")
@XmlRootElement(name = "scene")
public class Scene implements IContent {

	private static final long serialVersionUID = 1275773246226859464L;

	public Scene() {
		super(Constants.NEW_SCENE, ContentType.SCENE);
		// for jaxb / serialization
	}
	
	public Scene(String identifier) {
		super(identifier, ContentType.SCENE);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}