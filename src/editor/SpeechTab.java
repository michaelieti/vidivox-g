package editor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import utility.StagedAudio;
import utility.StagedMedia;
import utility.control.MediaHandler;
import utility.media.MediaFile;
import utility.media.MediaFormat;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import overlay.Commentary;
import overlay.control.OverlayController;
import overlay.control.OverlayType;
import player.VidivoxPlayer;

public class SpeechTab extends BindableTab {

	final private static int btnSpacing = 20;

	/* GUI elements */
	private Text msg;
	private TextArea userField;
	private Button speechBtn, saveBtn, overlayBtn, cancelOverlayBtn;
	private FileChooser f;
	private ProgressBar progBar = new ProgressBar();
	
	/* festival fields*/
	private int pid = -1;
	
	private MediaHandler speechMedia;
	
	/* commentary editing fields*/
	private SimpleBooleanProperty editFlag = new SimpleBooleanProperty(false);
	private Commentary commentUnderEdit = null;

	private static SpeechTab singletonObject;
	
	public SpeechTab(final MediaView mv, String title, String message) {
		super(mv, title);
		
		singletonObject = this;
		
		MediaFile mediaFile = MediaFile.createMediaContainer(MediaFormat.WAV);
		try {
			speechMedia = new MediaHandler(mediaFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		msg = new Text(message);
		msg.setFill(Color.LIGHTGRAY);
		userField = new TextArea();

		// Making the userField not editable when there is no video attached to
		// the media player
		userField.editableProperty().bind(mv.mediaPlayerProperty().isNotNull());

		/* button initialization and event handler setting */
		saveBtn = new Button("Save Speech");
		saveBtn.setTooltip(new Tooltip("Save the text to a wav file"));
		saveBtn.setOnAction(new SaveHandler());
		saveBtn.disableProperty().bind(userField.lengthProperty().isEqualTo(0));

		/* Speech button initialization, and setting up toggling action */
		speechBtn = new Button();
		setSpeechState(false);
		speechBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (pid == -1) {
					playSpeech();
				} else {
					cancelSpeech();
				}
			}
		});
		speechBtn.disableProperty().bind(
				userField.lengthProperty().isEqualTo(0));
		
		overlayBtn = new Button("Add to overlay");
		overlayBtn.setTooltip(new Tooltip(
						"Add this commentary with the current time to the overlays."));
		overlayBtn.setOnAction(new AddToOverlayHandler());
		//disables when no video or no text in field
		overlayBtn.disableProperty().bind(
			Bindings.or(userField.lengthProperty().isEqualTo(0), mv
				.mediaPlayerProperty().isNull()));
		

		cancelOverlayBtn = new Button("Cancel editing");
		cancelOverlayBtn.setTooltip(new Tooltip("Cancel the editing of this tooltip, and go back to inserting new commentary."));
		cancelOverlayBtn.setVisible(false);
		cancelOverlayBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				editFlag.set(false);
				commentUnderEdit = null;
			}
		});
		
		/* ensures that the cancel overlay button only appears when editflag = true*/
		editFlag.addListener(new CancelOverlayButtonListener());
		
		/* placement starts here */
		GridPane speechPane = new GridPane();
		speechPane.setGridLinesVisible(player.Main.GRID_IS_VISIBLE);
		speechPane.setVgap(10);
		speechPane.setHgap(10);
		speechPane.setPadding(new Insets(10, 10, 10, 10));
		speechPane.add(msg, 0, 0, 3, 1);
		speechPane.add(userField, 0, 1, 3, 3);
		HBox speechBtns = new HBox();
		speechBtns.setAlignment(Pos.CENTER);
		speechBtns.setSpacing(btnSpacing);
		speechBtns.getChildren().addAll(speechBtn, saveBtn, overlayBtn, cancelOverlayBtn);
		speechPane.add(speechBtns, 0, 4, 3, 1);
		progBar.setVisible(false);
		speechPane.add(progBar, 0, 5);
		speechPane.getStyleClass().add("gridPane");
		this.setContent(speechPane);

	}	
	
	@Override
	public void commitToMediaPlayer(MediaFile mediaToCommit) {
		return;
		
	}
	
	public static SpeechTab getSpeechTab(){
		return singletonObject;
	}


	public void editCommentary(Commentary commentary){
		setEditFlag(true);	//sets edit flag
		setUserField(commentary.getText());		//sets text field
		setCommentUnderEdit(commentary);		//sets comment under edit
	}
	
	public void setUserField(String text){
		userField.setText(text);
	}
	
	private void setEditFlag(boolean x){
		editFlag.set(x);
	}
	
	private void setCommentUnderEdit(Commentary comment){
		commentUnderEdit = comment;
	}
	
	@Override
	public void setBind(Stage toBindTo) {
		/**Defines the binding for the main stage message.
		 */
		StringBinding changeMsg;
		changeMsg = new 
				When(VidivoxPlayer.getVPlayer().getMediaView().mediaPlayerProperty().isNull()).
				then("Please open a video file to proceed.")
				.otherwise(msg.getText());
		msg.textProperty().bind(changeMsg);
		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));
	}

	@Override
	public MediaView getMediaView() {
		return null;
	}

	private void playSpeech() {
		cancelSpeech();
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				String expansion = "`echo \"" + userField.getText()
						+ "\" | festival --tts`";
				String[] cmd = { "bash", "-c", expansion };
				ProcessBuilder build = new ProcessBuilder(cmd);
				Process p;
				try {
					p = build.start();
					if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
						Field f = p.getClass().getDeclaredField("pid");
						f.setAccessible(true);
						pid = f.getInt(p);
						p.waitFor();
						pid = -1;

					}
				} catch (IOException | NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

				return null;
			}

		};
		task.setOnRunning(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				setSpeechState(true);
			}

		});
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				setSpeechState(false);
			}

		});

		Thread th = new Thread(task);
		th.setDaemon(true);

		th.start();

	}

	private void cancelSpeech() {
		if (pid != -1) {
			String expansion = "kill -9 `pstree -pn "
					+ pid
					+ " | grep -o \"([[:digit:]]*)\" | grep -o \"[[:digit:]]*\"`";
			String[] cmd = { "bash", "-c", expansion };
			ProcessBuilder build = new ProcessBuilder(cmd);
			try {
				build.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pid = -1;
		}
		return;
	}

	private void setSpeechState(boolean isSpeaking) {
		if (isSpeaking) {
			speechBtn.setText("Cancel");
			speechBtn.setTooltip(new Tooltip("Halt the speech prematurely"));

		} else {
			speechBtn.setText("Preview");
			speechBtn.setTooltip(new Tooltip(
					"Preview the speech in the text box"));

		}
	}


//	public void stageMedia() {
//		String path = stagedMedia.getFile().getAbsolutePath();
//		textToSpeech(userField.getText(), path);
//	}

	/**
	 * Convenience method which saves a particular string as an audio file at a
	 * given path. Not for public use
	 * 
	 * @param msg
	 * @param path
	 */
	private void textToSpeech(String msg, String path) {
		String expansion = "`echo \"" + msg + "\" | text2wave > " + path + "`";
		String[] cmd = { "bash", "-c", expansion };
		ProcessBuilder build = new ProcessBuilder(cmd);
		try {
			build.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void initStagedMedia() {
//		stagedMedia = new StagedAudio(StagedAudio.MediaTypes.WAV);
//	}

	private String buildPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("user.home"));
		sb.append("/temp/wav");
		return sb.toString();
	}
	
	
	/* EVENT HANDLERS, FILTERS, TASKS */
	
	/**
	 * To be called when the save button is pressed.
	 * @author michael
	 *
	 */
	public class SaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			FileChooser f = new FileChooser();
			f.setTitle("Save");
			File file = new FileChooser().showSaveDialog(null);
			if (file != null) {
				textToSpeech(userField.getText(), file.getAbsolutePath());
			}
		}

	}

	/**
	 * Create a new commentary and add it to the overlay list.
	 * The table is updated automatically.
	 */
	public class AddToOverlayHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg) {
			
			if (editFlag.get()){	//if currently under editing, just edit the text, leave the time
				commentUnderEdit.setText(userField.getText());
				OverlayController.getController().reloadTable();
				setEditFlag(false);
			} else {
				// not currently under editing: create new commentary, use the current time, and use new text
				String text = userField.getText();
				Duration time = VidivoxPlayer.getVPlayer().getMediaPlayer().getCurrentTime();
				Commentary comment = new Commentary(time, text, OverlayType.TTS);
				OverlayController.getController().addCommentary(comment);
			}
		}
	}
	
	public class CancelOverlayButtonListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (newValue){
				cancelOverlayBtn.setVisible(true);
				overlayBtn.setText("Finish editing commentary");
				overlayBtn.setTooltip(new Tooltip("Save this edit"));
			}
			else{
				cancelOverlayBtn.setVisible(false);
				overlayBtn.setText("Add to overlay list");
				overlayBtn.setTooltip(new Tooltip("Add this commentary at the current time to the list of commentary to be overlaid"));
			}
		}
	}


	


}
