package player;

import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class EditPanel extends Stage {
	
	final private static String DEFAULT_TITLE = "Editting Window";
	
	BindableTab speech, subtitle, mp3;
	
	public EditPanel() {
		this(DEFAULT_TITLE);
	}
	
	public EditPanel(String title) {
		super();
		this.setTitle(title);
		TabPane pane = new TabPane();
		speech = new SpeechTab("Speech","Some shit that will introduce this tab and what you can do");
		subtitle = new SubtitleTab("Subtitles","Some shit different place");
		mp3 = new Mp3Tab("Mp3","sync yo sik az beats");
		speech.setBind(this);
		subtitle.setBind(this);
		mp3.setBind(this);
		pane.getTabs().addAll(speech, subtitle, mp3);
		this.setScene(new Scene(pane));
	}

}


