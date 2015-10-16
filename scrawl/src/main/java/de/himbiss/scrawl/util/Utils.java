package de.himbiss.scrawl.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import de.himbiss.scrawl.editors.NodeEditor;

public class Utils {
	
	public static boolean closeEditorAskUser(NodeEditor editor) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.headerTextProperty().set("Warning");
		alert.contentTextProperty().set("Do you want to save '" + editor.getNode().getIdentifier() + "'?");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		return alert.showAndWait().filter( bt -> bt.equals(ButtonType.YES) ).isPresent();
	}
	
}
