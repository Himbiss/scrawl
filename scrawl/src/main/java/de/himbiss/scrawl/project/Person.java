package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "person")
@XmlRootElement(name = "person")
public class Person extends Node<Person> {

	private static final long serialVersionUID = 3432993778254731881L;
	
	private String motivation;
	private String goal;
	private String conflict;
	private String epiphany;
	private String oneParagraphStoryline;

	public Person() {
		super(Constants.NEW_PERSON, ContentType.PERSON);
		// for jaxb / serialization
	}
	
	public Person(String identifier) {
		super(identifier, ContentType.PERSON);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	public String getName() {
		return getIdentifier();
	}

	public void setName(String name) {
		setIdentifier(name);
	}

	public String getOneSentenceSummary() {
		return getDescription();
	}

	public void setOneSentenceSummary(String oneSentenceSummary) {
		setDescription(oneSentenceSummary);
	}

	@XmlElement
	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	
	@XmlElement
	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	@XmlElement
	public String getConflict() {
		return conflict;
	}

	public void setConflict(String conflict) {
		this.conflict = conflict;
	}

	@XmlElement
	public String getEpiphany() {
		return epiphany;
	}

	public void setEpiphany(String epiphany) {
		this.epiphany = epiphany;
	}

	@XmlElement
	public String getOneParagraphStoryline() {
		return oneParagraphStoryline;
	}

	public void setOneParagraphStoryline(String oneParagraphStoryline) {
		this.oneParagraphStoryline = oneParagraphStoryline;
	}
	
	public void copyAttributes(Person person) {
		setName(person.getName());
		setOneSentenceSummary(person.getOneSentenceSummary());
		setMotivation(person.getMotivation());
		setGoal(person.getGoal());
		setConflict(person.getConflict());
		setEpiphany(person.getEpiphany());
		setOneParagraphStoryline(person.getOneParagraphStoryline());
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
