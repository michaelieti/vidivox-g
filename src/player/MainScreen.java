package player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.stage.Stage;

public class MainScreen extends Application {
	
	String URL = null;
	MediaView mv;
	MediaPlayer mp;
	Media currentMedia;
	Button playBtn, pauseBtn, stopBtn, skipBackBtn, skipFwdBtn;
	
	public static void initiate(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//create panel for player
		//create slider section with player slider (uses seek)
			//create top hbar (play, pause, stop)
			//create middle hbar(skip back, skip forward)
			//create bottom panel(text, speech, subtitle button)
		
		primaryStage.setTitle("Vidivox 0.1");
		BorderPane root = new BorderPane();
		
			//MEDIA VIEW NODE ADDED: CENTER
		mv = new MediaView();
		root.setCenter(mv);
		
			//CONTROL PANEL ADDED: BOTTOM
		VBox controlPanel = new VBox();
			//ADD IN TOP PANEL
			HBox cp_top = new HBox();
				playBtn = new Button(">");
				pauseBtn = new Button("||");
				stopBtn = new Button("[]");
			cp_top.getChildren().addAll(playBtn, pauseBtn, stopBtn);
			//ADD IN MIDDLE PANEL
			HBox cp_mid = new HBox();
				skipFwdBtn = new Button(">>");
				skipBackBtn = new Button("<<");
			cp_mid.getChildren().addAll(skipBackBtn, skipFwdBtn);
			//ADD IN BOTTOM PANEL
			HBox cp_bot = new HBox();
		controlPanel.getChildren().addAll(cp_top, cp_mid, cp_bot);
		root.setBottom(controlPanel);
			
		primaryStage.setScene(new Scene(root, 900, 700));
		primaryStage.show();
	}
	
}
