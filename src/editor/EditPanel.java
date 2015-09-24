package editor;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

	/* Currently included Features */
	BindableTab speech, subtitle, mp3;

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
	/*	//[PREVIEW PANEL] Uncomment 1 of 2 for Preview Panel
 		HBox root = new HBox();	//the overall panel
		VBox previewPanel = new VBox();
		
			MediaView previewView = PreviewMedia.getPreviewMedia().getView();
			StackPane sp = new StackPane(previewView);
			PreviewControls previewControls = new PreviewControls();
				PreviewMedia.getPreviewMedia().setControls(previewControls);
	*/			
		TabPane pane = new TabPane();
		speech = new SpeechTab(mv, "Speech",
				"Some shit that will introduce this tab and what you can do");
		subtitle = new SubtitleTab(mv, "Subtitles", "Some shit different place");
		mp3 = new Mp3Tab(mv, "Mp3", "sync yo sik az beats");
		speech.setBind(this);
		subtitle.setBind(this);
		mp3.setBind(this);
		pane.getTabs().addAll(speech, subtitle, mp3);
	/*	//[PREVIEW PANEL] Uncomment 2 of 2 for Preview Panel
	 * 	//	ALSO: change Scene(pane) to Scene(root)
	  	previewPanel.getChildren().addAll(sp, previewControls);
		root.getChildren().addAll(pane, previewPanel);
	*/
		Scene scene = new Scene(pane);
		
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

}
