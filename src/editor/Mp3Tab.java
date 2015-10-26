package editor;

import java.io.File;
import java.net.URISyntaxException;

import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import player.VidivoxPlayer;
import utility.control.BackgroundTask;
import utility.control.MediaHandler;
import utility.media.MediaFile;
import utility.media.MediaFormat;

public class Mp3Tab extends BindableTab {

	final private static int btnSpacing = 20;

	private Text msg;
	private Stage binder;
	private Button browseBtn, overlayBtn;
	private final FileChooser fc = new FileChooser();
	private File userFile = null;
	private StringProperty filePath = new SimpleStringProperty();
	private ProgressBar prog;

	public Mp3Tab(final MediaView mv, String title, String message) {
		super(mv, title);
		msg = new Text(message);

		/*
		 * This creates a custom string property. The property will say
		 * "Please open..." if there is no video. Otherwise it will display the
		 * user defined message.
		 */
		StringBinding changeMsg;
		changeMsg = new When(mv.mediaPlayerProperty().isNull()).then(
				"Please open a video file to proceed.").otherwise(message);
		msg.textProperty().bind(changeMsg);

		msg.setFill(Color.LIGHTGRAY);
		prog = new ProgressBar();
		// Initializing Button Event handlers
		browseBtn = new Button("Browse");
		browseBtn.disableProperty().bind(mv.mediaPlayerProperty().isNull());
		browseBtn.setTooltip(new Tooltip("Select an mp3 file. This i"));
		browseBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// open up file chooser & get user selection as File object
				userFile = fc.showOpenDialog(binder);
				if (userFile != null) {
					// TODO: File now stored in userFile, ready to manipulate.
					filePath.setValue(userFile.getPath());
				}
			}

		});
		overlayBtn = new Button("Merge at this point in the video");
		overlayBtn
				.setTooltip(new Tooltip(
						"Click to overlay the mp3 with the current video and play the result"));
		overlayBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (userFile != null) {
					Media audio = new Media(userFile.toURI().toString());
					MediaFile audioContainer = null;
					try {
						audioContainer = new MediaFile(audio);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					try{
						BackgroundTask queue;
						queue = new BackgroundTask();
						//get current duration
						Duration currentDuration = VidivoxPlayer.getVPlayer().getMediaPlayer().getCurrentTime();
						//make blank = duration
						MediaFile blankFile = MediaFile.createMediaContainer(queue, MediaFormat.MP3);
						MediaHandler blankHandler = new MediaHandler(queue, blankFile);
						blankHandler.makeBlankAudio(currentDuration);
						//concat blank + mp3
						MediaFile concatFile = MediaFile.createMediaContainer(queue, MediaFormat.MP3);
						MediaHandler concatHandler = new MediaHandler(queue, concatFile);
						concatHandler.concatAudio(blankHandler.getMediaFile(), audioContainer);	//concats blank and mp3
						
						Media video = mv.getMediaPlayer().getMedia();
						
						//merge both video and mp3
						
						
						MediaFile mergedFile = MediaFile.createMediaContainer(queue, MediaFormat.MP4);
						MediaHandler mergedHandler = new MediaHandler(queue, mergedFile);
						mergedHandler.mergeAudioAndVideo(new MediaFile(video), concatHandler.getMediaFile());
						
						
						Thread th = new Thread(queue);
						th.setDaemon(true);
						th.start();
						
						Thread th2 = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									th.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
									System.out.println("Your join failed!");
								}
								VidivoxPlayer.getVPlayer().setMedia(mergedHandler.getMediaFile().getMedia());
							}
						});
						th2.setDaemon(true);
						th2.start();
						
						
						
					
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}

		});
		overlayBtn.disableProperty().bind(filePath.length().isEqualTo(0));
		TextField currentFile = new TextField("");
		filePath.setValue("");
		currentFile.textProperty().bind(filePath);
		currentFile.setEditable(false);
		GridPane mp3Pane = new GridPane();
		mp3Pane.setGridLinesVisible(player.Main.GRID_IS_VISIBLE);
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
	
}
