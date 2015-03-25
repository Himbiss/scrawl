package de.himbiss.scrawl.view;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import de.himbiss.scrawl.model.project.Folder;
import de.himbiss.scrawl.model.project.Node;

public class NodeTreeItem<T> extends TreeItem<Node<T>> {
	
	public NodeTreeItem(Node<T> node) {
		setValue(node);
	}
	
	@Override
	public ObservableList<TreeItem<Node<T>>> getChildren() {
		if(isFolder()) {
			Folder<T> folder = (Folder<T>) getValue();
			ObservableList<TreeItem<Node<T>>> children = FXCollections.observableArrayList();
			List<TreeItem<Node<T>>> lst = folder.getComponents().stream().map( (n) -> { return new NodeTreeItem<T>(n); }).collect(Collectors.toList());
			children.addAll(lst);
			return children;
		}
		return super.getChildren();
	}
	
	private boolean isFolder() {
		return getValue() instanceof Folder<?>;
	}
	
	@Override
	public boolean isLeaf() {
		return !isFolder();
	}
}
