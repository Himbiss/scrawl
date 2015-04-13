package de.himbiss.scrawl.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import javax.inject.Inject;

import com.cathive.fx.guice.FXMLController;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Object;
import de.himbiss.scrawl.project.Person;
import de.himbiss.scrawl.project.Project;
import de.himbiss.scrawl.project.ProjectManager;
import de.himbiss.scrawl.project.Scene;
import de.himbiss.scrawl.util.NodeHelper;

@FXMLController(controllerId = "mainController")
public final class MainLayoutController implements Initializable {

	MainApp mainApp;

	@FXML
	private TreeView<Node<Scene>> scenesTree;
	
	@FXML
	private TreeView<Node<Person>> personsTree;
	
	@FXML
	private TreeView<Node<Object>> objectsTree;
	
	@FXML
	private TreeView<Node<Location>> locationsTree;

	@FXML
	private TabPane tabPane;
	
	@Inject
	private ProjectManager projectManager;
	
	@Inject
	private EditorManager editorManager;
	
	public boolean openNewTab(Tab tab) {
		return tabPane.getTabs().add(tab);
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		editorManager.saveAll();
		projectManager.saveProject();
		System.exit(0);
	}

	/**
	 * Creates a new project
	 */
	@FXML
	private void handleNewProject() {
		System.out.println("new project");
	}

	/**
	 * Opens a new project
	 */
	@FXML
	private void handleOpenProject() {
		System.out.println("open project");
	}

	/**
	 * Saves the current open editor
	 */
	@FXML
	private void handleSaveCurrent() {
		editorManager.saveTab(tabPane.getSelectionModel().getSelectedItem());
	}

	/**
	 * Saves all open editors
	 */
	@FXML
	private void handleSaveAll() {
		editorManager.saveAll();
	}

	/**
	 * Opens the about popup
	 */
	@FXML
	private void handleAbout() {
		System.out.println("about");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void setProject(Project project) {
		if(project != null) {
			clearTrees();
			NodeTreeItem<Scene> scenesRoot = new NodeTreeItem<>(project.getScenes());
			NodeTreeItem<Location> locationsRoot = new NodeTreeItem<>(project.getLocations());
			NodeTreeItem<Person> personsRoot = new NodeTreeItem<>(project.getPersons());
			NodeTreeItem<Object> objectsRoot = new NodeTreeItem<>(project.getObjects());
			scenesTree.setRoot(scenesRoot);
			locationsTree.setRoot(locationsRoot);
			personsTree.setRoot(personsRoot);
			objectsTree.setRoot(objectsRoot);
		}
	}

	private void clearTrees() {
		scenesTree.setRoot(null);
		locationsTree.setRoot(null);
		personsTree.setRoot(null);
		objectsTree.setRoot(null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TreeContextMenu<Scene> scenesContextMenu = new TreeContextMenu<Scene>(scenesTree, NodeHelper.SCENE_FORMAT, projectManager);
		TreeContextMenu<Location> locationsContextMenu = new TreeContextMenu<Location>(locationsTree, NodeHelper.LOCATION_FORMAT, projectManager);
		TreeContextMenu<Person> personsContextMenu = new TreeContextMenu<Person>(personsTree, NodeHelper.PERSON_FORMAT, projectManager);
		TreeContextMenu<Object> objectsContextMenu = new TreeContextMenu<Object>(objectsTree, NodeHelper.OBJECT_FORMAT, projectManager);
		
		scenesTree.setContextMenu(scenesContextMenu);
		locationsTree.setContextMenu(locationsContextMenu);
		personsTree.setContextMenu(personsContextMenu);
		objectsTree.setContextMenu(objectsContextMenu);
		
		scenesTree.setOnMouseClicked(new MouseDoubleclickHandler<>(scenesTree, editorManager));
		locationsTree.setOnMouseClicked(new MouseDoubleclickHandler<>(locationsTree, editorManager));
		personsTree.setOnMouseClicked(new MouseDoubleclickHandler<>(personsTree, editorManager));
		objectsTree.setOnMouseClicked(new MouseDoubleclickHandler<>(objectsTree, editorManager));
		
		scenesTree.setCellFactory(new TreeItemCallback<Scene>());
		locationsTree.setCellFactory(new TreeItemCallback<Location>());
		personsTree.setCellFactory(new TreeItemCallback<Person>());
		objectsTree.setCellFactory(new TreeItemCallback<Object>());
	}
	
	
}

class TreeItemCallback <T extends Node<?>> implements Callback<TreeView<Node<T>>, TreeCell<Node<T>>> {

	@Override
	public TreeCell<Node<T>> call(TreeView<Node<T>> param) {
		return new TreeCell<Node<T>>() {
			@Override
			protected void updateItem(Node<T> item, boolean empty) {
				super.updateItem(item, empty);
				if(!isEmpty()) {
					item.registerObserver( (n) -> {setGraphic(new Label(n.getIdentifier()));} );
					setGraphic(new Label(item.getIdentifier()));
				} else {
					setGraphic(null);
				}
			}
		};
	}
	
}
class MouseDoubleclickHandler <T extends Node<T>> implements EventHandler<MouseEvent> {

	private TreeView<Node<T>> treeView;
	private EditorManager editorManager;
	
	public MouseDoubleclickHandler(TreeView<Node<T>> treeView, EditorManager editorManager) {
		this.treeView = treeView;
		this.editorManager = editorManager;
	}
	
	@Override
	public void handle(MouseEvent event) {
		if(event.getClickCount() > 1)
			editorManager.openPreferredEditor(treeView.getSelectionModel().getSelectedItem().getValue());
	}
	
}
