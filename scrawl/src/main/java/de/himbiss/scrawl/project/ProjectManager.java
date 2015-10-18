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
import de.himbiss.scrawl.gui.layout_controller.IVisualizesProject;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.NodeHelper;

public class ProjectManager implements IControlsProject {
	
	private static Logger logger = LogManager.getLogger(ProjectManager.class);
	
	@Inject
	IVisualizesProject projectView;
	
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
		projectView.setProject(getProject());
	}
	
	public void openProject(String projectName) {
		if(projectName != null) {
			logger.log(Level.INFO, "Trying to open the project \""+projectName+"\"");
			if(dao.projectExists(projectName)) {
				editorManager.closeAllOpenEditors();
				currentProject = dao.loadProject(projectName);
				setLastProjectName(projectName);
				projectView.setProject(getProject());
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
	public <T> boolean handleNewNode(Node<T> node) {
		Folder<T> folder = NodeHelper.getNextFolder(node);
		if(folder != null) {
			Node<?> uniqueNode = NodeFactory.createUniqueNode(node.getNodeType());
			folder.add((Node<T>) uniqueNode);
			projectView.setProject(getProject());
			return true;
		}
		return false;
	}

	@Override
	public <T> boolean handleDeleteNode(Node<T> node) {
		if(node.isDeletable()) {
			node.getParent().remove(node);
			NodeFactory.freeNode(node.getIdentifier(), node.getNodeType());
			projectView.setProject(getProject());
			return true;
		}
		return false;
	}

	@Override
	public <T> void handleCopyNode(Node<T> node) {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		content.put(NodeHelper.getDataFormat(node.getNodeType()), node);
		clipboard.setContent(content);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> boolean handlePasteNode(Node<T> node) {
		DataFormat fmt = NodeHelper.getDataFormat(node.getNodeType());
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if(clipboard.hasContent(fmt)) {
			Node<?> content = (Node<?>) clipboard.getContent(fmt);
			if(content.getNodeType().equals(node.getNodeType())) {
				Node<T> paste = (Node<T>) content;
				paste.setIdentifier(NodeFactory.generateUniqueIdentifier(paste.getIdentifier(),paste.getNodeType()));
				NodeFactory.registerNode(paste);
				NodeHelper.getNextFolder(node).add(paste);
				projectView.setProject(getProject());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean handleOpenNode(Node<?> node, Class<? extends NodeEditor> editor) {
		return editorManager.openEditor(editor, node);
	}

	@Override
	public <T> boolean handleNewFolder(Node<T> node) {
		Folder<T> folder = NodeHelper.getNextFolder(node);
		folder.add(new Folder<T>("New Folder", node.getNodeType()));
		projectView.setProject(getProject());
		return true;
	}

	@Override
	public void refreshView() {
		projectView.setProject(currentProject);
	}
	
}
