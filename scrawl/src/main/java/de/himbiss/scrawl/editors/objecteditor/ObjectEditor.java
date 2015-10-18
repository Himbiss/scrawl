package de.himbiss.scrawl.editors.objecteditor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.gui.EditorKeyEventHandler;
import de.himbiss.scrawl.gui.layout_controller.ObjectLayoutController;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Object;

public class ObjectEditor extends NodeEditor {

	@Inject
	EditorManager editorManager;

	private ObjectLayoutController controller;

	private Object object;

	@Override
	protected void initialize(Node<?> content) {
		object = (Object) content;
	}

	@Override
	protected void createContent(AnchorPane anchorPane) {
		// Load root layout from fxml file.
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Pane result = fxmlLoader.load(MainApp.class.getResource(
					"gui/ObjectTemplate.fxml").openStream());
			if (result instanceof AnchorPane) {
				controller = (ObjectLayoutController) fxmlLoader
						.getController();
				controller.setObject(object, p -> setDirty());
				AnchorPane anchor = (AnchorPane) result;
				AnchorPane.setBottomAnchor(result, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				anchorPane.getChildren().add(anchor);
				anchorPane.onKeyPressedProperty().setValue(EditorKeyEventHandler.create(this));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean validate(Node<?> node) {
		return node instanceof Object;
	}

	@Override
	public Node<?> getNode() {
		return object;
	}

	@Override
	public void save() {
		object.copyAttributes(controller.getObject());
		setClean();
	}
	
	@Override
	public Set<Class<? extends Node<?>>> getAssociatedNodes() {
		Set<Class<? extends Node<?>>> ret = new HashSet<>();
		ret.add(Object.class);
		return ret;
	}
	
	public String getEditorName() {
		return "Object Editor";
	}

}
