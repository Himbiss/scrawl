package de.himbiss.scrawl.editors.personeditor;

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
import de.himbiss.scrawl.gui.PersonLayoutController;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.Person;

public class PersonEditor extends NodeEditor {

	@Inject
	EditorManager editorManager;
	
	private PersonLayoutController controller;

	private Person person;

	@Override
	protected void initialize(Node<?> content) {
		person = (Person) content;
	}

	@Override
	protected void createContent(AnchorPane anchorPane) {
		// Load root layout from fxml file.
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Pane result = fxmlLoader.load(MainApp.class.getResource("gui/PersonTemplate.fxml").openStream());
			if(result instanceof AnchorPane) {
				controller = (PersonLayoutController) fxmlLoader.getController();
				controller.setPerson(person, (p) -> { setDirty(); });
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
		return node instanceof Person;
	}

	@Override
	protected Node<?> getNode() {
		return person;
	}

	@Override
	public void save() {
		person.copyAttributes(controller.getPerson());
		setClean();
	}
	
	@Override
	public Set<Class<? extends Node<?>>> getAssociatedNodes() {
		Set<Class<? extends Node<?>>> ret = new HashSet<>();
		ret.add(Person.class);
		return ret;
	}
}
