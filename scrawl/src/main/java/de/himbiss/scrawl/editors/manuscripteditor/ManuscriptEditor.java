package de.himbiss.scrawl.editors.manuscripteditor;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;

import javax.inject.Inject;

import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.gui.EditorKeyEventHandler;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.NodeType;
import de.himbiss.scrawl.project.Scene;

public class ManuscriptEditor extends NodeEditor {

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
	}

	@Override
	protected boolean validate(Node<?> node) {
		return node.getNodeType() == NodeType.SCENE;
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
