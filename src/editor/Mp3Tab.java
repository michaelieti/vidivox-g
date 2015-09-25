package editor;

import java.io.File;

import utility.StagedAudio;
import utility.StagedMedia;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Mp3Tab extends BindableTab {
	
	final private static int btnSpacing = 20;
	
	private Text msg;
	private Stage binder;
	private Button browseBtn,overlayBtn;
	private final FileChooser fc = new FileChooser();
	private File userFile = null;
	private StringProperty filePath = new SimpleStringProperty();
	private ProgressBar prog;
	
	public Mp3Tab(final MediaView mv, String title, String message) {
		super(mv, title);
		msg = new Text(message);
		msg.setFill(Color.LIGHTGRAY);
		prog = new ProgressBar();
		//Initializing Button Event handlers
		browseBtn = new Button("Browse");
		browseBtn.setTooltip(new Tooltip("Select an mp3 file"));
		browseBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				//open up file chooser & get user selection as File object
				userFile = fc.showOpenDialog(binder);
				if (userFile != null){
					//TODO: File now stored in userFile, ready to manipulate.
					filePath.setValue(userFile.getPath());
				}
			}
			
		});
		overlayBtn = new Button("Overlay");
		overlayBtn.setTooltip(new Tooltip("Click to overlay the mp3 with the current video and play the result"));
		overlayBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (userFile != null) {
					Media audio = new Media(userFile.toURI().toString());
					StagedAudio s = new StagedAudio(audio,"");
					Media video = mv.getMediaPlayer().getMedia();
					MediaConverter.mergeVideoAndAudio(video, s, prog);
				}
			}
			
		});
		TextField currentFile = new TextField("");
		filePath.setValue("");
		currentFile.textProperty().bind(filePath);
		GridPane mp3Pane = new GridPane();
		mp3Pane.setGridLinesVisible(player.VidivoxLauncher.GRID_IS_VISIBLE);
		mp3Pane.setVgap(10);
		mp3Pane.setHgap(10);
		mp3Pane.setPadding(new Insets(10, 10, 10, 10));
		mp3Pane.add(msg, 0, 0, 2, 1);
		HBox mp3Buttons = new HBox();
		mp3Buttons.setAlignment(Pos.CENTER);
		mp3Buttons.setSpacing(btnSpacing);
		mp3Buttons.getChildren().add(overlayBtn);
		mp3Pane.add(browseBtn, 0, 1);
		mp3Pane.add(currentFile, 1, 1);
		mp3Pane.add(mp3Buttons, 0, 2, 2, 1);
		prog.setVisible(false);
		mp3Pane.add(prog, 0, 3);
		
		
		this.setContent(mp3Pane);
		
	}

	public void setBind(Stage toBindTo) {
		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));
		binder = toBindTo;
		return;
	}
	
	public File getFile() {
		return userFile;
	}

	@Override
	public MediaView getMediaView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void stageMedia() {
		// TODO Auto-generated method stub
		return;
	}


	@Override
	public void publishStage(StagedMedia media) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initStagedMedia() {
		// TODO Auto-generated method stub
		
	}
}
