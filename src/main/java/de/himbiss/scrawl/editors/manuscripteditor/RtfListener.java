package de.himbiss.scrawl.editors.manuscripteditor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.fxmisc.richtext.InlineStyleTextArea;
import org.fxmisc.richtext.LineTerminator;
import org.fxmisc.richtext.Paragraph;
import org.fxmisc.richtext.StyledText;

import com.rtfparserkit.parser.RtfListenerAdaptor;
import com.rtfparserkit.rtf.Command;
import com.rtfparserkit.rtf.CommandType;

public class RtfListener extends RtfListenerAdaptor {

	private InlineStyleTextArea<StyleInfo> area;
	private List<Paragraph<StyleInfo>> paragraphs;

	private Command currentDestination = Command.rtf;
	private final Deque<Command> destinationStack = new ArrayDeque<Command>();
	private StyleInfo currentStyleInfo = new StyleInfo();
	private Paragraph<StyleInfo> currentParagraph = new Paragraph<StyleInfo>(
			"", currentStyleInfo);

	public RtfListener(InlineStyleTextArea<StyleInfo> area) {
		this.area = area;
		this.paragraphs = new ArrayList<Paragraph<StyleInfo>>();
		this.paragraphs.add(currentParagraph);
	}

	@Override
	public void processDocumentEnd() {
		area.append(RtfWrapperDocument.createRtfDocument(paragraphs));
	}

	@Override
	public void processGroupStart() {
		destinationStack.push(currentDestination);
	}

	@Override
	public void processGroupEnd() {
		currentDestination = destinationStack.pop();
	}

	@Override
	public void processString(String string) {

		switch (currentDestination) {
		case rtf:
		case pntext:
		case fldrslt: {
			System.out.println("STR" + string);
			StyledText<StyleInfo> txt = new StyledText<StyleInfo>(string,
					currentStyleInfo);
			currentParagraph = currentParagraph.concat(txt);
			break;
		}
		default: {
			// Do nothing
			break;
		}
		}
	}

	@Override
	public void processCommand(Command command, int parameter,
			boolean hasParameter, boolean optional) {
		if (command.getCommandType() == CommandType.Destination) {
			currentDestination = command;
		}
		switch (command) {
		case par: {
			currentParagraph.terminate(LineTerminator.CRLF);
			paragraphs.add(currentParagraph);
			currentStyleInfo = new StyleInfo();
			currentParagraph = new Paragraph<StyleInfo>("\n", currentStyleInfo);
			break;
		}
		case line:
		case row: {
			currentParagraph.terminate(LineTerminator.CR);
			break;
		}
		case tab:
		case cell: {
			currentParagraph.terminate(LineTerminator.CR);
			break;
		}
		case pard:
		case plain: {
			currentStyleInfo = new StyleInfo();
			currentParagraph = currentParagraph.restyle(currentStyleInfo);
			break;
		}
		case i: {
			currentStyleInfo = currentStyleInfo.updateItalic(true);
			currentParagraph = currentParagraph.restyle(currentStyleInfo);
		}
		case ul: {
			currentStyleInfo = currentStyleInfo.updateUnderline(true);
			currentParagraph = currentParagraph.restyle(currentStyleInfo);
		}
		case b: {
			currentStyleInfo = currentStyleInfo.updateBold(true);
			currentParagraph = currentParagraph.restyle(currentStyleInfo);
		}
		case strike: {
			currentStyleInfo = currentStyleInfo.updateStrikethrough(true);
			currentParagraph = currentParagraph.restyle(currentStyleInfo);
		}
		case fs: {
			if (hasParameter && parameter > 0) {
				currentStyleInfo = currentStyleInfo
						.updateFontSize(parameter / 2);
				currentParagraph = currentParagraph.restyle(currentStyleInfo);
			}
		}
		default: {
			// Do nothing
			break;
		}
		}
	}
}
