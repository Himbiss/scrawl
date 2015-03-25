package de.himbiss.scrawl.model.project;

import java.io.File;
import java.util.prefs.Preferences;

import javax.inject.Inject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.model.Constants;
import de.himbiss.scrawl.view.MainLayoutController;

public class ProjectManager {
	
	@Inject
	MainLayoutController mainController;
	
	Logger logger = LogManager.getLogger(ProjectManager.class);
	
	public File getProjectPath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String path = prefs.get(Constants.PROJECT_LOCATION, null);
		if(path != null) {
			return new File(path);
		} else {
			return new File(System.getProperty(Constants.USER_HOME));
		}
	}
	
	public void setProjectPath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put(Constants.PROJECT_LOCATION, file.getPath());
        } else {
            prefs.remove(Constants.PROJECT_LOCATION);
        }
    }
	
	private Project parseProject(File projectRoot) {
		logger.log(Level.INFO, "Parsing the project");
		Project project = new Project("Testproject1");
		project.getPersons().add(new Person("Hänsel"));
		project.getPersons().add(new Person("Gretel"));
		project.getLocations().add(new Location("Pfefferkuchenhaus"));
		project.getLocations().add(new Location("Wald"));
		project.getObjects().add(new Object("Ofen"));
		project.getScenes().add(new Scene("Kapitel1"));
		return project;
	}
	
	public boolean loadProject() {
		File projectRoot = getProjectPath();
		if(projectRoot != null) {
			logger.log(Level.INFO, "Loading project from location: " + projectRoot.getAbsolutePath());
			Project project = parseProject(projectRoot);
			mainController.setProject(project);
			return true;
		}
		logger.log(Level.ERROR, "Error loading project, could not retrieve project path");
		return false;
	}
}
