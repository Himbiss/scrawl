package de.himbiss.scrawl.editors.manuscripteditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.gui.EditorKeyEventHandler;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.ContentType;
import de.himbiss.scrawl.project.Scene;

public class ManuscriptEditor extends NodeEditor {
	
	private static Logger logger = LogManager.getLogger(ManuscriptEditor.class);

	@Inject
	EditorManager editorManager;
	
	private Scene scene;

	private HTMLEditor htmlEditor;

	@Override
	protected void initialize(Node<?> content) {
		if(content instanceof Scene)
			scene = (Scene) content;
	}

	@Override
	protected void createContent(AnchorPane anchorPane) {
		htmlEditor = new HTMLEditor();
		AnchorPane.setTopAnchor(htmlEditor, 0d);
		AnchorPane.setBottomAnchor(htmlEditor, 0d);
		AnchorPane.setRightAnchor(htmlEditor, 0d);
		AnchorPane.setLeftAnchor(htmlEditor, 0d);
        anchorPane.getChildren().add(htmlEditor);
        htmlEditor.setHtmlText(scene.getContent());
        htmlEditor.onKeyPressedProperty().setValue(EditorKeyEventHandler.create(this));
        Button btn = new Button("Generate PDF");
        btn.setOnAction( e -> generatePDF() );
	}
	
	private void generatePDF() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a destination");
		File file = fileChooser.showSaveDialog(null);
		if(file != null) {
		    try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");) {
		   		file.createNewFile();
		   		String content = htmlEditor.getHtmlText();
		 		writer.write(content);
		 	} catch (IOException e) {
				logger.log(Level.ERROR, "Error writing content of node '" + scene.getIdentifier() + "' to file '" + file.getAbsolutePath() + "'");
			}
		}
	}

	@Override
	protected boolean validate(Node<?> node) {
		return node.getNodeType() == ContentType.SCENE;
	}

	@Override
	public String getEditorName() {
		return "Manuscript HTML Editor";
	}

	@Override
	public Node<?> getNode() {
		return scene;
	}

	@Override
	public Set<Class<? extends Node<?>>> getAssociatedNodes() {
		Set<Class<? extends Node<?>>> ret = new HashSet<>();
		ret.add(Scene.class);
		return ret;
	}

	@Override
	public void save() {
		scene.setContent(htmlEditor.getHtmlText());
		setClean();
	}
	
	
}
