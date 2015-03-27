package de.himbiss.scrawl.editors.manuscripteditor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.control.Tab;
import javafx.scene.paint.Color;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.InlineStyleTextArea;

import de.himbiss.scrawl.editors.IEditor;
import de.himbiss.scrawl.util.Constants;

public class ManuscriptEditor implements IEditor {

	private final Logger logger = LogManager.getLogger(ManuscriptEditor.class);
	
	private final InlineStyleTextArea<StyleInfo> area = new InlineStyleTextArea<StyleInfo>(
			StyleInfo.EMPTY.updateFontSize(12).updateFontFamily("Serif")
					.updateTextColor(Color.BLACK), style -> style.toCss());
	{
		area.setWrapText(true);
	}

	private File file;


	@Override
	public void initialize(File file) {
		this.file = file;
	}
	
	@Override
	public Tab createTab() {
		Tab tab = new Tab(file.getName());
		tab.setContent(area);

		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(file.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.ERROR, "Could not load contents of file: " + file.getAbsolutePath());
		}

		area.replaceText(new String(encoded, Charset.defaultCharset()));
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

	@Override
	public boolean validate(File file) {
		return !file.isDirectory();
	}

	@Override
	public File getFile() {
		return file;
	}


}
