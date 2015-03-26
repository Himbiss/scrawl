package de.himbiss.scrawl.view;


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
import de.himbiss.scrawl.model.Constants;
import de.himbiss.scrawl.model.project.Node;
import de.himbiss.scrawl.model.project.ProjectController;

public class TreeContextMenu <T> extends ContextMenu {

	private TreeView<Node<T>> treeView;
	private MenuItem newItem;
	private MenuItem copyItem;
	private MenuItem pasteItem;
	private MenuItem deleteItem;
	
	private Property<Boolean> newDisabled;
	private Property<Boolean> copyDisabled;
	private Property<Boolean> pasteDisabled;
	private Property<Boolean> deleteDisabled;
	
	public TreeContextMenu(TreeView<Node<T>> treeView, DataFormat fmt, ProjectController projectController) {
		this.treeView = treeView;
		newItem = new MenuItem(Constants.NEW_ITEM);
		copyItem = new MenuItem(Constants.COPY_ITEM);
		pasteItem = new MenuItem(Constants.PASTE_ITEM);
		deleteItem = new MenuItem(Constants.DELETE_ITEM);
		
		newDisabled = new SimpleBooleanProperty(false);
		copyDisabled = new SimpleBooleanProperty(true);
		pasteDisabled = new SimpleBooleanProperty(false);
		deleteDisabled = new SimpleBooleanProperty(true);
		
		newItem.disableProperty().bindBidirectional(newDisabled);
		copyItem.disableProperty().bindBidirectional(copyDisabled);
		pasteItem.disableProperty().bindBidirectional(pasteDisabled);
		deleteItem.disableProperty().bindBidirectional(deleteDisabled);
		
		treeView.onContextMenuRequestedProperty().setValue(new javafx.event.EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				if(treeView.getSelectionModel().getSelectedItem() != null) {
					copyDisabled.setValue(false);
					deleteDisabled.setValue(false);
					Node<T> node = treeView.getSelectionModel().getSelectedItem().getValue();
					newDisabled.setValue(node.isFolder());
				} else {
					copyDisabled.setValue(true);
					deleteDisabled.setValue(true);
					newDisabled.setValue(true);
				}

				Clipboard clipboard = Clipboard.getSystemClipboard();
				pasteDisabled.setValue(!clipboard.hasContent(fmt));
			}
		});
		
		newItem.setOnAction( (event) -> {projectController.handleNewNode(getSelectedNode());} );
		deleteItem.setOnAction( (event) -> {projectController.handleDeleteNode(getSelectedNode());} );
		copyItem.setOnAction( (event) -> {projectController.handleCopyNode(getSelectedNode());} );
		pasteItem.setOnAction( (event) -> {projectController.handlePasteNode(getSelectedNode());} );
		
		getItems().addAll(newItem, copyItem, pasteItem, deleteItem);
	}
	
	private Node<T> getSelectedNode() {
		SelectionModel<TreeItem<Node<T>>> selectionModel = treeView.getSelectionModel();
		return selectionModel.isEmpty() ? null : selectionModel.getSelectedItem().getValue();
	}
	
}
