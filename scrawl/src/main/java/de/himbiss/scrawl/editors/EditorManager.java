package de.himbiss.scrawl.editors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.editors.locationeditor.LocationEditor;
import de.himbiss.scrawl.editors.manuscripteditor.ManuscriptEditor;
import de.himbiss.scrawl.editors.objecteditor.ObjectEditor;
import de.himbiss.scrawl.editors.personeditor.PersonEditor;
import de.himbiss.scrawl.gui.controller.MainLayoutController;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Object;
import de.himbiss.scrawl.project.Person;
import de.himbiss.scrawl.project.ProjectManager;
import de.himbiss.scrawl.project.Scene;
import de.himbiss.scrawl.util.Constants;

public class EditorManager {
	
	private static Logger logger = LogManager.getLogger(EditorManager.class);

	EditorService editorService = EditorService.getInstance();
	
	@Inject
	MainLayoutController mainController;
	
	@Inject
	ProjectManager projectManager;

	private Set<Pair<NodeEditor, Tab>> editors;
	private Set<NodeEditor> dirtyEditors;

	public EditorManager() {
		editors = new HashSet<>();
		dirtyEditors = new HashSet<>();
	}
	
	public void initialize() {
		editorService.loadServices();
		editorService.registerEditor(ManuscriptEditor.class, Scene.class);
		editorService.registerEditor(LocationEditor.class, Location.class);
		editorService.registerEditor(ObjectEditor.class, Object.class);
		editorService.registerEditor(PersonEditor.class, Person.class);
	}

	public <T> boolean openEditor(Class<? extends NodeEditor> editor, Node<T> node) {
		return openEditor(editor.getName(), node);
	}
	
	public <T> boolean openEditor(String editorId, Node<T> node) {
		Class<? extends NodeEditor> editorClass = editorService.getEditors().get(editorId);
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
							editors = editors.stream().filter(p -> p.getKey() != editorInstance).collect(Collectors.toSet());
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
		return editors.stream().anyMatch( (e) -> {return e.getKey().getClass().getName().equals(editorId) && e.getKey().getNode().equals(node);} );
	}
	
	public boolean openPreferredEditor(Node<?> node) {
		Class<? extends NodeEditor> editor = editorService.getPreferredEditor(node.getClass());
		if(editor != null) {
			return openEditor(editor, node);
		} else {
			logger.log(Level.FATAL, "Tried to open editor but no editor was associated with the node of type " + node.getClass().getName());
			return false;
		}
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
		projectManager.refreshView();
	}

	public void saveTab(Tab selectedItem) {
		NodeEditor editor = editors.stream().filter( (p) -> { return p.getValue().equals(selectedItem);} ).findFirst().get().getKey();
		if(editor != null) {
			editor.save();
			projectManager.refreshView();
		}
	}

	public void closeAllOpenTabs() {
		saveAll();
		editors.stream().forEach( p -> mainController.closeTab(p.getValue()) );
		projectManager.saveProject();
	}
	
}
