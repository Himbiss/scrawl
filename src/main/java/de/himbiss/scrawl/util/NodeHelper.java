package de.himbiss.scrawl.util;

import javafx.scene.input.DataFormat;
import de.himbiss.scrawl.model.project.Folder;
import de.himbiss.scrawl.model.project.Node;
import de.himbiss.scrawl.model.project.NodeType;

public class NodeHelper {
	// clipboard data formats
	public static final DataFormat PERSON_FORMAT = new DataFormat("obj/person");
	public static final DataFormat LOCATION_FORMAT = new DataFormat("obj/location");
	public static final DataFormat SCENE_FORMAT = new DataFormat("obj/scene");
	public static final DataFormat OBJECT_FORMAT = new DataFormat("obj/object");
	public static final DataFormat FOLDER_FORMAT = new DataFormat("obj/folder");

	public static DataFormat getDataFormat(NodeType type) {
		switch(type) {
		case PERSON:
			return PERSON_FORMAT;
		case LOCATION:
			return LOCATION_FORMAT;
		case SCENE:
			return SCENE_FORMAT;
		case OBJECT:
			return OBJECT_FORMAT;
		case FOLDER:
			return FOLDER_FORMAT;
		default:
			return null;
		}
	}
	
	public static <T> Folder<T> getNextFolder(Node<T> node) {
		if(node.isFolder())
			return (Folder<T>)node;
		return node.getParent();
	}
}
