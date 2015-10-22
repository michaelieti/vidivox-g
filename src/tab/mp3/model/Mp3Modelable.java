package tab.mp3.model;

import java.io.File;

import tab.TabModelable;

import javafx.beans.property.StringProperty;


public interface Mp3Modelable extends TabModelable {

	public File getFile();

	public StringProperty getFilePathProperty();

}
