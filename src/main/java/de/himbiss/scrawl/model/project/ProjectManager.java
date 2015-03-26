package de.himbiss.scrawl.model.project;

import java.io.File;
import java.util.prefs.Preferences;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.NodeHelper;
import de.himbiss.scrawl.view.MainLayoutController;

public class ProjectManager implements ProjectController {
	
	@Inject
	MainLayoutController mainController;

	Logger logger = LogManager.getLogger(ProjectManager.class);
	
	private Project currentProject;

	public File getProjectPath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String path = prefs.get(Constants.PROJECT_LOCATION, null);
		if(path != null) {
			return new File(path);
		} else {
			return new File(System.getProperty(Constants.USER_HOME));
		}
	}
	
	public void setProjectPath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put(Constants.PROJECT_LOCATION, file.getPath());
        } else {
            prefs.remove(Constants.PROJECT_LOCATION);
        }
    }
	
	private Project parseProject(File projectRoot) {
		logger.log(Level.INFO, "Parsing the project");
		Project project = NodeFactory.createProject("TestProjekt");
		project.getPersons().add(NodeFactory.createPerson("Hänsel"));
		project.getPersons().add(NodeFactory.createPerson("Gretel"));
		project.getLocations().add(NodeFactory.createLocation("Pfefferkuchenhaus"));
		project.getLocations().add(NodeFactory.createLocation("Wald"));
		project.getObjects().add(NodeFactory.createObject("Ofen"));
		project.getScenes().add(NodeFactory.createScene("Kapitel1"));
		return project;
	}
	
	public boolean loadProject() {
		File projectRoot = getProjectPath();
		if(projectRoot != null) {
			logger.log(Level.INFO, "Loading project from location: " + projectRoot.getAbsolutePath());
			Project project = parseProject(projectRoot);
			this.currentProject = project;
			mainController.setProject(project);
			return true;
		}
		logger.log(Level.ERROR, "Error loading project, could not retrieve project path");
		return false;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void handleNewNode(Node<T> n) {
		Folder<T> folder = n.isFolder() ? (Folder<T>)n : n.getParent();
		Node<?> uniqueNode = NodeFactory.createUniqueNode(n.getNodeType());
		folder.add((Node<T>) uniqueNode);
		mainController.setProject(getCurrentProject());
	}

	@Override
	public <T> void handleDeleteNode(Node<T> n) {
		n.getParent().remove(n);
		NodeFactory.freeNode(n.getIdentifier(), n.getNodeType());
		mainController.setProject(getCurrentProject());
	}

	@Override
	public <T> void handleCopyNode(Node<T> n) {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		content.put(NodeHelper.getDataFormat(n.getNodeType()), n);
		clipboard.setContent(content);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void handlePasteNode(Node<T> n) {
		DataFormat fmt = NodeHelper.getDataFormat(n.getNodeType());
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if(clipboard.hasContent(fmt)) {
			Node<?> content = (Node<?>) clipboard.getContent(fmt);
			if(content.getNodeType().equals(n.getNodeType())) {
				Node<T> paste = (Node<T>) content;
				paste.setIdentifier(NodeFactory.generateUniqueIdentifier(paste.getIdentifier(),paste.getNodeType()));
				NodeFactory.registerNode(paste);
				NodeHelper.getNextFolder(n).add(paste);
			}
		}
		mainController.setProject(getCurrentProject());
	}
	
}
