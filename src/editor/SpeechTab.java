package editor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import utility.StagedAudio;
import utility.StagedMedia;
import utility.control.MediaHandler;
import utility.control.SchemeFile;
import utility.media.MediaFile;
import utility.media.MediaFormat;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import overlay.Commentary;
import overlay.control.OverlayController;
import overlay.control.OverlayType;
import player.SliderVX;
import player.VidivoxPlayer;

public class SpeechTab extends BindableTab {

	final private static int btnSpacing = 20;

	/* GUI elements */
	private Text msg;
	private TextArea userField;
	private Button speechBtn, saveBtn, overlayBtn, cancelOverlayBtn;
	private FileChooser f;
	private ProgressBar progBar = new ProgressBar();
	
	/* advanced GUI elements */
	private final ComboBox<SchemeFile.VoiceActor> voiceActorsComboBox;
	private SliderVX rateSlider, pitchInitialSlider, pitchFinalSlider;
	private Button resetAdvanced;
	
	/* festival fields*/
	private int pid = -1;
	
	private MediaHandler speechMedia;
	private SchemeFile scmFile;
	
	/* commentary editing fields*/
	private SimpleBooleanProperty editFlag = new SimpleBooleanProperty(false);
	private Commentary commentUnderEdit = null;

	private static SpeechTab singletonObject;
	
	/**
	 * sets the scheme file's settings according to what is currently on the GUI.
	 */
	private void bindAdvancedOptions(){
		//bind all sliders and stuff to scmFile
		//must be called when new scmFile is created.
		
		voiceActorsComboBox.setOnAction(new EventHandler(){
			@Override
			public void handle(Event event) {
				scmFile.setActor((SchemeFile.VoiceActor)voiceActorsComboBox.getSelectionModel().getSelectedItem());
			}
		});
		
		rateSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double currentRate = rateSlider.getValue() / SchemeFile.MAX_RATE;
				scmFile.setRateOfSpeech(currentRate);
			}
		});
		
		pitchInitialSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double currentPitch = pitchInitialSlider.getValue() / SchemeFile.MAX_PITCH;
				scmFile.setInitialPitch((int)currentPitch);
			}
		});
		
		pitchFinalSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double currentPitch = pitchFinalSlider.getValue() / SchemeFile.MAX_PITCH;
				scmFile.setFinalPitch((int)currentPitch);
			}
		});
	}
	private void customSetSliders(SchemeFile scm){
		voiceActorsComboBox.getSelectionModel().select(scm.getActor());
		rateSlider.setValue(scm.getRateOfSpeech());
		pitchInitialSlider.setValue(scm.getInitialPitch());
		pitchFinalSlider.setValue(scm.getFinalPitch());
	}
	
	
	public SpeechTab(final MediaView mv, String title, String message) {
		super(mv, title);
		
		scmFile = new SchemeFile();
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
		
		
		VBox advancedOptionBox = new VBox();
		advancedOptionBox.setMaxWidth(200);
		advancedOptionBox.setAlignment(Pos.CENTER);
		
		HBox voiceActorPanel = new HBox();
		Text voiceActorLabel = new Text("Voice Actor: ");
		ObservableList<SchemeFile.VoiceActor> options = 
			    FXCollections.observableArrayList(SchemeFile.VoiceActor.Jono, SchemeFile.VoiceActor.Robot, SchemeFile.VoiceActor.Gordon );
		voiceActorsComboBox = new ComboBox<SchemeFile.VoiceActor>(options);
		voiceActorPanel.getChildren().addAll(voiceActorLabel, voiceActorsComboBox);
		
		Text rateLabel = new Text("Rate");
		rateSlider = new SliderVX(SchemeFile.MIN_RATE, SchemeFile.MAX_RATE, SchemeFile.DEFAULT_RATE);
		rateSlider.setMaxWidth(150);
		
		Text pitch_initial = new Text("Initial pitch");
		pitchInitialSlider = new SliderVX(SchemeFile.MIN_PITCH, SchemeFile.MAX_PITCH, SchemeFile.DEFAULT_PITCH);
		pitchInitialSlider.setMaxWidth(150);
		Text pitch_final = new Text("Final pitch");
		pitchFinalSlider = new SliderVX(SchemeFile.MIN_PITCH, SchemeFile.MAX_PITCH, SchemeFile.DEFAULT_PITCH);
		pitchFinalSlider.setMaxWidth(150);
		
		resetAdvanced = new Button("Reset sliders");
		resetAdvanced.setOnAction(new ResetSlidersHandler());
		
		advancedOptionBox.getChildren().addAll(voiceActorPanel, rateLabel, rateSlider, pitch_initial, pitchInitialSlider, 
				pitch_final, pitchFinalSlider, resetAdvanced);
		
		
		/* placement starts here */
		BorderPane speechPane = new BorderPane();
		
		speechPane.setPadding(new Insets(10, 10, 10, 10));
		speechPane.setTop(msg);
		speechPane.setLeft(userField);
		
		VBox speechCtrl = new VBox();
			HBox speechBtns = new HBox();
			speechBtns.setAlignment(Pos.CENTER);
			speechBtns.setSpacing(btnSpacing);
			speechBtns.getChildren().addAll(speechBtn, saveBtn, overlayBtn, cancelOverlayBtn);
			progBar.setVisible(false);
		speechCtrl.getChildren().addAll(speechBtns, progBar);
		speechPane.setBottom(speechCtrl);
		
		speechPane.getStyleClass().add("gridPane");
		
		speechPane.setRight(advancedOptionBox);
		
		BorderPane.setMargin(advancedOptionBox, new Insets(12,12,12,12));
		BorderPane.setMargin(speechCtrl, new Insets (12,12,12,12));
		this.setContent(speechPane);
		
		bindAdvancedOptions();

	}	
	
	public static SpeechTab getSpeechTab(){
		return singletonObject;
	}
	
	public void setScheme(SchemeFile scm){
		this.scmFile = scm;
		bindAdvancedOptions();
	}


	public void editCommentary(Commentary commentary){
		setEditFlag(true);	//sets edit flag
		setUserField(commentary.getText());		//sets text field
		setScheme(commentary.getScheme());
		customSetSliders(commentary.getScheme());
		bindAdvancedOptions();
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
				commentUnderEdit.setScheme(scmFile);
				OverlayController.getController().reloadTable();
				setEditFlag(false);
				scmFile = new SchemeFile();
				bindAdvancedOptions();
			} else {
				// not currently under editing: create new commentary, use the current time, and use new text
				String text = userField.getText();
				Duration time = VidivoxPlayer.getVPlayer().getMediaPlayer().getCurrentTime();
				Commentary comment = new Commentary(time, text, OverlayType.TTS);
				comment.setScheme(scmFile);
				OverlayController.getController().addCommentary(comment);
				scmFile = new SchemeFile();
				bindAdvancedOptions();
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

	
	public class ResetSlidersHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			rateSlider.setValue(SchemeFile.DEFAULT_RATE);
			pitchInitialSlider.setValue(SchemeFile.DEFAULT_PITCH);
			pitchFinalSlider.setValue(SchemeFile.DEFAULT_PITCH);
		}
		
	}


}
