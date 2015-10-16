package de.himbiss.scrawl.project;

import java.util.prefs.Preferences;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.dao.IProjectDAO;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.gui.controller.MainLayoutController;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.NodeHelper;

public class ProjectManager implements IProjectController {
	
	private static Logger logger = LogManager.getLogger(ProjectManager.class);
	
	@Inject
	MainLayoutController mainController;
	
	@Inject
	EditorManager editorManager;
	
	@Inject
	IProjectDAO dao;

	
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
	
	public void openProject(String projectName) {
		if(projectName != null) {
			logger.log(Level.INFO, "Trying to open the project \""+projectName+"\"");
			if(dao.projectExists(projectName)) {
				editorManager.closeAllOpenEditors();
				currentProject = dao.loadProject(projectName);
				setLastProjectName(projectName);
				mainController.setProject(getProject());
			} else {
				logger.log(Level.ERROR, "The project \""+projectName+"\" does not exist!");
			}
		}
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
		if(folder != null) {
			Node<?> uniqueNode = NodeFactory.createUniqueNode(n.getNodeType());
			folder.add((Node<T>) uniqueNode);
			mainController.setProject(getProject());
		}
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
	public void handleOpenNode(Node<?> node, Class<? extends NodeEditor> editor) {
		editorManager.openEditor(editor, node);
	}

	@Override
	public <T> void handleNewFolder(Node<T> n) {
		Folder<T> folder = n.isFolder() ? (Folder<T>)n : n.getParent();
		folder.add(new Folder<T>("New Folder", n.getNodeType()));
		mainController.setProject(getProject());
	}

	@Override
	public void refreshView() {
		mainController.setProject(currentProject);
	}
	
}
