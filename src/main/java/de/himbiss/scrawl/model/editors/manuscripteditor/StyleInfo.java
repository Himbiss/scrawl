package de.himbiss.scrawl.model.editors.manuscripteditor;

import java.util.Optional;

import javafx.scene.paint.Color;

public class StyleInfo {
	public static final StyleInfo EMPTY = new StyleInfo();

	public static StyleInfo fontSize(int fontSize) {
		return EMPTY.updateFontSize(fontSize);
	}

	public static StyleInfo fontFamily(String family) {
		return EMPTY.updateFontFamily(family);
	}

	public static StyleInfo textColor(Color color) {
		return EMPTY.updateTextColor(color);
	}

	final Optional<Boolean> bold;
	final Optional<Boolean> italic;
	final Optional<Boolean> underline;
	final Optional<Boolean> strikethrough;
	final Optional<Integer> fontSize;
	final Optional<String> fontFamily;
	final Optional<Color> textColor;

	public StyleInfo() {
		bold = Optional.empty();
		italic = Optional.empty();
		underline = Optional.empty();
		strikethrough = Optional.empty();
		fontSize = Optional.empty();
		fontFamily = Optional.empty();
		textColor = Optional.empty();
	}

	public StyleInfo(Optional<Boolean> bold, Optional<Boolean> italic,
			Optional<Boolean> underline, Optional<Boolean> strikethrough,
			Optional<Integer> fontSize, Optional<String> fontFamily,
			Optional<Color> textColor) {
		this.bold = bold;
		this.italic = italic;
		this.underline = underline;
		this.strikethrough = strikethrough;
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
		this.textColor = textColor;
	}

	public String toCss() {
		StringBuilder sb = new StringBuilder();
		if (bold.isPresent()) {
			if (bold.get()) {
				sb.append("-fx-font-weight: bold;");
			} else {
				sb.append("-fx-font-weight: normal;");
			}
		}
		if (italic.isPresent()) {
			if (italic.get()) {
				sb.append("-fx-font-style: italic;");
			} else {
				sb.append("-fx-font-style: normal;");
			}
		}
		if (underline.isPresent()) {
			if (underline.get()) {
				sb.append("-fx-underline: true;");
			} else {
				sb.append("-fx-underline: false;");
			}
		}
		if (strikethrough.isPresent()) {
			if (strikethrough.get()) {
				sb.append("-fx-strikethrough: true;");
			} else {
				sb.append("-fx-strikethrough: false;");
			}
		}
		if (fontSize.isPresent()) {
			sb.append("-fx-font-size: " + fontSize.get() + "pt;");
		}
		if (fontFamily.isPresent()) {
			sb.append("-fx-font-family: " + fontFamily.get() + ";");
		}
		if (textColor.isPresent()) {
			Color color = textColor.get();
			int red = (int) (color.getRed() * 255);
			int green = (int) (color.getGreen() * 255);
			int blue = (int) (color.getBlue() * 255);
			sb.append("-fx-fill: rgb(" + red + ", " + green + ", " + blue + ")");
		}
		return sb.toString();
	}

	public StyleInfo updateWith(StyleInfo mixin) {
		return new StyleInfo(mixin.bold.isPresent() ? mixin.bold : bold,
				mixin.italic.isPresent() ? mixin.italic : italic,
				mixin.underline.isPresent() ? mixin.underline : underline,
				mixin.strikethrough.isPresent() ? mixin.strikethrough
						: strikethrough,
				mixin.fontSize.isPresent() ? mixin.fontSize : fontSize,
				mixin.fontFamily.isPresent() ? mixin.fontFamily : fontFamily,
				mixin.textColor.isPresent() ? mixin.textColor : textColor);
	}

	public StyleInfo updateBold(boolean bold) {
		return new StyleInfo(Optional.of(bold), italic, underline,
				strikethrough, fontSize, fontFamily, textColor);
	}

	public StyleInfo updateItalic(boolean italic) {
		return new StyleInfo(bold, Optional.of(italic), underline,
				strikethrough, fontSize, fontFamily, textColor);
	}

	public StyleInfo updateUnderline(boolean underline) {
		return new StyleInfo(bold, italic, Optional.of(underline),
				strikethrough, fontSize, fontFamily, textColor);
	}

	public StyleInfo updateStrikethrough(boolean strikethrough) {
		return new StyleInfo(bold, italic, underline,
				Optional.of(strikethrough), fontSize, fontFamily, textColor);
	}

	public StyleInfo updateFontSize(int fontSize) {
		return new StyleInfo(bold, italic, underline, strikethrough,
				Optional.of(fontSize), fontFamily, textColor);
	}

	public StyleInfo updateFontFamily(String fontFamily) {
		return new StyleInfo(bold, italic, underline, strikethrough, fontSize,
				Optional.of(fontFamily), textColor);
	}

	public StyleInfo updateTextColor(Color textColor) {
		return new StyleInfo(bold, italic, underline, strikethrough, fontSize,
				fontFamily, Optional.of(textColor));
	}
}