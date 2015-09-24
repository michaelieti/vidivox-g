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


public class EditPanel extends Stage {
	
	final private static String DEFAULT_TITLE = "Editing Window";
	
	BindableTab speech, subtitle, mp3;
	
	public EditPanel(MediaView mv) {
		this(mv, DEFAULT_TITLE);
	}
	
	public EditPanel(MediaView mv, String title) {
		super();
		this.setTitle(title);
		HBox root = new HBox();	//the overall panel
		VBox previewPanel = new VBox();
		
			MediaView previewView = PreviewMedia.getPreviewMedia().getView();
			StackPane sp = new StackPane(previewView);
			PreviewControls previewControls = new PreviewControls();
				PreviewMedia.getPreviewMedia().setControls(previewControls);
				
		TabPane pane = new TabPane();
		speech = new SpeechTab(mv, "Speech","Some shit that will introduce this tab and what you can do");
		subtitle = new SubtitleTab(mv, "Subtitles","Some shit different place");
		mp3 = new Mp3Tab(mv, "Mp3","sync yo sik az beats");
		speech.setBind(this);
		subtitle.setBind(this);
		mp3.setBind(this);
		pane.getTabs().addAll(speech, subtitle, mp3);
		previewPanel.getChildren().addAll(sp, previewControls);
		root.getChildren().addAll(pane, previewPanel);
		Scene scene = new Scene(root);
		
		this.setScene(scene);
		/*
		 * Setting the close operation for this window to simply hide it. This will allow the window to be reopened at
		 * a future stage
		 */
		final Stage s = this;
		this.setOnCloseRequest(new EventHandler<WindowEvent>(){
		    @Override
		    public void handle(WindowEvent event) {
		        s.hide();
		    }
		});
	}

}


