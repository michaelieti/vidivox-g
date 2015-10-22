package tab;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import main.model.MainModelable;

public interface TabModelable {
	
	public MainModelable getMainModel();
	
	public String getTabMessage();
	
	public StringProperty getTabMessageProperty();
	
	public boolean getDisabled();
	
	public BooleanProperty getDisabledProperty();

}
