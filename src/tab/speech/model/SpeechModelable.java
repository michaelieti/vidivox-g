package tab.speech.model;

import tab.TabModelable;
import javafx.beans.property.StringProperty;

public interface SpeechModelable extends TabModelable {

	public String getUserInputText();

	public StringProperty getUserInputTextProperty();

}
