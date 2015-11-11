package de.himbiss.scrawl.project.content;

import de.himbiss.scrawl.project.ContentType;

public interface IContent {

	String getIdentifier();
	
	void setIdentifier();
	
	String getContent();
	
	void setContent(String content);
	
	ContentType getContentType();
	
}
