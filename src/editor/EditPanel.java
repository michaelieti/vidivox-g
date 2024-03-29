package editor;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import player.VidivoxPlayer;

/**
 * The EditPanel is the users interface with the video editting functionality.
 * Features which allow video manipulation can be included to this panel by
 * extending the BindableTab abstract class.
 * 
 * @author adav194
 * 
 */
public class EditPanel extends Stage {

	final private static String DEFAULT_TITLE = "Editing Window";
	private static EditPanel singletonObject;
	private TabPane pane;
	
	/* Currently included Features */
	BindableTab speechTab, subtitleTab, mp3;

	/* singleton getter */
	public static EditPanel getEditPanel(){
		if (singletonObject == null){
			singletonObject = new EditPanel();
		}
		return singletonObject;
	}
	
	public EditPanel() {
		this(VidivoxPlayer.getVPlayer().getMediaView(), DEFAULT_TITLE);
	}
	/**
	 * A convenience constructor which constructs the Editor Panel with the
	 * default title.
	 * 
	 * @param mv
	 */
	public EditPanel(MediaView mv) {
		this(mv, DEFAULT_TITLE);
	}

	/**
	 * Constructs an EditPanel which allows manipulation of a video being played
	 * in a given MediaView.
	 * 
	 * @param mv
	 * @param title
	 */
	public EditPanel(MediaView mv, String title) {
		super();
		this.setTitle(title);
		pane = new TabPane();
		
		speechTab = new SpeechTab(mv, "Speech",
				"Enter text to be spoken over the video. Hover over controls for more details");
		subtitleTab = new SubtitleTab(mv, "Subtitles", "Enter text to be subtitled (coming soon...)");
		mp3 = new Mp3Tab(mv, "Audio", "Overlay an mp3 or wav file on the current video. The overlay will apply to the entire video from the current time point.");
		speechTab.setBind(this);
		subtitleTab.setBind(this);
		mp3.setBind(this);
		pane.getTabs().addAll(speechTab, subtitleTab, mp3);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("/skins/BlueSkin.css").toExternalForm());
		this.setScene(scene);
		/*
		 * Setting the close operation for this window to simply hide it. This
		 * will allow the window to be reopened at a future stage
		 */
		final Stage s = this;
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				s.hide();
			}
		});
		
	}
	
	public void editMp3(){
		//TODO: finish the method signature
		//switch to mp3 tab
		//output filepath to field
		//set currently editing flag in mp3 tab
	}
	
	
	
}
