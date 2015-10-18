package de.himbiss.scrawl.project;

import de.himbiss.scrawl.editors.NodeEditor;

public interface IControlsProject {

	/**
	 * Handles adding a node to the currently selected node
	 * @param node
	 */
	public <T> boolean handleNewNode(Node<T> node);
	
	/**
	 * Handles adding a folder to the currently selected node
	 * @param node
	 */
	public <T> boolean handleNewFolder(Node<T> node);
	
	/**
	 * Handles the deletion of node
	 * @param node
	 */
	public <T> boolean handleDeleteNode(Node<T> node);
	
	/**
	 * Handles copying of node to the clipboard
	 * @param node
	 */
	public <T> void handleCopyNode(Node<T> node);
	
	/**
	 * Handles pasting the content of the clipboard at node
	 * @param node
	 */
	public <T> boolean handlePasteNode(Node<T> node);

	/**
	 * Handles opening node with editor
	 * @param node
	 * @param editor
	 */
	public boolean handleOpenNode(Node<?> node, Class<? extends NodeEditor> editor);

	/**
	 * Refreshes the project view
	 */
	public void refreshView();

	
}
