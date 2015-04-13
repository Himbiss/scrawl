package de.himbiss.scrawl.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.gui.MainLayoutController;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Person;
import de.himbiss.scrawl.project.Scene;
import de.himbiss.scrawl.util.Constants;

public class EditorManager {
	private static Logger logger = LogManager.getLogger(EditorManager.class);

	@Inject
	MainLayoutController mainController;

	private Map<String, Class<NodeEditor>> editorMap;
	private Set<Pair<NodeEditor, Tab>> editors;
	private Set<NodeEditor> dirtyEditors;

	public EditorManager() {
		editorMap = new HashMap<>();
		editors = new HashSet<>();
		dirtyEditors = new HashSet<>();
	}

	public <T> boolean openEditor(String editorId, Node<T> node) {
		Class<NodeEditor> editorClass = editorMap.get(editorId);
		logger.log(Level.INFO, "Opening node " + node.getIdentifier()
				+ " in editor: " + editorId);
		if (editorClass != null) {
			try {
				NodeEditor editorInstance = editorClass.newInstance();
				if (editorInstance.validate(node)) {
					if (!isEditorInstanceOpen(editorId, node)) {
						editorInstance.setEditorManager(this);
						editorInstance.initialize(node);
						Tab tab = new Tab(node.getIdentifier());
						AnchorPane anchorPane = new AnchorPane();
						editorInstance.createContent(anchorPane);
						tab.setContent(anchorPane);
						tab.setOnClosed((event) -> {
							editors.remove(editorInstance);
						});
						editors.add(new Pair<>(editorInstance,tab));
						return mainController.openNewTab(tab);
					} else {
						logger.log(Level.WARN, "Editor with id " + editorId
								+ " and node " + node.toString()
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
						"Error opening node " + node.toString()
								+ " in editor: " + editorId);
			}
		}
		return false;
	}

	private boolean isEditorInstanceOpen(String editorId, Node<?> node) {
		return editors.stream().anyMatch( (e) -> {return e.getKey().getEditorId().equals(editorId) && e.getKey().getNode().equals(node);} );
	}

	@SuppressWarnings("unchecked")
	public void registerEditor(String editorId, Class<?> editorClass) {
		if (editorMap.get(editorId) == null) {
			if (NodeEditor.class.isAssignableFrom(editorClass)) {
				editorMap.put(editorId, (Class<NodeEditor>) editorClass);
				logger.log(Level.INFO, "Registered editor with id: " + editorId);
			} else {
				logger.log(Level.WARN, "Could not register editor with id: "
						+ editorId + ", the class does not implement "
						+ NodeEditor.class.getName());
			}
		} else {
			logger.log(Level.WARN, "Could not register editor with id: "
					+ editorId + ", the editor was already registered");
		}
	}

	public void openPreferredEditor(Node<?> node) {
		// TODO: better implementation
		if(node instanceof Scene)
			openEditor(Constants.MANUSCRIPT_EDITOR, node);
		else if(node instanceof Person)
			openEditor(Constants.PERSON_EDITOR, node);
	}
	
	public void setDirty(NodeEditor editor) {
		Tab tab = editors.stream().filter( (p) -> {return p.getKey().equals(editor); }).findFirst().get().getValue();
		tab.setText(editor.getNode().getIdentifier() + Constants.IS_DIRTY);
		dirtyEditors.add(editor);
	}
	
	public void setClean(NodeEditor editor) {
		Tab tab = editors.stream().filter( (p) -> {return p.getKey().equals(editor);} ).findFirst().get().getValue();
		tab.setText(editor.getNode().getIdentifier());
		dirtyEditors.remove(editor);
	}

	public void saveAll() {
		List<NodeEditor> toSave = new ArrayList<>(dirtyEditors);
		toSave.stream().forEach( (e) -> { e.save(); });
	}

	public void saveTab(Tab selectedItem) {
		NodeEditor editor = editors.stream().filter( (p) -> { return p.getValue().equals(selectedItem);} ).findFirst().get().getKey();
		if(editor != null) {
			editor.save();
		}
	}
	
}
