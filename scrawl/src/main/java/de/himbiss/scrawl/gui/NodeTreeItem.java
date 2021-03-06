package de.himbiss.scrawl.gui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import de.himbiss.scrawl.project.AbstractNode;
import de.himbiss.scrawl.project.Folder;
import de.himbiss.scrawl.project.Node;

public class NodeTreeItem<T> extends TreeItem<AbstractNode> {
	
	private boolean isFirstTimeChildren = true;
	
	public NodeTreeItem(Node<T> node) {
		super(node);
		setExpanded(true);
		addEventHandler(treeNotificationEvent(), (e) -> { node.setExpanded(isExpanded()); } );
	}
	
	@Override
	public ObservableList<TreeItem<Node<T>>> getChildren() {
		if(getValue().isFolder() && isFirstTimeChildren) {
			Folder<T> folder = (Folder<T>) getValue();
			ObservableList<TreeItem<Node<T>>> children = super.getChildren();
			List<TreeItem<Node<T>>> lst = folder.getComponents().stream().map( (n) -> { NodeTreeItem<T> item = new NodeTreeItem<T>(n); item.setExpanded(n.isExpanded()); return item; }).collect(Collectors.toList());
			children.setAll(lst);
			isFirstTimeChildren = false;
			return children;
		}
		return super.getChildren();
	}
	
	@Override
	public boolean isLeaf() {
		return !getValue().isFolder();
	}
}
