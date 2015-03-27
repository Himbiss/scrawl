package de.himbiss.scrawl.editors;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.Tab;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.gui.MainLayoutController;

public class EditorManager {
	private static Logger logger = LogManager.getLogger(EditorManager.class);

	@Inject
	MainLayoutController mainController;

	private Map<String, Class<IEditor>> editorMap;
	private Set<IEditor> editors;

	public EditorManager() {
		editorMap = new HashMap<>();
		editors = new HashSet<>();
	}

	public boolean openEditor(String editorId, File file) {
		Class<IEditor> editorClass = editorMap.get(editorId);
		logger.log(Level.INFO, "Opening file " + file.getAbsolutePath()
				+ " in editor: " + editorId);
		if (editorClass != null) {
			try {
				IEditor editorInstance = editorClass.newInstance();
				if (editorInstance.validate(file)) {
					if (!isEditorInstanceOpen(editorId, file)) {
						editorInstance.initialize(file);
						editors.add(editorInstance);
						Tab tab = editorInstance.createTab();
						tab.setOnClosed((event) -> {
							editors.remove(editorInstance);
						});
						return mainController.openNewTab(tab);
					} else {
						logger.log(Level.WARN, "Editor with id " + editorId
								+ " and file " + file.getAbsolutePath()
								+ " is already open");
					}
				} else {
					logger.log(Level.WARN, "Editor with id " + editorId
							+ " failed file validation");
					return false;
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				logger.log(Level.ERROR,
						"Error opening file " + file.getAbsolutePath()
								+ " in editor: " + editorId);
			}
		}
		return false;
	}

	private boolean isEditorInstanceOpen(String editorId, File file) {
		return editors.stream().anyMatch( (e) -> {return e.getEditorId().equals(editorId) && e.getFile().equals(file);} );
	}

	@SuppressWarnings("unchecked")
	public void registerEditor(String editorId, Class<?> editorClass) {
		if (editorMap.get(editorId) == null) {
			if (IEditor.class.isAssignableFrom(editorClass)) {
				editorMap.put(editorId, (Class<IEditor>) editorClass);
				logger.log(Level.INFO, "Registered editor with id: " + editorId);
			} else {
				logger.log(Level.WARN, "Could not register editor with id: "
						+ editorId + ", the class does not implement "
						+ IEditor.class.getName());
			}
		} else {
			logger.log(Level.WARN, "Could not register editor with id: "
					+ editorId + ", the editor was already registered");
		}
	}
}
