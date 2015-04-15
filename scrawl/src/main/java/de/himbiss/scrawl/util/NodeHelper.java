package de.himbiss.scrawl.util;

import javafx.scene.input.DataFormat;
import de.himbiss.scrawl.project.Folder;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.NodeFactory;
import de.himbiss.scrawl.project.NodeType;

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
		default:
			return null;
		}
	}
	
	public static <T> Folder<T> getNextFolder(Node<T> node) {
		if(node.isFolder())
			return (Folder<T>)node;
		return node.getParent();
	}
	
	public static <T> void fixParent(Node<T> node) {
		if(node.isFolder()) {
			Folder<T> folder = (Folder<T>) node;
			folder.getComponents().stream().forEach( (n) -> { n.setParent(folder); fixParent(n); } );
		}
	}
	
	public static <T> void registerNodes(Node<T> node) {
		if(node.isFolder()) {
			Folder<T> folder = (Folder<T>) node;
			folder.getComponents().stream().forEach( (n) -> { registerNodes(n); } );
		} else {
			NodeFactory.registerNode(node);
		}
	}
}
