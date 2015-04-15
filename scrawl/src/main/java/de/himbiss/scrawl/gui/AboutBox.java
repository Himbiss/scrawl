package de.himbiss.scrawl.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutBox extends Stage {
	public AboutBox() throws Exception {
        initModality(Modality.APPLICATION_MODAL);
        Button btn = new Button("Close");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                close();
            }
        });

        Parent root = FXMLLoader.load(getClass().getResource("AboutBox.fxml"));
        
        BorderPane borderPane = new BorderPane(root);
        HBox hbox = new HBox(btn);
        HBox.setMargin(btn, new Insets(0, 100, 0, 100));
        borderPane.setBottom(hbox);
        setScene(new Scene(borderPane));
        setWidth(300);
        setHeight(200);
    }
}
