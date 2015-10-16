package de.himbiss.scrawl.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import de.himbiss.scrawl.project.NodeFactory;
import de.himbiss.scrawl.project.Object;

public class ObjectLayoutController implements Initializable {

	@FXML TextField name;
	@FXML TextField location;
	@FXML TextField type;
	@FXML TextField abilities;
	@FXML TextArea summary;
	
	private Object object;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		object = NodeFactory.createUniqueObject();
	}
	
	public void setObject(Object object, Consumer<Object> onTextChange) {
		this.object.copyAttributes(object);
		name.setText(object.getName());
		location.setText(object.getLocation());
		type.setText(object.getType());
		abilities.setText(object.getAbilities());
		summary.setText(object.getSummary());
		ChangeListener<String> handler = (o,so,sn) -> {
			this.object.setName(name.getText());
			this.object.setLocation(location.getText());
			this.object.setType(type.getText());
			this.object.setAbilities(abilities.getText());
			this.object.setSummary(summary.getText());
			onTextChange.accept(object);
		};
		name.textProperty().addListener(handler);
		location.textProperty().addListener(handler);
		type.textProperty().addListener(handler);
		abilities.textProperty().addListener(handler);
		summary.textProperty().addListener(handler);
	}
	
	public Object getObject() {
		return object;
	}
}
