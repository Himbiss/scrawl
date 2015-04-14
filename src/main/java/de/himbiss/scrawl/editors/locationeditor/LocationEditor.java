package de.himbiss.scrawl.editors.locationeditor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.gui.LocationLayoutController;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.util.Constants;

public class LocationEditor extends NodeEditor {

	@Inject
	EditorManager editorManager;

	private LocationLayoutController controller;

	private Location location;

	@Override
	protected void initialize(Node<?> content) {
		location = (Location) content;
	}

	@Override
	protected void createContent(AnchorPane anchorPane) {
		// Load root layout from fxml file.
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Pane result = fxmlLoader.load(MainApp.class.getResource(
					"gui/LocationTemplate.fxml").openStream());
			if (result instanceof AnchorPane) {
				controller = (LocationLayoutController) fxmlLoader
						.getController();
				controller.setLocation(location, (p) -> {
					setDirty();
				});
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
		return node instanceof Location;
	}

	@Override
	protected String getEditorId() {
		return Constants.LOCATION_EDITOR;
	}

	@Override
	protected Node<?> getNode() {
		return location;
	}

	@Override
	public void save() {
		location.copyAttributes(controller.getLocation());
		setClean();
	}
}