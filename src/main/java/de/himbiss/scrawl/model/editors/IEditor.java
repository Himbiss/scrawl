package de.himbiss.scrawl.model.editors;

import java.io.File;

import javafx.scene.control.Tab;

public interface IEditor {

	Tab createTab( File file );
	
	void refresh();
	
	void save();
	
	String getEditorId();
	
}
