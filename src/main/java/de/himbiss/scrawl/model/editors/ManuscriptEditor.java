package de.himbiss.scrawl.model.editors;

import java.io.File;

import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import de.himbiss.scrawl.model.Constants;

public class ManuscriptEditor implements IEditor {

	@Override
	public Tab createTab(File file) {
		Tab tab = new Tab(file.getName());
		tab.setContent(new Text("Hello World!!!"));
		return tab;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String getEditorId() {
		return Constants.MANUSCRIPT_EDITOR;
	}

}
