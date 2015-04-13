package de.himbiss.scrawl.util;

import java.awt.Color;

import com.lowagie.text.Font;
import com.lowagie.text.rtf.style.RtfFont;

import de.himbiss.scrawl.editors.manuscripteditor.StyleInfo;

public class StyleInfoHelper {

	public static Font toFont(StyleInfo info) {
		return info==null ? null : new RtfFont(info.getFontFamily(),info.getFontSize(),getStyle(info), getColor(info));
	}
	
	public static StyleInfo toStyleInfo(Font font) {
		return null;
	}
	
	private static int getStyle(StyleInfo info) {
		int style = Font.NORMAL;
		if(info != null) {
			style |= info.getBold() ? Font.BOLD : Font.NORMAL;
			style |= info.getItalic() ? Font.ITALIC : Font.NORMAL;
			style |= info.getStrikethrough() ? Font.STRIKETHRU : Font.NORMAL;
			style |= info.getUnderline() ? Font.UNDERLINE : Font.NORMAL;
		}
		return style;
	}
	
	private static Color getColor(StyleInfo info) {
		javafx.scene.paint.Color col = info.getTextColor();
		return new Color((float) col.getRed(), (float) col.getGreen(), (float) col.getBlue());
		
	}
}
