package extensions;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import de.himbiss.scrawl.editors.NodeEditor;
import de.himbiss.scrawl.project.Location;
import de.himbiss.scrawl.project.Node;


public class SampleEditorExtension extends NodeEditor{

	private Location location;
	
	public SampleEditorExtension() {
	}
	
	@Override
	protected void initialize(Node<?> content) {
		location = (Location) content;
	}

	@Override
	protected void createContent(AnchorPane anchorPane) {
		anchorPane.getChildren().add(new Label("This is an example editor extension!"));
	}

	@Override
	protected boolean validate(Node<?> node) {
		return node instanceof Location;
	}

	@Override
	protected Node<?> getNode() {
		return location;
	}

	@Override
	public Set<Class<? extends Node<?>>> getAssociatedNodes() {
		Set<Class<? extends Node<?>>> ret = new HashSet<>();
		ret.add(Location.class);
		return ret;
	}

	@Override
	public void save() {
		setClean();
	}

}
