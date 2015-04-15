package de.himbiss.scrawl.editors.manuscripteditor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.IndexRange;

import org.fxmisc.richtext.Paragraph;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyledDocument;


public class RtfWrapperDocument implements StyledDocument<StyleInfo> {

	private List<Paragraph<StyleInfo>> paragraphs;
	private int length;
	
	public RtfWrapperDocument() {
		paragraphs = new ArrayList<Paragraph<StyleInfo>>();
	}
	
	public static StyledDocument<StyleInfo> createRtfDocument(List<Paragraph<StyleInfo>> paragraphs) {
		RtfWrapperDocument doc = new RtfWrapperDocument();
		doc.setParagraphs(paragraphs);
		doc.length();
		return doc;
	}
	
	public void setParagraphs(List<Paragraph<StyleInfo>> paragraphs) {
		this.paragraphs = paragraphs;
	}
	
	@Override
	public int length() {
		length = 0;
		paragraphs.stream().forEach( (p) -> { length += p.fullLength(); } );
		return length;
	}

	@Override
	public char charAt(int index) {
		return 0;
	}

	@Override
	public Position position(int major, int minor) {
		return null;
	}

	@Override
	public Position offsetToPosition(int offset, Bias bias) {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public String getText(int start, int end) {
		return null;
	}

	@Override
	public String getText(IndexRange range) {
		return null;
	}

	@Override
	public StyledDocument<StyleInfo> subSequence(int start, int end) {
		return null;
	}

	@Override
	public StyledDocument<StyleInfo> subSequence(IndexRange range) {
		return null;
	}

	@Override
	public StyledDocument<StyleInfo> subDocument(int paragraphIndex) {
		return null;
	}

	@Override
	public StyledDocument<StyleInfo> concat(StyledDocument<StyleInfo> latter) {
		return null;
	}

	@Override
	public StyleInfo getStyleOfChar(int index) {
		return null;
	}

	@Override
	public StyleInfo getStyleOfChar(int paragraph, int column) {
		return null;
	}

	@Override
	public StyleInfo getStyleAtPosition(int position) {
		return null;
	}

	@Override
	public StyleInfo getStyleAtPosition(int paragraph, int position) {
		return null;
	}

	@Override
	public IndexRange getStyleRangeAtPosition(int position) {
		return null;
	}

	@Override
	public IndexRange getStyleRangeAtPosition(int paragraph, int position) {
		return null;
	}

	@Override
	public StyleSpans<StyleInfo> getStyleSpans(int from, int to) {
		return null;
	}

	@Override
	public StyleSpans<StyleInfo> getStyleSpans(int paragraph) {
		return null;
	}

	@Override
	public StyleSpans<StyleInfo> getStyleSpans(int paragraph, int from, int to) {
		return null;
	}

	@Override
	public List<Paragraph<StyleInfo>> getParagraphs() {
		return paragraphs;
	}

}
