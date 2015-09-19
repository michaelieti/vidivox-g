package editor;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Mp3Tab extends BindableTab {
	
	final private static int btnSpacing = 20;
	
	private Text msg;
	private Stage binder;
	private Button browseBtn,okBtn;
	private final FileChooser fc = new FileChooser();
	private File userFile = null;
	private StringProperty filePath = new SimpleStringProperty();
	
	public Mp3Tab(String title, String message) {
		super(title);
		msg = new Text(message);
		//Initializing Button Event handlers
		browseBtn = new Button("Browse");
		browseBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				//open up file chooser & get user selection as File object
				userFile = fc.showOpenDialog(binder);
				if (userFile != null){
					//TODO: File now stored in userFile, ready to manipulate.
					filePath.setValue(userFile.getPath());
				}
			}
			
		});
		okBtn = new Button("Ok");
		okBtn.setOnAction(null);
		TextField currentFile = new TextField("");
		filePath.setValue("");
		currentFile.textProperty().bind(filePath);
		GridPane mp3Pane = new GridPane();
		mp3Pane.setGridLinesVisible(player.VidivoxLauncher.GRID_IS_VISIBLE);
		mp3Pane.setVgap(10);
		mp3Pane.setHgap(10);
		mp3Pane.setPadding(new Insets(10, 10, 10, 10));
		mp3Pane.add(msg, 0, 0, 2, 1);
		HBox mp3Buttons = new HBox();
		mp3Buttons.setAlignment(Pos.CENTER);
		mp3Buttons.setSpacing(btnSpacing);
		mp3Buttons.getChildren().add(okBtn);
		mp3Pane.add(browseBtn, 0, 1);
		mp3Pane.add(currentFile, 1, 1);
		mp3Pane.add(mp3Buttons, 0, 2, 2, 1);
		
		this.setContent(mp3Pane);
		
	}

	public void setBind(Stage toBindTo) {
		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));
		binder = toBindTo;
		return;
	}
	
	public File getFile() {
		return userFile;
	}
}
