package de.himbiss.scrawl.editors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

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
import de.himbiss.scrawl.gui.layout_controller.MainLayoutController;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Object;
import de.himbiss.scrawl.project.Person;
import de.himbiss.scrawl.project.ProjectManager;
import de.himbiss.scrawl.project.Scene;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.Utils;

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
							if(dirtyEditors.contains(editorInstance)) {
								saveTab(tab, e1 -> Utils.closeEditorAskUser(e1));
							}
							editors.removeIf(p -> p.getKey().equals(editorInstance));
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
		Optional<Pair<NodeEditor, Tab>> optional = editors.stream().filter( (p) -> {return p.getKey().equals(editor); }).findFirst();
		if(optional.isPresent()) {
			optional.get().getValue().setText(editor.getNode().getIdentifier() + Constants.IS_DIRTY);
			dirtyEditors.add(editor);
		}
	}
	
	public void setClean(NodeEditor editor) {
		Optional<Pair<NodeEditor, Tab>> optional = editors.stream().filter( (p) -> {return p.getKey().equals(editor); }).findFirst();
		if(optional.isPresent()) {
			optional.get().getValue().setText(editor.getNode().getIdentifier());
			dirtyEditors.remove(editor);
		}
	}

	/**
	 * Saves all open editors
	 */
	public void saveAll() {
		saveAll( e -> true );
	}
	
	/**
	 * Filters all editors with predicate and saves them if the predicate is true.
	 * @param predicate
	 */
	public void saveAll(Predicate<NodeEditor> predicate) {
		new ArrayList<NodeEditor>(dirtyEditors).stream().filter(predicate).forEach( e -> e.save() );
		dirtyEditors.forEach( e -> e.setClean() );
		dirtyEditors.clear();
		projectManager.refreshView();	
	}

	/**
	 * Saves the content of an open tab
	 * @param selectedItem
	 */
	public void saveTab(Tab selectedItem) {
		saveTab(selectedItem, e -> true);
	}
	
	public void focusOnEditor(NodeEditor editor) {
		Optional<Pair<NodeEditor, Tab>> optional = editors.stream().filter( p -> p.getKey().equals(editor) ).findFirst();
		if(optional.isPresent()) {
			Tab tab = optional.get().getValue();
			tab.getTabPane().getSelectionModel().select(tab);
		}
	}
	
	public void saveTab(Tab tab, Predicate<NodeEditor> pred) {
		Optional<Pair<NodeEditor, Tab>> optional = editors.stream().filter( p -> p.getValue().equals(tab) ).findFirst();
		if(optional.isPresent() && pred.test(optional.get().getKey())) {
			optional.get().getKey().save();
			projectManager.refreshView();
		}
	}

	/**
	 * Closes all open editors and saves the content
	 */
	public void closeAllOpenEditors() {
		closeAllOpenEditors( e -> true );
	}
	
	/**
	 * Closes all open editors, saves the content according to the predicate 
	 * @param predicate
	 */
	public void closeAllOpenEditors(Predicate<NodeEditor> predicate) {
		saveAll(predicate);
		editors.stream().forEach( p -> mainController.closeTab(p.getValue()) );
		projectManager.saveProject();
	}
}
