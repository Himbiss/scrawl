package de.himbiss.scrawl.gui.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cathive.fx.guice.FXMLController;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.dao.IProjectDAO;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.gui.AboutBox;
import de.himbiss.scrawl.gui.NodeTreeItem;
import de.himbiss.scrawl.gui.TreeContextMenu;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Object;
import de.himbiss.scrawl.project.Person;
import de.himbiss.scrawl.project.Project;
import de.himbiss.scrawl.project.ProjectManager;
import de.himbiss.scrawl.project.Scene;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.NodeHelper;

@FXMLController(controllerId = "mainController")
public final class MainLayoutController implements Initializable {
	
	private static Logger logger = LogManager.getLogger(MainLayoutController.class);

	MainApp mainApp;
	
	@Inject
	IProjectDAO dao;

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
	 * Closes the application and tries to save all open editors before that.
	 */
	@FXML
	private void handleExit() {
		mainApp.exitApplication();
	}

	/**
	 * Creates a new project
	 */
	@FXML
	private void handleNewProject() {
		TextInputDialog dialog = new TextInputDialog("New Project");
		dialog.setTitle("New project");
		dialog.setHeaderText("Create a new project");
		dialog.setContentText("New project name:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			String projectName = result.get();
			if(!dao.projectExists(projectName)) {
				logger.log(Level.INFO, "Creating the new project \""+projectName+"\"");
				dao.createProject(projectName);
				projectManager.openProject(projectName);
			} else {
				Alert alert = new Alert(AlertType.ERROR, "This project already exists");
				alert.showAndWait();
			}
		}
	}

	/**
	 * Opens a new project
	 */
	@FXML
	private void handleOpenProject() {
		List<String> projects = dao.findAll();
		ChoiceDialog<String> dialog = new ChoiceDialog<>(projects.get(0), projects);
		dialog.setTitle(Constants.OPEN_PROJECT);
		dialog.setHeaderText(Constants.OPEN_PROJECT);
		dialog.setContentText("Choose a project to open:");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			projectManager.openProject(result.get());
		}
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
	 * @throws Exception 
	 */
	@FXML
	private void handleAbout() throws Exception {
		AboutBox box = new AboutBox();
		box.show();
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
			mainApp.getPrimaryStage().setTitle(Constants.TITLE+": "+project.getIdentifier());
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

	public void closeTab(Tab tab) {
		tab.getOnClosed().handle(null);
		tabPane.getTabs().remove(tab);
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
	private static Logger logger = LogManager.getLogger(MouseDoubleclickHandler.class);

	private TreeView<Node<T>> treeView;
	private EditorManager editorManager;
	
	public MouseDoubleclickHandler(TreeView<Node<T>> treeView, EditorManager editorManager) {
		this.treeView = treeView;
		this.editorManager = editorManager;
	}
	
	@Override
	public void handle(MouseEvent event) {
		if(event.getClickCount() > 1) {
			Node<?> node = treeView.getSelectionModel().getSelectedItem().getValue();
			boolean success = editorManager.openPreferredEditor(node);
			if(!success) {
				logger.log(Level.FATAL, "Could not open node " + node.toString() + " in new editor");
			}
		}
	}
	
}
