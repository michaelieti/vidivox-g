package editor;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Mp3Tab extends BindableTab {
	
	final private static boolean GRID_VISIBLE = true;
	final private static int btnSpacing = 20;
	
	private Text msg;
	private Stage binder;
	private Button browseBtn,selectBtn;
	private final FileChooser fc = new FileChooser();
	private File userFile = null;
	
	public Mp3Tab(String title, String message) {
		super(title);
		msg = new Text(message);
		//Initializing Button Event handlers
		browseBtn = new Button("Browse");
		browseBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				//open up file chooser & get user selection as File object
				userFile = fc.showOpenDialog(binder);
				if (userFile != null){
					//TODO: File now stored in userFile, ready to manipulate.
				}
			}
			
		});
		selectBtn = new Button("Select");
		selectBtn.setOnAction(null);
		
		GridPane mp3Pane = new GridPane();
		mp3Pane.setGridLinesVisible(GRID_VISIBLE);
		mp3Pane.setVgap(10);
		mp3Pane.setHgap(10);
		mp3Pane.setPadding(new Insets(10, 10, 10, 10));
		mp3Pane.add(msg, 0, 0);
		HBox mp3Butons = new HBox();
		mp3Butons.setAlignment(Pos.CENTER);
		mp3Butons.setSpacing(btnSpacing);
		mp3Butons.getChildren().addAll(browseBtn, selectBtn);
		mp3Pane.add(mp3Butons, 0, 1);
		
		this.setContent(mp3Pane);
		
	}

	public void setBind(Stage toBindTo) {
		msg.wrappingWidthProperty().bind(toBindTo.widthProperty().subtract(20));
		binder = toBindTo;
		return;
	}


}
