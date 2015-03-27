package de.himbiss.scrawl.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

import javax.inject.Inject;

import com.cathive.fx.guice.FXMLController;

import de.himbiss.scrawl.MainApp;
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
	
	public boolean openNewTab(Tab tab) {
		return tabPane.getTabs().add(tab);
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
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
		System.out.println("save current");
	}

	/**
	 * Saves all open editors
	 */
	@FXML
	private void handleSaveAll() {
		projectManager.saveProject();
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
			scenesTree.setRoot(new NodeTreeItem<>(project.getScenes()));
			locationsTree.setRoot(new NodeTreeItem<>(project.getLocations()));
			personsTree.setRoot(new NodeTreeItem<>(project.getPersons()));
			objectsTree.setRoot(new NodeTreeItem<>(project.getObjects()));
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
	}
}
