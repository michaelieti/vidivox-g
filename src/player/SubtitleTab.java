package player;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SubtitleTab extends BindableTab {
	final private static boolean GRID_VISIBLE = true;
	final private static int btnSpacing = 20;
	
	private Text msg;
	private TextArea userField;
	private Button playBtn, previewBtn, overlayBtn;
	
	public SubtitleTab(String title, String message) {
		super(title);
		msg = new Text(message);
		userField = new TextArea();
		//Initializing Button Event handlers
		playBtn = new Button("Play");
		playBtn.setOnAction(null);
		previewBtn = new Button("Preview");
		previewBtn.setOnAction(null);
		overlayBtn = new Button("Overlay");
		overlayBtn.setOnAction(null);
		
		GridPane subtitlePane = new GridPane();
		subtitlePane.setGridLinesVisible(GRID_VISIBLE);
		subtitlePane.setVgap(10);
		subtitlePane.setHgap(10);
		subtitlePane.setPadding(new Insets(10, 10, 10, 10));
		subtitlePane.add(msg, 0, 0, 3, 1);
		subtitlePane.add(userField, 0, 1, 3, 3);
		HBox subtitleBtns = new HBox();
		subtitleBtns.setAlignment(Pos.CENTER);
		subtitleBtns.setSpacing(btnSpacing);
		subtitleBtns.getChildren().addAll(playBtn, previewBtn, overlayBtn);
		subtitlePane.add(subtitleBtns, 0, 4, 3, 1);
		
		this.setContent(subtitlePane);
		
	}
	
	public void setBind(Stage toBindTo) {
		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));
		return;
	}

}

