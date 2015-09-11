package player;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.stage.Stage;

public class MainScreen extends Application {
	
	final boolean GRID_IS_VISIBLE = true;
	
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
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);	grid.setHgap(10);
		grid.setPadding(new Insets(25,25,15,25));
		grid.setGridLinesVisible(GRID_IS_VISIBLE);
		
			//MEDIA VIEW NODE ADDED: CENTER
		StackPane mediaPane = new StackPane();
		mediaPane.setMinHeight(800);
		mediaPane.setMinWidth(1000);
		mv = new MediaView();
		mediaPane.getChildren().add(mv);
		
		
		grid.add(mv,0,1);
		
			//CONTROL PANEL ADDED: BOTTOM
		VBox controlPanel = new VBox();
			//ADD IN TOP PANEL
			HBox cp_top = new HBox();
			cp_top.setAlignment(Pos.CENTER);
			cp_top.setSpacing(10);
				playBtn = new Button(">");
				pauseBtn = new Button("||");
				stopBtn = new Button("[]");
			cp_top.getChildren().addAll(playBtn, pauseBtn, stopBtn);
			//ADD IN MIDDLE PANEL
			HBox cp_mid = new HBox();
			cp_mid.setAlignment(Pos.CENTER);
				skipFwdBtn = new Button(">>");
				skipBackBtn = new Button("<<");
			cp_mid.getChildren().addAll(skipBackBtn, skipFwdBtn);
			//ADD IN BOTTOM PANEL
			HBox cp_bot = new HBox();
		controlPanel.setSpacing(10);
		controlPanel.getChildren().addAll(cp_top, cp_mid, cp_bot);
		grid.add(controlPanel, 0,3);
			
		primaryStage.setScene(new Scene(grid));
		primaryStage.show();
	}
	
}
