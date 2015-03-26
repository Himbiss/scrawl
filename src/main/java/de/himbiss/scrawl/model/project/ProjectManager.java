package de.himbiss.scrawl.model.project;

import java.util.prefs.Preferences;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.dao.IProjectDAO;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.NodeHelper;
import de.himbiss.scrawl.view.MainLayoutController;

public class ProjectManager implements ProjectController {
	
	@Inject
	MainLayoutController mainController;
	
	@Inject
	IProjectDAO dao;

	Logger logger = LogManager.getLogger(ProjectManager.class);
	
	private Project currentProject;
	
	public void initialize() {
		if(getLastProjectName() == null || !dao.projectExists(getLastProjectName())) {
			currentProject = dao.createProject(Constants.NEW_PROJECT);
			setLastProjectName(currentProject.getIdentifier());
		} else {
			currentProject = dao.loadProject(getLastProjectName());
		}
		mainController.setProject(getProject());
	}
	
	private String getLastProjectName() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		return prefs.get(Constants.PROJECT_NAME, null);
	}
	
	private void setLastProjectName(String name) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		prefs.put(Constants.PROJECT_NAME, name);
	}
	
	public void saveProject() {
		dao.saveProject(currentProject);
	}

	public Project getProject() {
		return currentProject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void handleNewNode(Node<T> n) {
		Folder<T> folder = n.isFolder() ? (Folder<T>)n : n.getParent();
		Node<?> uniqueNode = NodeFactory.createUniqueNode(n.getNodeType());
		folder.add((Node<T>) uniqueNode);
		mainController.setProject(getProject());
	}

	@Override
	public <T> void handleDeleteNode(Node<T> n) {
		n.getParent().remove(n);
		NodeFactory.freeNode(n.getIdentifier(), n.getNodeType());
		mainController.setProject(getProject());
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
		mainController.setProject(getProject());
	}

	@Override
	public <T> void handleNewFolder(Node<T> n) {
		Folder<T> folder = n.isFolder() ? (Folder<T>)n : n.getParent();
		folder.add(new Folder<T>("New Folder", n.getNodeType()));
		mainController.setProject(getProject());
	}
	
}
