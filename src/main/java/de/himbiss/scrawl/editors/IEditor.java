package de.himbiss.scrawl.editors;

import java.io.File;

import javafx.scene.control.Tab;

public interface IEditor {
	
	void initialize(File file);
	
	Tab createTab();
	
	void refresh();
	
	void save();
	
	boolean validate(File file);
	
	String getEditorId();
	
	File getFile();
	
}
