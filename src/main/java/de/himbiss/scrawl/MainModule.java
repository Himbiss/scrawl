package de.himbiss.scrawl;

import com.google.inject.AbstractModule;

import de.himbiss.scrawl.dao.IProjectDAO;
import de.himbiss.scrawl.dao.XMLProjectDAO;
import de.himbiss.scrawl.model.editors.EditorManager;
import de.himbiss.scrawl.model.project.ProjectManager;
import de.himbiss.scrawl.view.MainLayoutController;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MainLayoutController.class).asEagerSingleton();
		bind(EditorManager.class).asEagerSingleton();
		bind(ProjectManager.class).asEagerSingleton();
		bind(IProjectDAO.class).to(XMLProjectDAO.class).asEagerSingleton();;
	}

}
