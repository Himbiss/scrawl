package de.himbiss.scrawl.dao;

import java.util.List;

import de.himbiss.scrawl.model.project.Project;

/**
 * DAO Interface for accessing projects
 * 
 * @author Vincent Ortland
 * @email vincent.ortland@gmx.de
 *
 */
public interface IProjectDAO {
	
	/**
	 * Returns a list with all project namesj found
	 * @return found
	 */
	public List<String> findAll();

	/**
	 * Loads and returns a project
	 * @param name
	 * @return project
	 */
	public Project loadProject(String name);
	
	/**
	 * Deletes a project
	 */
	public void deleteProject(String name);
	
	/**
	 * Creates a new project
	 * @param name
	 */
	public Project createProject(String name);
	
	/**
	 * Saves a project
	 * @param project
	 */
	public void saveProject(Project project);

	/**
	 * Returns if the project exists
	 * @param name
	 * @return exists
	 */
	public boolean projectExists(String lastProjectName);
}
