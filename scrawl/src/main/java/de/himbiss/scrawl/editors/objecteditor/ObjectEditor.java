package de.himbiss.scrawl.editors.objecteditor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.project.Object;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.gui.ObjectLayoutController;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.util.Constants;

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
				anchorPane.onKeyPressedProperty().setValue((e) -> {
					if (e.isControlDown() && e.getCode() == KeyCode.S) {
						save();
					} else if (!e.isMetaDown()) {
						setDirty();
					}
				});
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
	protected String getEditorId() {
		return Constants.OBJECT_EDITOR;
	}

	@Override
	protected Node<?> getNode() {
		return object;
	}

	@Override
	public void save() {
		object.copyAttributes(controller.getObject());
		setClean();
	}

}
