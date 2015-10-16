package de.himbiss.scrawl.editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xbean.finder.ResourceFinder;

import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.util.Constants;

public class EditorService {

	private static Logger logger = LogManager.getLogger(EditorService.class);
	private static EditorService service;
	private Map<Class<? extends Node<?>>,List<Class<? extends NodeEditor>>> editorAssociationMap;
	private Map<String, Class<? extends NodeEditor>> editors;
	
	public EditorService() {
		editorAssociationMap = new HashMap<>();
		editors = new HashMap<>();
	}
	
	public void loadServices() {
		ResourceFinder finder = new ResourceFinder(Constants.SERVICE_LOCATION);
		try {
			logger.log(Level.INFO,"Loading all Editor extensions from \""+Constants.SERVICE_LOCATION+"\"");
			List<Class<? extends NodeEditor>> impls = finder.findAllImplementations(NodeEditor.class);
			for(Class<? extends NodeEditor> editorClass : impls) {
				logger.log(Level.INFO, "Found editor extension: "+editorClass.getSimpleName());
				try {
					NodeEditor editor = editorClass.newInstance();
					editors.put(editorClass.getName(),editorClass);
					
					for(Class<? extends Node<?>> nodeClass : editor.getAssociatedNodes()) {
						associateNodeWithEditor(nodeClass, editorClass);
					}
					
				} catch (InstantiationException e) {
					logger.log(Level.WARN, "Could not instantiate editor extension \""+editorClass.getSimpleName()+"\": "+e.getMessage());
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					logger.log(Level.WARN, "Could not instantiate editor extension \""+editorClass.getSimpleName()+"\": "+e.getMessage());
					e.printStackTrace();
				}
			}
			
		} catch (ClassNotFoundException e) {
			logger.log(Level.ERROR, "The ResourceFinder could not load editor extensions: "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(Level.ERROR, "The ResourceFinder could not load editor extensions: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void associateNodeWithEditor(Class<? extends Node<?>> nodeClass, Class<? extends NodeEditor> editorClass) {
		if(editorAssociationMap.get(nodeClass) == null) {
			editorAssociationMap.put(nodeClass, new ArrayList<>());
		}
		editorAssociationMap.get(nodeClass).add(editorClass);
	}
	
	public static synchronized EditorService getInstance() {
		if(service == null)
			service = new EditorService();
		return service;
	}
	
	public Map<Class<? extends Node<?>>,List<Class<? extends NodeEditor>>> getEditorAssociationMap() {
		return editorAssociationMap;
	}

	public Map<String, Class<? extends NodeEditor>> getEditors() {
		return Collections.unmodifiableMap(editors);
	}
}
