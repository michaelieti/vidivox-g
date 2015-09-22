package editor;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
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
		TabPane pane = new TabPane();
		speech = new SpeechTab(mv, "Speech","Some shit that will introduce this tab and what you can do");
		subtitle = new SubtitleTab(mv, "Subtitles","Some shit different place");
		mp3 = new Mp3Tab(mv, "Mp3","sync yo sik az beats");
		speech.setBind(this);
		subtitle.setBind(this);
		mp3.setBind(this);
		pane.getTabs().addAll(speech, subtitle, mp3);
		this.setScene(new Scene(pane));
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


