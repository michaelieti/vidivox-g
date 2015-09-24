package utility;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserMessageStage extends Stage {

	private static String DEFAULT_TITLE = "Warning!";
	private Button okBtn;

	// Initializer block for object construction
	{
		final Stage stg = this;
		okBtn = new Button("Ok");
		okBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				stg.close();
			}
		});
	}

	/**
	 * Constructs a message window with a given title and message. An 'ok'
	 * button is also created to allow the user to close the window after
	 * reading the message.
	 * 
	 * @param title
	 * @param msg
	 */
	public UserMessageStage(String title, String msg) {
		super();
		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(new Text(msg), okBtn);
		this.setScene(new Scene(layout));
		this.setTitle(title);
		this.show();
	}

	/**
	 * A convenience method which utilizes the default title.
	 * 
	 * @param msg
	 */
	public UserMessageStage(String msg) {
		this(DEFAULT_TITLE, msg);
	}

}
