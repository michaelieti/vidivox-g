package player;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VidivoxVideoControls extends VBox {

	private VidivoxLauncher launcher;
	private Button playBtn, pauseBtn, stopBtn, skipBackBtn, skipFwdBtn;
	
	public VidivoxVideoControls(VidivoxLauncher vl) {
		super();
		launcher = vl;
		
		//Buttons defined here (e.g. play button, pause button, stop button...)
		playBtn = new Button(">");
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				launcher.playVideo();
			}
		});
		pauseBtn = new Button("||");
		pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				launcher.pauseVideo();
			}
		});
		stopBtn = new Button("[]");
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				launcher.stopVideo();
			}
		});
		skipFwdBtn = new Button(">>");
		skipFwdBtn.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent event) {
				launcher.ffwdVideo();
			}
		});
		skipBackBtn = new Button("<<");
		skipBackBtn.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent event) {
				launcher.rwdVideo();
			}
		});
		
		//ADD IN TOP PANEL
		HBox cp_top = new HBox();
		cp_top.setAlignment(Pos.CENTER);
		cp_top.setSpacing(10);
		cp_top.getChildren().addAll(playBtn, pauseBtn, stopBtn);
		
		//ADD IN MIDDLE PANEL
		HBox cp_mid = new HBox();
		cp_mid.setAlignment(Pos.CENTER);
		cp_mid.getChildren().addAll(skipBackBtn, skipFwdBtn);
		
		//ADD IN BOTTOM PANEL
		HBox cp_bot = new HBox();
		this.setSpacing(10);
		this.getChildren().addAll(cp_top, cp_mid, cp_bot);
		
	}
}

