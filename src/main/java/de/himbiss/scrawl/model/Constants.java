package de.himbiss.scrawl.model;

import javafx.scene.input.DataFormat;

public class Constants {
	public static final String MANUSCRIPT_EDITOR = "manuscript_editor";
	
	public static final String PROJECT_LOCATION = "project_location";

	public static final String USER_HOME = "user.home";
	
	// constants for menu items
	public static final String NEW_ITEM = "New";
	public static final String COPY_ITEM = "Copy";
	public static final String PASTE_ITEM = "Paste";
	public static final String DELETE_ITEM = "Delete";
	
	// clipboard data formats
	public static final DataFormat PERSON_FORMAT = new DataFormat("obj/person");
	public static final DataFormat LOCATION_FORMAT = new DataFormat("obj/location");
	public static final DataFormat SCENE_FORMAT = new DataFormat("obj/scene");
	public static final DataFormat OBJECT_FORMAT = new DataFormat("obj/object");
	
	// node types
	public static final String FOLDER_NODE = "Folder";
	public static final String LOCATION_NODE = "Location";
	public static final String SCENE_NODE = "Scene";
	public static final String OBJECT_NODE = "Object";
	public static final String PERSON_NODE = "Person";
	public static final String PROJECT_NODE = "Project";
}
