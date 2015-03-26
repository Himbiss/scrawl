package de.himbiss.scrawl.dao;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.himbiss.scrawl.model.project.Folder;

@XmlRootElement
public class FolderWrapper<T> {

	private Folder<T> folder;
	
	public FolderWrapper() {
		folder = new Folder<T>();
	}
	
	public FolderWrapper(Folder<T> folder) {
		this.folder = folder;
	}
	
	@XmlAnyElement(lax=true)
	public Folder<T> getFolder() {
		return folder;
	}
}
