package player;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import editor.EditPanel;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class will act as the controller class for the application
 * @author adav194
 *
 */
public class VidivoxLauncher extends Application {
	
	/*Developer-configurable fields for application*/
	public static final boolean GRID_IS_VISIBLE = true;
	public static final String DEFAULT_TITLE = "Vidivox V1";
	
	/*User-configurable fields for application */
	private boolean isAutoPlayEnabled = true;
	
	/*Status flags for the Application*/
	private boolean mediaEnded = false;
	
	protected final FileChooser fileChooser = new FileChooser();
	private Stage ms, editorPanel;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ms = new MainStage(this);
		editorPanel = new EditPanel();
		primaryStage = ms;
		primaryStage.show();
		editorPanel.show();
	}

	/*
	 * Placeholder Functions
	 */
	public void openFile() {
		System.out.println("File Opened Wow!");
		URI mediaPath = null;
		File file = fileChooser.showOpenDialog(ms);
		if (file != null){
			mediaPath = file.toURI();	//converts to URI object
			//next step retrieves the MediaView object from the main stage
			MediaView mediaView = getCurrentMediaView();
			Media media = null;
			try{
				media = new Media(mediaPath.toString());
			} catch (MediaException me){	//this is pretty hax tbh TODO: come up with a better way to do this
				String message = me.getMessage().trim();
				if (message.equals("MEDIA_UNSUPPORTED : Unrecognized file signature!")){
				//	mediaPath = MediaFormatter.transformMedia(mediaPath, MediaFormatter.MP4);	//TODO: reformats the media at mediaPath and provides a new URI.
					media = new Media(mediaPath.toString());
				} else {
					throw me;	//lol
				}
			}
			System.out.println(mediaPath.toString());
			if (mediaView.getMediaPlayer() != null){
				mediaView.getMediaPlayer().stop();
				mediaView.getMediaPlayer().dispose();
				System.out.println("Called mp disposal method");
			}
			//need to check media to make sure it is flv or mp4 or whatever
			//and then assigns a new MediaPlayer with a new Media to that MediaView 
			//object, but with the new file.
			mediaView.setMediaPlayer(new MediaPlayer(media));
			//sets video to play automatically
			mediaView.getMediaPlayer().setAutoPlay(isAutoPlayEnabled);
		}
	}
	
	public void saveFile() {
		System.out.println("Save this shit yo");
	}

	/**
	 * An event handling method that dictates what occurs when the Play button is pushed.
	 * If the video is paused, stopped, or at the end of the video track, play resumes from current point or the start of the video track.
	 * If the video is currently playing, the video is paused, setting into motion the setOnPaused event handler.
	 * Other states - nothing happens (e.g. an error state is generated)
	 * @param src - the button that generated the event
	 */
	public void playVideo(Button src) {
		System.out.println("Pressed Play Wow!");
		MediaPlayer mp = getCurrentPlayer();				// obtain the current media view
		Status status = mp.getStatus();	// obtain current media player status.
				//Several checks are now carried out.
		//CHECK ONE: possible error? nothing is done in these states for now
		if (status == Status.UNKNOWN || status == Status.HALTED ){
			return;
		}
		//CHECK TWO: stopped/paused/ready states - begin playing video
		if (status == Status.STOPPED || status == Status.READY || status == Status.PAUSED ){
			//check if video track is at end: if so, reset the track.
			if (mediaEnded){
				mp.seek(mp.getStartTime());
				mediaEnded = false;
			}//continue playing from that point
			mp.play();
		} else {
			mp.pause();
		}
	}

	public void pauseVideo(Button src) {
		System.out.println("Pressed Pause Wow!");
		getCurrentPlayer().pause();
	}

	public void stopVideo() {
		System.out.println("Pressed Stop Wow!");
		getCurrentPlayer().stop();
	}

	public void ffwdVideo() {
		System.out.println("Pressed Fast Foward Wow!");
		MediaPlayer mp = getCurrentPlayer();
		mp.seek(mp.getCurrentTime().add(Duration.seconds(10)));
	}

	public void rwdVideo() {
		System.out.println("Pressed Rewind Wow!");
		MediaPlayer mp = getCurrentPlayer();
		mp.seek(mp.getCurrentTime().subtract(Duration.seconds(10)));
	}
	
	public void speech() {
		System.out.println("Pressed Speech");
	}

	public void sub() {
		System.out.println("Pressed Sub");
	}
	
	public void mp3() {
		System.out.println("Pressed mp3");
	}
	
	protected MediaPlayer getCurrentPlayer(){
		return ((MainStage)ms).getMediaPane().getMediaView().getMediaPlayer();
	}
	protected MediaView getCurrentMediaView(){
		return ((MainStage)ms).getMediaPane().getMediaView();
	}
}
