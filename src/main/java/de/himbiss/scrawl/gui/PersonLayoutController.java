package de.himbiss.scrawl.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import de.himbiss.scrawl.project.NodeFactory;
import de.himbiss.scrawl.project.Person;
import de.himbiss.scrawl.util.Constants;

public class PersonLayoutController implements Initializable {

	private Person person;
	
	private @FXML TextField name;
	private @FXML TextArea oneSentenceSummary;
	private @FXML TextArea motivation;
	private @FXML TextArea goal;
	private @FXML TextArea conflict;
	private @FXML TextArea epiphany;
	private @FXML TextArea oneParagraphStoryline;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		person = NodeFactory.createPerson(Constants.NEW_PERSON);
	}
	
	public void setPerson(Person person, Consumer<Person> onTextChange) {
		this.person.copyAttributes(person);
		name.setText(person.getName());
		oneSentenceSummary.setText(person.getOneSentenceSummary());
		motivation.setText(person.getMotivation());
		goal.setText(person.getGoal());
		conflict.setText(person.getConflict());
		epiphany.setText(person.getEpiphany());
		oneParagraphStoryline.setText(person.getOneParagraphStoryline());
		javafx.beans.value.ChangeListener<String> handler = (o,so,sn) -> {
			person.setName(name.getText());
			person.setOneSentenceSummary(oneSentenceSummary.getText());
			person.setMotivation(motivation.getText());
			person.setGoal(goal.getText());
			person.setConflict(conflict.getText());
			person.setEpiphany(epiphany.getText());
			person.setOneParagraphStoryline(oneParagraphStoryline.getText());
			onTextChange.accept(person);
		};
		name.textProperty().addListener(handler);
		oneSentenceSummary.textProperty().addListener(handler);
		motivation.textProperty().addListener(handler);
		goal.textProperty().addListener(handler);
		conflict.textProperty().addListener(handler);
		epiphany.textProperty().addListener(handler);
		oneParagraphStoryline.textProperty().addListener(handler);
	}
	
	public Person getPerson() {
		return person;
	}
}
