package de.himbiss.scrawl.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.NodeFactory;
import de.himbiss.scrawl.util.Constants;

public class LocationLayoutController implements Initializable {

	private Location location;
	
	@FXML TextField name;
	@FXML TextArea summary;
	@FXML TextArea shortSummary;
	@FXML TextArea inhabitants;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = NodeFactory.createLocation(Constants.NEW_LOCATION);
	}

	public void setLocation(Location location, Consumer<Location> onTextChange) {
		this.location.copyAttributes(location);
		name.setText(location.getName());
		summary.setText(location.getSummary());
		shortSummary.setText(location.getShortSummary());
		inhabitants.setText(location.getInhabitants());
		javafx.beans.value.ChangeListener<String> handler = (o,so,sn) -> {
			this.location.setName(name.getText());
			this.location.setSummary(summary.getText());
			this.location.setShortSummary(shortSummary.getText());
			this.location.setInhabitants(inhabitants.getText());
			onTextChange.accept(location);
		};
		name.textProperty().addListener(handler);
		summary.textProperty().addListener(handler);
		shortSummary.textProperty().addListener(handler);
		inhabitants.textProperty().addListener(handler);
	}
	
	public Location getLocation() {
		return location;
	}
}
