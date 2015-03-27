package de.himbiss.scrawl.editors.manuscripteditor;

import javax.inject.Inject;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import org.fxmisc.richtext.InlineStyleTextArea;

import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.NodeType;
import de.himbiss.scrawl.util.Constants;

public class ManuscriptEditor extends NodeEditor {

	@Inject
	EditorManager editorManager;

	private Node<?> node;

	private final InlineStyleTextArea<StyleInfo> area = new InlineStyleTextArea<StyleInfo>(
			StyleInfo.EMPTY.updateFontSize(12).updateFontFamily("Serif")
					.updateTextColor(Color.BLACK), style -> style.toCss());

	public ManuscriptEditor() {
		area.setWrapText(true);
	}

	@Override
	public void createContent(AnchorPane anchorPane) {
		anchorPane.getChildren().add(area);
		AnchorPane.setTopAnchor(area, 0d);
		AnchorPane.setBottomAnchor(area, 0d);
		AnchorPane.setRightAnchor(area, 0d);
		AnchorPane.setLeftAnchor(area, 0d);
		area.replaceText(node.getContent());
		area.onKeyPressedProperty().setValue((e) -> {
			if (e.isControlDown() && e.getCode() == KeyCode.S) {
				save();
			} else if (!e.isMetaDown()) {
				setDirty();
			}
		});
	}

	@Override
	public String getEditorId() {
		return Constants.MANUSCRIPT_EDITOR;
	}

	@Override
	public void initialize(Node<?> node) {
		this.node = node;
	}

	@Override
	public boolean validate(Node<?> node) {
		return node.getNodeType() == NodeType.SCENE;
	}

	@Override
	public Node<?> getNode() {
		return node;
	}

	@Override
	public void save() {
		node.setContent(area.getText());
		setClean();
	}

}
