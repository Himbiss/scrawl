package de.himbiss.scrawl.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import com.cathive.fx.guice.FXMLController;

import de.himbiss.scrawl.MainApp;

@FXMLController(controllerId = "mainController")
public final class MainLayoutController implements Initializable {

	MainApp mainApp;

	@FXML
	private TreeView<File> treeView;

	@FXML
	private TabPane tabPane;

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}

	/**
	 * Creates a new project
	 */
	@FXML
	private void handleNewProject() {
		System.out.println("new project");
	}

	/**
	 * Opens a new project
	 */
	@FXML
	private void handleOpenProject() {
		System.out.println("open project");
	}

	/**
	 * Saves the current open editor
	 */
	@FXML
	private void handleSaveCurrent() {
		System.out.println("save current");
	}

	/**
	 * Saves all open editors
	 */
	@FXML
	private void handleSaveAll() {
		System.out.println("save all");
	}

	/**
	 * Opens the about popup
	 */
	@FXML
	private void handleAbout() {
		System.out.println("about");
	}

	@FXML
	public void handleMouseClicked(MouseEvent event) {
		if (event.getClickCount() > 1) {
			TreeItem<File> selected = treeView.getSelectionModel()
					.getSelectedItem();
			if (selected != null) {
				File file = selected.getValue();
				Tab tab = new Tab(file.getName());
				tabPane.getTabs().add(tab);
			}
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		treeView.setRoot(new SimpleFileTreeItem(new File("/")));
		treeView.setCellFactory(cellFactory);
	}

	Callback<TreeView<File>, TreeCell<File>> cellFactory = new Callback<TreeView<File>, TreeCell<File>>()
			{

				@Override
				public TreeCell<File> call(TreeView<File> param) {
					TreeCell<File> fileCell = new TextFieldTreeCellImpl();
					fileCell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {

						@Override
						public void handle(Event event) {
							System.out.println("Clicked: "+event.getSource().toString());
						}
					});
					
					return fileCell;
				}
	};
	
	 private final class TextFieldTreeCellImpl extends TreeCell<File> {
		 
	        private TextField textField;
	 
	        public TextFieldTreeCellImpl() {
	        }
	 
	        @Override
	        public void startEdit() {
	            super.startEdit();
	 
	            if (textField == null) {
	                createTextField();
	            }
	            setText(null);
	            setGraphic(textField);
	            textField.selectAll();
	        }
	 
	        @Override
	        public void cancelEdit() {
	            super.cancelEdit();
	            setText(((File) getItem()).getName());
	            setGraphic(getTreeItem().getGraphic());
	        }
	 
	        @Override
	        public void updateItem(File item, boolean empty) {
	            super.updateItem(item, empty);
	 
	            if (empty) {
	                setText(null);
	                setGraphic(null);
	            } else {
	                if (isEditing()) {
	                    if (textField != null) {
	                        textField.setText(getString());
	                    }
	                    setText(null);
	                    setGraphic(textField);
	                } else {
	                    setText(getString());
	                    setGraphic(getTreeItem().getGraphic());
	                }
	            }
	        }
	 
	        private void createTextField() {
	            textField = new TextField(getString());
	            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
	 
	                @Override
	                public void handle(KeyEvent t) {
	                    if (t.getCode() == KeyCode.ENTER) {
	                        commitEdit(new File(textField.getText()));
	                    } else if (t.getCode() == KeyCode.ESCAPE) {
	                        cancelEdit();
	                    }
	                }
	            });
	        }
	 
	        private String getString() {
	            return getItem() == null ? "" : getItem().toString();
	        }
	    }
}
