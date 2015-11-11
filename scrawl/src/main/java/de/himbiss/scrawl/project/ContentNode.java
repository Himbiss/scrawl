package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlElement;

import de.himbiss.scrawl.project.content.IContent;

public class ContentNode extends Node implements IContentHolder {
	
	private IContent content;

	public ContentNode(IContent content) {
		super(content.getIdentifier(), true, true);
		this.content = content;
	}

	@XmlElement(name = "content")
	public IContent getContent() {
		return content;
	}

	public void setContent(IContent content) {
		this.content = content;
		notifyObservers();
	}
	
	@Override
	public boolean isFolder() {
		// TODO Auto-generated method stub
		return false;
	}

}
