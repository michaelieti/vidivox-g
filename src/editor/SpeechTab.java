package editor;

import utility.StagedMedia;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SpeechTab extends BindableTab {
	
	final private static int btnSpacing = 20;
	
	private Text msg;
	private TextArea userField;
	private Button playBtn, previewBtn, overlayBtn;
	
	public SpeechTab(String title, String message) {
		super(title);
		msg = new Text(message);
		userField = new TextArea();
		//Initializing Button Event handlers
		playBtn = new Button("Play");
		playBtn.setOnAction(null);
		previewBtn = new Button("Preview");
		previewBtn.setOnAction(null);
		overlayBtn = new Button("Overlay");
		overlayBtn.setOnAction(null);
		
		GridPane speechPane = new GridPane();
		speechPane.setGridLinesVisible(player.VidivoxLauncher.GRID_IS_VISIBLE);
		speechPane.setVgap(10);
		speechPane.setHgap(10);
		speechPane.setPadding(new Insets(10, 10, 10, 10));
		speechPane.add(msg, 0, 0, 3, 1);
		speechPane.add(userField, 0, 1, 3, 3);
		HBox speechBtns = new HBox();
		speechBtns.setAlignment(Pos.CENTER);
		speechBtns.setSpacing(btnSpacing);
		speechBtns.getChildren().addAll(playBtn, previewBtn, overlayBtn);
		speechPane.add(speechBtns, 0, 4, 3, 1);
		
		this.setContent(speechPane);
		
	}
	
	public void setBind(Stage toBindTo) {
		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));
		return;
	}

	@Override
	public MediaView getMediaView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stageMedia() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void previewMedia(StagedMedia media) {
		// TODO Auto-generated method stub
		
	}

}

