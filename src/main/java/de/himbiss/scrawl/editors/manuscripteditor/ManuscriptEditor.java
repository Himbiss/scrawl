package de.himbiss.scrawl.editors.manuscripteditor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.inject.Inject;

import org.fxmisc.richtext.InlineStyleTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyledDocument;
import org.reactfx.SuspendableNo;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfWriter2;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStringSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;

import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.project.Node;
import de.himbiss.scrawl.project.NodeType;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.StyleInfoHelper;

public class ManuscriptEditor extends NodeEditor {

	@Inject
	EditorManager editorManager;

	private Node<?> node;
	private final SuspendableNo updatingToolbar = new SuspendableNo();

	
	private final InlineStyleTextArea<StyleInfo> area = new InlineStyleTextArea<StyleInfo>(
			StyleInfo.EMPTY.updateFontSize(12).updateFontFamily("Serif")
					.updateTextColor(Color.BLACK), style -> style.toCss());

	public ManuscriptEditor() {
		area.setWrapText(true);
	}

	@Override
	public void createContent(AnchorPane anchorPane) {
		AnchorPane.setTopAnchor(area, 0d);
		AnchorPane.setBottomAnchor(area, 0d);
		AnchorPane.setRightAnchor(area, 0d);
		AnchorPane.setLeftAnchor(area, 0d);
		area.onKeyPressedProperty().setValue((e) -> {
			if (e.isControlDown() && e.getCode() == KeyCode.S) {
				save();
			} else if (!e.isMetaDown()) {
				setDirty();
			}
		});
		CheckBox wrapToggle = new CheckBox("Wrap");
		wrapToggle.setSelected(true);
		area.wrapTextProperty().bind(wrapToggle.selectedProperty());
		Button undoBtn = createButton("undo", () -> area.undo());
		Button redoBtn = createButton("redo", () -> area.redo());
		Button cutBtn = createButton("cut", () -> area.cut());
		Button copyBtn = createButton("copy", () -> area.copy());
		Button pasteBtn = createButton("paste", () -> area.paste());
		Button boldBtn = createButton("bold", () -> toggleBold());
		Button italicBtn = createButton("italic", () -> toggleItalic());
		Button underlineBtn = createButton("underline", () -> toggleUnderline());
		Button strikeBtn = createButton("strikethrough",
				() -> toggleStrikethrough());
		ComboBox<Integer> sizeCombo = new ComboBox<>(
				FXCollections.observableArrayList(5, 6, 7, 8, 9, 10, 11, 12,
						13, 14, 16, 18, 20, 22, 24, 28, 32, 36, 40, 48, 56, 64,
						72));
		sizeCombo.getSelectionModel().select(Integer.valueOf(12));
		ComboBox<String> familyCombo = new ComboBox<>(
				FXCollections.observableList(Font.getFamilies()));
		familyCombo.getSelectionModel().select("Serif");
		ColorPicker textColorPicker = new ColorPicker(Color.BLACK);
		sizeCombo.setOnAction(evt -> updateFontSize(sizeCombo.getValue()));
		familyCombo
				.setOnAction(evt -> updateFontFamily(familyCombo.getValue()));
		textColorPicker.valueProperty().addListener(
				(o, old, color) -> updateTextColor(color));
		undoBtn.disableProperty().bind(
				Bindings.not(area.undoAvailableProperty()));
		redoBtn.disableProperty().bind(
				Bindings.not(area.redoAvailableProperty()));
		BooleanBinding selectionEmpty = new BooleanBinding() {
			{
				bind(area.selectionProperty());
			}

			@Override
			protected boolean computeValue() {
				return area.getSelection().getLength() == 0;
			}
		};
		cutBtn.disableProperty().bind(selectionEmpty);
		copyBtn.disableProperty().bind(selectionEmpty);
		area.beingUpdatedProperty().addListener(
				(o, old, beingUpdated) -> {
					if (!beingUpdated) {
						boolean bold, italic, underline, strike;
						Integer fontSize;
						String fontFamily;
						Color textColor;
						IndexRange selection = area.getSelection();
						if (selection.getLength() != 0) {
							StyleSpans<StyleInfo> styles = area
									.getStyleSpans(selection);
							bold = styles.styleStream().anyMatch(
									s -> s.bold.orElse(false));
							italic = styles.styleStream().anyMatch(
									s -> s.italic.orElse(false));
							underline = styles.styleStream().anyMatch(
									s -> s.underline.orElse(false));
							strike = styles.styleStream().anyMatch(
									s -> s.strikethrough.orElse(false));
							int[] sizes = styles.styleStream()
									.mapToInt(s -> s.fontSize.orElse(-1))
									.distinct().toArray();
							fontSize = sizes.length == 1 ? sizes[0] : -1;
							String[] families = styles.styleStream()
									.map(s -> s.fontFamily.orElse(null))
									.distinct().toArray(i -> new String[i]);
							fontFamily = families.length == 1 ? families[0]
									: null;
							Color[] colors = styles.styleStream()
									.map(s -> s.textColor.orElse(null))
									.distinct().toArray(i -> new Color[i]);
							textColor = colors.length == 1 ? colors[0] : null;
						} else {
							int p = area.getCurrentParagraph();
							int col = area.getCaretColumn();
							StyleInfo style = area.getStyleAtPosition(p, col);
							bold = style.bold.orElse(false);
							italic = style.italic.orElse(false);
							underline = style.underline.orElse(false);
							strike = style.strikethrough.orElse(false);
							fontSize = style.fontSize.orElse(-1);
							fontFamily = style.fontFamily.orElse(null);
							textColor = style.textColor.orElse(null);
						}
						updatingToolbar
								.suspendWhile(() -> {
									if (bold) {
										if (!boldBtn.getStyleClass().contains(
												"pressed")) {
											boldBtn.getStyleClass().add(
													"pressed");
										}
									} else {
										boldBtn.getStyleClass().remove(
												"pressed");
									}
									if (italic) {
										if (!italicBtn.getStyleClass()
												.contains("pressed")) {
											italicBtn.getStyleClass().add(
													"pressed");
										}
									} else {
										italicBtn.getStyleClass().remove(
												"pressed");
									}
									if (underline) {
										if (!underlineBtn.getStyleClass()
												.contains("pressed")) {
											underlineBtn.getStyleClass().add(
													"pressed");
										}
									} else {
										underlineBtn.getStyleClass().remove(
												"pressed");
									}
									if (strike) {
										if (!strikeBtn.getStyleClass()
												.contains("pressed")) {
											strikeBtn.getStyleClass().add(
													"pressed");
										}
									} else {
										strikeBtn.getStyleClass().remove(
												"pressed");
									}
									if (fontSize != -1) {
										sizeCombo.getSelectionModel().select(
												fontSize);
									} else {
										sizeCombo.getSelectionModel()
												.clearSelection();
									}
									if (fontFamily != null) {
										familyCombo.getSelectionModel().select(
												fontFamily);
									} else {
										familyCombo.getSelectionModel()
												.clearSelection();
									}
									if (textColor != null) {
										textColorPicker.setValue(textColor);
									}
								});
					}
				});
		HBox panel = new HBox(3.0);
		panel.getChildren().addAll(wrapToggle, undoBtn, redoBtn, cutBtn,
				copyBtn, pasteBtn, boldBtn, italicBtn, underlineBtn, strikeBtn,
				sizeCombo, familyCombo, textColorPicker);
		VBox vbox = new VBox();
		VBox.setVgrow(area, Priority.ALWAYS);
		vbox.getChildren().addAll(panel, area);
		AnchorPane.setTopAnchor(vbox, 0d);
		AnchorPane.setBottomAnchor(vbox, 0d);
		AnchorPane.setRightAnchor(vbox, 0d);
		AnchorPane.setLeftAnchor(vbox, 0d);
		anchorPane.getChildren().add(vbox);
	}

	private Button createButton(String styleClass, Runnable action) {
		Button button = new Button();
		button.getStyleClass().add(styleClass);
		button.setOnAction((evt) -> {
			action.run();
			area.requestFocus();
		});
		button.setPrefWidth(20);
		button.setPrefHeight(20);
		return button;
	}

	@Override
	public String getEditorId() {
		return Constants.MANUSCRIPT_EDITOR;
	}

	@Override
	public void initialize(Node<?> node) {
		this.node = node;
		
		IRtfSource src = new RtfStringSource(node.getContent());
		
		StandardRtfParser parser = new StandardRtfParser();
		
		try {
			parser.parse(src, new RtfListener(area));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		StyledDocument<StyleInfo> doc = area.getDocument();
		 // Create Document object
        Document document = new Document();
        String rtfStr = "";
        
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();){

            RtfWriter2.getInstance(document, stream);
            
            // open the document object
            document.open();
            
            for(org.fxmisc.richtext.Paragraph<StyleInfo> pa : doc.getParagraphs()) {
            	Paragraph newPar = new Paragraph();
            	pa.getSegments().stream().forEach( (t) -> {
                	try {
                		Chunk ch = new Chunk(t.toString());
                		ch.setFont(StyleInfoHelper.toFont(t.getStyle()));
                		newPar.add(ch);
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}); 
				document.add(newPar);
            }
            
            //close the document
            document.close();
            rtfStr = new String(stream.toByteArray());
        }
        catch(Exception e) {
        	e.printStackTrace();
        }

		node.setContent(rtfStr);
		setClean();
	}

	private void toggleBold() {
		updateStyleInSelection(spans -> StyleInfo.EMPTY.updateBold(!spans
				.styleStream().allMatch(style -> style.bold.orElse(false))));
	}

	private void toggleItalic() {
		updateStyleInSelection(spans -> StyleInfo.EMPTY.updateItalic(!spans
				.styleStream().allMatch(style -> style.italic.orElse(false))));
	}

	private void toggleUnderline() {
		updateStyleInSelection(spans -> StyleInfo.EMPTY
				.updateUnderline(!spans.styleStream().allMatch(
						style -> style.underline.orElse(false))));
	}

	private void toggleStrikethrough() {
		updateStyleInSelection(spans -> StyleInfo.EMPTY
				.updateStrikethrough(!spans.styleStream().allMatch(
						style -> style.strikethrough.orElse(false))));
	}

	private void updateStyleInSelection(
			Function<StyleSpans<StyleInfo>, StyleInfo> mixinGetter) {
		IndexRange selection = area.getSelection();
		if (selection.getLength() != 0) {
			StyleSpans<StyleInfo> styles = area.getStyleSpans(selection);
			StyleInfo mixin = mixinGetter.apply(styles);
			StyleSpans<StyleInfo> newStyles = styles.mapStyles(style -> style
					.updateWith(mixin));
			area.setStyleSpans(selection.getStart(), newStyles);
		}
	}

	private void updateStyleInSelection(StyleInfo mixin) {
		IndexRange selection = area.getSelection();
		if (selection.getLength() != 0) {
			StyleSpans<StyleInfo> styles = area.getStyleSpans(selection);
			StyleSpans<StyleInfo> newStyles = styles.mapStyles(style -> style
					.updateWith(mixin));
			area.setStyleSpans(selection.getStart(), newStyles);
		}
	}

	private void updateFontSize(Integer size) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(StyleInfo.fontSize(size));
		}
	}

	private void updateFontFamily(String family) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(StyleInfo.fontFamily(family));
		}
	}

	private void updateTextColor(Color color) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(StyleInfo.textColor(color));
		}
	}

}
