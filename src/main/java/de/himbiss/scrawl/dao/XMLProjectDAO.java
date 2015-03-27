package de.himbiss.scrawl.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.himbiss.scrawl.MainApp;
import de.himbiss.scrawl.model.project.Project;
import de.himbiss.scrawl.util.Constants;
import de.himbiss.scrawl.util.NodeHelper;

public class XMLProjectDAO implements IProjectDAO {
	
	Logger logger = LogManager.getLogger(IProjectDAO.class);
	
	private String getWorkingDirectoryPath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		return prefs.get(Constants.WORKSPACE_PATH, System.getProperty(Constants.USER_HOME));
	}

	@Override
	public List<String> findAll() {
		File workingDirectory = new File(getWorkingDirectoryPath());
		List<File> files = Arrays.asList(workingDirectory.listFiles());
		List<String> found = new ArrayList<>();
		found.addAll(files.stream()
				.filter( (file) -> { return file.getPath().endsWith(Constants.FILE_EXTENSION); } )
				.map( (file) -> { return file.getName(); } )
				.collect(Collectors.toList()));
		return found;
	}

	@Override
	public Project loadProject(String name) {
		Project project = null;
		try {
			JAXBContext context = JAXBContext.newInstance(Project.class);
			Unmarshaller um = context.createUnmarshaller();
			String workingDirectory = getWorkingDirectoryPath();
			String path = workingDirectory + File.separator + name + Constants.FILE_EXTENSION;
			logger.log(Level.INFO, "Loading project " + name + " from "+path);
		    project = (Project) um.unmarshal(new FileReader(path));
		    NodeHelper.registerNodes(project.getScenes());
		    NodeHelper.registerNodes(project.getObjects());
		    NodeHelper.registerNodes(project.getLocations());
		    NodeHelper.registerNodes(project.getPersons());
		    NodeHelper.fixParent(project.getScenes());
		    NodeHelper.fixParent(project.getObjects());
		    NodeHelper.fixParent(project.getLocations());
		    NodeHelper.fixParent(project.getPersons());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return project;
	}

	@Override
	public void deleteProject(String name) {
		String workingDirectory = getWorkingDirectoryPath();
		String path = workingDirectory + File.separator + name + Constants.FILE_EXTENSION;
		File file = new File(path);
		if(file.exists() && !file.isDirectory()) {
			logger.log(Level.INFO, "Deleting project " + name + " at "+path);
			file.delete();
		}
	}

	@Override
	public Project createProject(String name) {
		Project project = new Project(name);
		saveProject(project);
		return project;
	}

	@Override
	public void saveProject(Project project) {
		try {
			JAXBContext context = JAXBContext.newInstance(Project.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			String workingDirectory = getWorkingDirectoryPath();
			String path = workingDirectory + File.separator + project.getIdentifier() + Constants.FILE_EXTENSION;
			logger.log(Level.INFO, "Saving project " + project.getIdentifier() + " to "+path);
		    m.marshal(project, new FileWriter(path));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean projectExists(String name) {
		String workingDirectory = getWorkingDirectoryPath();
		String path = workingDirectory + File.separator + name + Constants.FILE_EXTENSION;
		return (new File(path)).exists();
	}

	

}
