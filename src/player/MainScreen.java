package player;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainScreen extends Application {
	
	final boolean GRID_IS_VISIBLE = true; //Configurable parameters
	
	String URL = null;
	MediaView mv;
	MediaPlayer mp;
	Media currentMedia;
	Button playBtn, pauseBtn, stopBtn, skipBackBtn, skipFwdBtn;
	Stage primary; //Allows use of convience function setVisible
	
	public void initiate(String[] args){
		launch(args);
	}

	public void setVisible(boolean vis) {
		if (vis) {
			primary.show();
		} else {
			primary.hide();
		}	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new MainStage();
		primaryStage.show();
	}
}
