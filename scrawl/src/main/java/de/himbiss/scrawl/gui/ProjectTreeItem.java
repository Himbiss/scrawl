package de.himbiss.scrawl.gui;

import java.util.List;
import java.util.stream.Collectors;

import de.himbiss.scrawl.project.AbstractNode;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Project;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class ProjectTreeItem extends TreeItem<AbstractNode> {

	private boolean isFirstTimeChildren = true;
	
	public ProjectTreeItem(Project project) {
		super(project);
		setExpanded(true);
		addEventHandler(treeNotificationEvent(), e -> project.setExpanded(isExpanded()) );
	}
	
	@Override
	public ObservableList<TreeItem<AbstractNode>> getChildren() {
		if(isFirstTimeChildren) {
			Project project = (Project) getValue();
			ObservableList<TreeItem<AbstractNode>> children = super.getChildren();
			List<TreeItem<AbstractNode>> lst = project.getScenes().getComponents().stream().map(ProjectTreeItem::mapNodeToTreeItem).collect(Collectors.toList());
		}
		return super.getChildren();
	}
	
	private static <T> TreeItem<AbstractNode> mapNodeToTreeItem(Node<T> node) {
		AbstractNode abstractNode = (AbstractNode) node;
		TreeItem<Node<T>> item = new NodeTreeItem<T>(abstractNode); 
		item.setExpanded(node.isExpanded()); 
		return (TreeItem<AbstractNode>) item;
	}
}
