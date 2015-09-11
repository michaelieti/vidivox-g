package player;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	Stage primary;
	
	public void initiate(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primary = primaryStage;
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
		
			//ADD IN A CONTROL BAR
		HBox cntrlBar = new HBox();
			Button openFileBtn = new Button("Open file");
				openFileBtn.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent event) {
						VidivoxLauncher.openFile();
					}
					
				});
			cntrlBar.getChildren().add(openFileBtn);
		grid.add(cntrlBar,  0, 0);
		
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
			Button	playBtn = new Button(">");
				playBtn.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						VidivoxLauncher.playVideo();
					}
				});
			Button pauseBtn = new Button("||");
				pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						VidivoxLauncher.pauseVideo();
					}
				});
			Button stopBtn = new Button("[]");
				stopBtn.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						VidivoxLauncher.stopVideo();
					}
				});
			cp_top.getChildren().addAll(playBtn, pauseBtn, stopBtn);
			//ADD IN MIDDLE PANEL
			HBox cp_mid = new HBox();
			cp_mid.setAlignment(Pos.CENTER);
			Button skipFwdBtn = new Button(">>");
				skipFwdBtn.setOnAction(new EventHandler<ActionEvent> () {
					public void handle(ActionEvent event) {
						VidivoxLauncher.ffwdVideo();
					}
				});
			Button skipBackBtn = new Button("<<");
				skipBackBtn.setOnAction(new EventHandler<ActionEvent> () {
					public void handle(ActionEvent event) {
						VidivoxLauncher.rwdVideo();
					}
				});
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
