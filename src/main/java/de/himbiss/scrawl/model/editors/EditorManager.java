package de.himbiss.scrawl.model.editors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Tab;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.view.MainLayoutController;

public class EditorManager {
	private static Logger logger = LogManager.getLogger(EditorManager.class);
	
	@Inject 
	MainLayoutController mainController;

	private Map<String,Class<IEditor>> editorMap;
	
	public EditorManager() {
		editorMap = new HashMap<>();
	}
	
	public boolean openEditor(String editorId, File file) {
		Class<IEditor> editorClass = editorMap.get(editorId);
		logger.log(Level.INFO, "Opening file " + file.getAbsolutePath() + " in editor: " + editorId);
		if(editorClass != null) {
			try {
				IEditor editorInstance = editorClass.newInstance();
				Tab tab = editorInstance.createTab(file);
				mainController.openNewTab(tab);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				logger.log(Level.ERROR, "Error opening file " + file.getAbsolutePath() + " in editor: " + editorId);
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void registerEditor(String editorId, Class<?> editorClass) {
		if(editorMap.get(editorId) == null) {
			if(IEditor.class.isAssignableFrom(editorClass)) {
				editorMap.put(editorId, (Class<IEditor>) editorClass);
				logger.log(Level.INFO, "Registered editor with id: " + editorId);
			} else {
				logger.log(Level.WARN, "Could not register editor with id: " + editorId + ", the class does not implement " + IEditor.class.getName());
			}
		} else {
			logger.log(Level.WARN, "Could not register editor with id: " + editorId + ", the editor was already registered");
		}
	}
}
