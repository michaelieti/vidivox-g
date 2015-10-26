package utility.control;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

public class BackgroundTask extends Task<Void> {
	
	List<FFMPEG> listOfTasks = new ArrayList<FFMPEG>();
	
	public BackgroundTask() {
		
	}

	public void addTask(FFMPEG task) {
		listOfTasks.add(task);
	}
	@Override
	protected Void call() throws Exception {
		for (FFMPEG f: listOfTasks) {
			System.out.print(listOfTasks.indexOf(f));
			f.getProcess().call();
			f.waitFor();
			Thread.sleep(1000);
		}
		listOfTasks.clear();
		System.out.println("Done");
		return null;
	}

}
