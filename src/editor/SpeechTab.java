package editor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import utility.StagedAudio;
import utility.StagedMedia;
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

	private Text msg;
	private TextArea userField;
	private Button speechBtn, saveBtn, overlayBtn, cancelOverlayBtn;
	private FileChooser f;
	private Stage stage;
	private ProgressBar progBar = new ProgressBar();
	private int pid = -1;
	private SimpleBooleanProperty editFlag = new SimpleBooleanProperty(false);
	private Commentary commentUnderEdit = null;
	private static SpeechTab speechTab;
	
	public static SpeechTab getSpeechTab(){
		return speechTab;
	}
	

	public SpeechTab(final MediaView mv, String title, String message) {
		super(mv, title);

		speechTab = this;
		
		msg = new Text(message);
		msg.setFill(Color.LIGHTGRAY);
		userField = new TextArea();

		// Making the userField not editable when there is no video attached to
		// the media player
		userField.editableProperty().bind(mv.mediaPlayerProperty().isNotNull());

		f = new FileChooser();
		f.setTitle("Save");
		// Initializing Button Event handlers
		saveBtn = new Button("Save Speech");
		saveBtn.setTooltip(new Tooltip("Save the text to a wav file"));
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser f = new FileChooser();
				f.setTitle("Save");
				File file = new FileChooser().showSaveDialog(null);
				if (file != null) {
					textToSpeech(userField.getText(), file.getAbsolutePath());
				}
			}

		});
		saveBtn.disableProperty().bind(userField.lengthProperty().isEqualTo(0));

		/*
		 * Initializes the Speech button, which toggles between Preview and
		 * Cancel.
		 */
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
		
		overlayBtn = new Button("Add to overlay");
		overlayBtn.setTooltip(new Tooltip(
						"Add this commentary with the current time to the overlays."));

		// Disables the overlay button when either there is no input text, or
		// there is no attached video.
		overlayBtn.disableProperty().bind(
				Bindings.or(userField.lengthProperty().isEqualTo(0), mv
						.mediaPlayerProperty().isNull()));
		overlayBtn.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * Create a new commentary and add it to the overlay list.
			 * The table is updated automatically.
			 */
			@Override
			public void handle(ActionEvent arg) {
				
				if (editFlag.get()){	//if currently under editing, just edit the text, leave the time
					commentUnderEdit.setText(userField.getText());
					//reload table - is this needed?
					setEditFlag(false);
				} else {
					// not currently under editing: create new commentary, use the current time, and use new text
					String text = userField.getText();
					Duration time = VidivoxPlayer.getVPlayer(null).getMediaPlayer().getCurrentTime();
					Commentary comment = new Commentary(time, text, OverlayType.TTS);

					OverlayController.getOLController().addCommentary(comment);
				}
				/*
				 * THE OLD IMPLEMENTATION OF SPEECH TAB ADDS IN A COMMENTARY AT
				 * THE START OF THE VIDEO. THIS IS BEING REPLACED
				 * 
				 * try { String msg = userField.getText(); StagedAudio stgAudio
				 * = MediaConverter.textToSpeech(msg); Media video =
				 * mv.getMediaPlayer().getMedia();
				 * MediaConverter.mergeVideoAndAudio(video, stgAudio, progBar);
				 * } catch (Exception e) { e.printStackTrace();
				 * System.out.println("gg u screwed"); }
				 */

			}

		});
		// disables the button when no text in field

		editFlag.addListener(new ChangeListener<Boolean>(){
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
			
		});
		
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

	public void editCommentary(Commentary commentary){
		commentUnderEdit = commentary;
		userField.setText(commentary.getText());
		setEditFlag(true);
	}
	
	private void setEditFlag(boolean x){
		editFlag.set(x);
	}
	
	
	public void setBind(Stage toBindTo) {
		/*
		 * This creates a custom string property. The property will say
		 * "Please open..." if there is no video. Otherwise it will display the
		 * user defined message.
		 */
		StringBinding changeMsg;
		changeMsg = new When(VidivoxPlayer.getVPlayer(null).getMediaView().mediaPlayerProperty().isNull()).then(
				"Please open a video file to proceed.")
				.otherwise(msg.getText());
		msg.textProperty().bind(changeMsg);

		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));

		return;
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

	@Override
	public void stageMedia() {
		String path = stagedMedia.getFile().getAbsolutePath();
		textToSpeech(userField.getText(), path);
	}

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

	@Override
	public void publishStage(StagedMedia media) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initStagedMedia() {
		stagedMedia = new StagedAudio(StagedAudio.MediaTypes.WAV);
	}

	private String buildPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("user.home"));
		sb.append("/temp/wav");
		return sb.toString();
	}

}
