package de.himbiss.scrawl.gui;


import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DataFormat;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.ProjectController;
import de.himbiss.scrawl.util.Constants;

public class TreeContextMenu <T> extends ContextMenu {

	private TreeView<Node<T>> treeView;
	private MenuItem newItem;
	private MenuItem newFolderItem;
	private MenuItem copyItem;
	private MenuItem pasteItem;
	private MenuItem deleteItem;
	
	private Property<Boolean> newDisabled;
	private Property<Boolean> newFolderDisabled;
	private Property<Boolean> copyDisabled;
	private Property<Boolean> pasteDisabled;
	private Property<Boolean> deleteDisabled;
	
	public TreeContextMenu(TreeView<Node<T>> treeView, DataFormat fmt, ProjectController projectController) {
		this.treeView = treeView;
		newItem = new MenuItem(Constants.NEW_ITEM);
		newFolderItem = new MenuItem(Constants.NEW_FOLDER);
		copyItem = new MenuItem(Constants.COPY_ITEM);
		pasteItem = new MenuItem(Constants.PASTE_ITEM);
		deleteItem = new MenuItem(Constants.DELETE_ITEM);
		
		newDisabled = new SimpleBooleanProperty(false);
		newFolderDisabled = new SimpleBooleanProperty(false);
		copyDisabled = new SimpleBooleanProperty(true);
		pasteDisabled = new SimpleBooleanProperty(false);
		deleteDisabled = new SimpleBooleanProperty(true);
		
		newItem.disableProperty().bindBidirectional(newDisabled);
		newFolderItem.disableProperty().bindBidirectional(newFolderDisabled);
		copyItem.disableProperty().bindBidirectional(copyDisabled);
		pasteItem.disableProperty().bindBidirectional(pasteDisabled);
		deleteItem.disableProperty().bindBidirectional(deleteDisabled);
		
		treeView.onContextMenuRequestedProperty().setValue(new javafx.event.EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				if(treeView.getSelectionModel().getSelectedItem() != null) {
					Node<?> n = treeView.getSelectionModel().getSelectedItem().getValue();
					copyDisabled.setValue(false);
					deleteDisabled.setValue(!n.isDeletable());
					newDisabled.setValue(false);
					newFolderDisabled.setValue(false);
				} else {
					copyDisabled.setValue(true);
					deleteDisabled.setValue(true);
					newDisabled.setValue(true);
					newFolderDisabled.setValue(true);
				}

				Clipboard clipboard = Clipboard.getSystemClipboard();
				pasteDisabled.setValue(!clipboard.hasContent(fmt));
			}
		});
		
		newItem.setOnAction( (event) -> {projectController.handleNewNode(getSelectedNode());} );
		newFolderItem.setOnAction( (event) -> {projectController.handleNewFolder(getSelectedNode());} );
		deleteItem.setOnAction( (event) -> {projectController.handleDeleteNode(getSelectedNode());} );
		copyItem.setOnAction( (event) -> {projectController.handleCopyNode(getSelectedNode());} );
		pasteItem.setOnAction( (event) -> {projectController.handlePasteNode(getSelectedNode());} );
		
		getItems().addAll(newItem, newFolderItem, copyItem, pasteItem, deleteItem);
	}
	
	private Node<T> getSelectedNode() {
		SelectionModel<TreeItem<Node<T>>> selectionModel = treeView.getSelectionModel();
		return selectionModel.isEmpty() ? null : selectionModel.getSelectedItem().getValue();
	}
	
}
