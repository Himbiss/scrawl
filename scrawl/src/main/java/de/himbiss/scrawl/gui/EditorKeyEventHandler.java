package de.himbiss.scrawl.gui;

import de.himbiss.scrawl.editors.NodeEditor;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditorKeyEventHandler implements EventHandler<KeyEvent> {

	private NodeEditor editor;
	
	public static EditorKeyEventHandler create(NodeEditor editor) {
		return new EditorKeyEventHandler(editor);
	}
	
	public EditorKeyEventHandler(NodeEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public void handle(KeyEvent event) {
		if (event.isControlDown() && event.getCode() == KeyCode.S) {
			editor.save();
		} else if ( !event.isMetaDown() && 
					!event.isControlDown() && 
					!event.isAltDown() && 
					!event.isShortcutDown() && 
					!event.isShiftDown()) {
			editor.setDirty();
		}
	}

}
