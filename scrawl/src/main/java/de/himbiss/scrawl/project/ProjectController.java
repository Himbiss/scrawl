package de.himbiss.scrawl.project;

public interface ProjectController {

	/**
	 * Handles adding a node to the currently selected node n
	 * @param n
	 */
	public <T> void handleNewNode(Node<T> n);
	
	/**
	 * Handles adding a folder to the currently selected node n
	 * @param n
	 */
	public <T> void handleNewFolder(Node<T> n);
	
	/**
	 * Handles the deletion of node n
	 * @param n
	 */
	public <T> void handleDeleteNode(Node<T> n);
	
	/**
	 * Handles copying of the node n to the clipboard
	 * @param n
	 */
	public <T> void handleCopyNode(Node<T> n);
	
	/**
	 * Handles pasting the content of the clipboard at node n
	 * @param n
	 */
	public <T> void handlePasteNode(Node<T> n);

	/**
	 * Refreshes the project view
	 */
	public void refreshView();
	
}