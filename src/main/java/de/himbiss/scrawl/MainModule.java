package de.himbiss.scrawl;

import com.google.inject.AbstractModule;

import de.himbiss.scrawl.model.ProjectManager;
import de.himbiss.scrawl.model.editors.EditorManager;
import de.himbiss.scrawl.view.MainLayoutController;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MainLayoutController.class).asEagerSingleton();
		bind(EditorManager.class).asEagerSingleton();
		bind(ProjectManager.class).asEagerSingleton();
	}

}
