package de.himbiss.scrawl.project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import de.himbiss.scrawl.dao.IProjectDAO;
import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.gui.layout_controller.IVisualizesProject;

/**
 * The class <code>NodeHierarchyTest</code> contains tests for the class {@link
 * <code>ProjectManager</code>}.
 *
 * @pattern JUnit Test Case
 *
 * @author vincent
 *
 * @version $Revision$
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectManagerTest {

	@Mock
	IProjectDAO projectDAO;
	
	@Mock
	IVisualizesProject mainView;
	
	@Mock
	EditorManager editorManager;
	
	@InjectMocks
	ProjectManager manager;
	
	Project project;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		project = new Project("testProject");
		
		project.getLocations().add(new Location("location"));
		project.getScenes().add(new Scene("scene"));
		
		when(projectDAO.createProject(Mockito.anyString())).thenReturn(project);
		
		manager.initialize();
		
	}
	
	/**
	 * Run the testHandleDeleteNode test
	 */
	@Test
	public void testHandleDeleteNode() {
		verify(mainView).setProject(Mockito.any());
		Scene undeletableScene = NodeFactory.createUniqueScene();
		Scene deletableScene = NodeFactory.createUniqueScene();
		
		undeletableScene.setDeletable(false);
		deletableScene.setDeletable(true);
		
		project.getScenes().add(undeletableScene);
		project.getScenes().add(deletableScene);

		assertFalse(manager.handleDeleteNode(undeletableScene));
		assertTrue(manager.handleDeleteNode(deletableScene));
	}
	
	/**
	 * Run the testFolderRemoveNodes test
	 */
	@Test
	public void testHandleNewFolder() {
		verify(mainView).setProject(Mockito.any());
		assertTrue(project.getObjects().getComponents().isEmpty());
		
		assertTrue(manager.handleNewFolder(project.getObjects()));
		
		Node<?> objNode = project.getObjects().getComponents().get(0); 
		assertTrue(objNode.isFolder());
	}

	/**
	 * Run the testFolderAddNodes test
	 */
	@Test
	public void testHandleNewNode() {
		verify(mainView).setProject(Mockito.any());
		// test adding a new node to the root folder
		assertTrue(manager.handleNewNode(project.getObjects()));
		Node<?> objNewNode1 = project.getObjects().getComponents().get(0);
		// test that adding a new node to a node object that is not a folder,
		// the node is added to the parent folder
		assertTrue(manager.handleNewNode(objNewNode1));
		Node<?> objNewNode2 = project.getObjects().getComponents().get(1); 
		assertTrue(project.getObjects().equals(objNewNode2.getParent()));
	}
}