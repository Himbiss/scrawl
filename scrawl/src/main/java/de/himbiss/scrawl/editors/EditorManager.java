package de.himbiss.scrawl.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import de.himbiss.scrawl.gui.MainLayoutController;
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

	private Map<String, Class<? extends NodeEditor>> editorMap;
	private Set<Pair<NodeEditor, Tab>> editors;
	private Set<NodeEditor> dirtyEditors;

	public EditorManager() {
		editorMap = new HashMap<>();
		editors = new HashSet<>();
		dirtyEditors = new HashSet<>();
	}
	
	public void initialize() {
		editorService.loadServices();
		editorMap = new HashMap<>(editorService.getEditors());
		registerEditor(ManuscriptEditor.class, Scene.class);
		registerEditor(LocationEditor.class, Location.class);
		registerEditor(ObjectEditor.class, Object.class);
		registerEditor(PersonEditor.class, Person.class);
	}

	public <T> boolean openEditor(Class<? extends NodeEditor> clazz, Node<T> node) {
		return openEditor(clazz.getName(), node);
	}
	
	public <T> boolean openEditor(String editorId, Node<T> node) {
		Class<? extends NodeEditor> editorClass = editorMap.get(editorId);
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

	@SafeVarargs
	@SuppressWarnings( "unchecked" )
	public final void registerEditor(Class<? extends NodeEditor> editorClass, Class<? extends Node<?>>... nodes) {
		if (editorMap.get(editorClass.getName()) == null) {
			if (NodeEditor.class.isAssignableFrom(editorClass)) {
				editorMap.put(editorClass.getName(), (Class<NodeEditor>) editorClass);
				Arrays.asList(nodes).stream().forEach( n -> editorService.associateNodeWithEditor(n, editorClass) );
				logger.log(Level.INFO, "Registered editor with id: " + editorClass.getName());
			} else {
				logger.log(Level.WARN, "Could not register editor with id: "
						+ editorClass.getName() + ", the class does not implement "
						+ NodeEditor.class.getName());
			}
		} else {
			logger.log(Level.WARN, "Could not register editor with id: "
					+ editorClass.getName() + ", the editor was already registered");
		}
	}
	
	public boolean openPreferredEditor(Node<?> node) {
		Class<?> clazz = node.getClass();
		if(editorService.getEditorAssociationMap().containsKey(clazz) && editorService.getEditorAssociationMap().get(clazz).size() > 0) {
			List<Class<? extends NodeEditor>> associatedEditors = editorService.getEditorAssociationMap().get(clazz);
			Class<? extends NodeEditor> preferredEditorClass = associatedEditors.get(0);
			return openEditor(preferredEditorClass, node);
			
		} else {
			logger.log(Level.FATAL, "Tried to open editor but no editor was associated with the node of type " + clazz.getName());
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
