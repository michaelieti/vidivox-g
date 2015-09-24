package editor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import utility.StagedAudio;
import utility.StagedMedia;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import player.VidivoxPlayer;

public class SpeechTab extends BindableTab {

	final private static int btnSpacing = 20;

	private Text msg;
	private TextArea userField;
	private Button playBtn, cancelBtn, saveBtn, overlayBtn;
	private FileChooser f;
	private Stage stage;
	private ProgressBar progBar = new ProgressBar();
	private int pid = -1;

	public SpeechTab(final MediaView mv, String title, String message) {
		super(mv, title);
		msg = new Text(message);
		userField = new TextArea();
		f = new FileChooser();
		f.setTitle("Save");
		// Initializing Button Event handlers
		playBtn = new Button("Play");
		playBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				playSpeech();
			}

		});
		saveBtn = new Button("Save Speech");
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
		cancelBtn = new Button("Cancel Speech");
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				cancelSpeech();
			}

		});
		overlayBtn = new Button("Overlay");
		overlayBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg) {
				// TODO: THIS SHIT IS BROKEN AHHHH
				// make temp wav file
				// call mp3 to wav
				// call overlay metho0d in mp3
				try {
					String msg = userField.getText();
					StagedAudio stgAudio = MediaConverter.textToSpeech(msg);
					Media video = mv.getMediaPlayer().getMedia();
					MediaConverter.mergeVideoAndAudio(video, stgAudio, progBar);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("gg u screwed");
				}

			}

		});

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
		speechBtns.getChildren()
				.addAll(playBtn, cancelBtn, saveBtn, overlayBtn);
		speechPane.add(speechBtns, 0, 4, 3, 1);
		progBar.setVisible(false);
		speechPane.add(progBar, 0, 5);

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
		String expansion = "`echo " + msg + " | text2wave > " + path + "`";
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
