package de.himbiss.scrawl;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.inject.Inject;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.cathive.fx.guice.GuiceFXMLLoader.Result;
import com.google.inject.Module;

import de.himbiss.scrawl.editors.EditorManager;
import de.himbiss.scrawl.gui.MainLayoutController;
import de.himbiss.scrawl.project.ProjectManager;
import de.himbiss.scrawl.util.Constants;

public class MainApp extends GuiceApplication {

	private Stage primaryStage;
	
	@Inject 
	private MainLayoutController rootController;

	@Inject
	private GuiceFXMLLoader fxmlLoader;
	
	@Inject
	private ProjectManager projectManager;

	@Inject
	private EditorManager EditorManager;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		Parent root = fxmlLoader.load(getClass().getResource("gui/MainLayout.fxml")).getRoot();

		Scene scene = new Scene(root);
		scene.setRoot(root);

		primaryStage.setScene(scene);

		showMainLayout();

		primaryStage.setTitle(Constants.TITLE);
		primaryStage.show();
		
		projectManager.initialize();
		EditorManager.initialize();
	}
	
	void showMainLayout() {
		try {
			// Load root layout from fxml file.
			Result result = fxmlLoader.load(MainApp.class.getResource("gui/MainLayout.fxml"));

			BorderPane rootPane = (BorderPane) result.getRoot();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootPane);
			scene.getStylesheets().add(MainApp.class.getClassLoader().getResource(Constants.CSS_MANUSCRIPT_EDITOR).toExternalForm());
			primaryStage.setScene(scene);

			rootController.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void init(List<Module> modules) throws Exception {
		modules.add(new MainModule());
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
