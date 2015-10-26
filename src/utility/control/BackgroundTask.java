package utility.control;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

public class BackgroundTask extends Task<Void> {
	
	List<FFMPEG> listOfTasks = new ArrayList<FFMPEG>();

	public void addTask(FFMPEG task) {
		if (task.getProcess().isValid()) {
			listOfTasks.add(task);
		}
	}
	@Override
	protected Void call() throws Exception {
		for (FFMPEG f: listOfTasks) {
			System.out.print(listOfTasks.indexOf(f) + " - " + f.toString() );
			f.getProcess().call();
			f.waitFor();
			Thread.sleep(1000);
		}
		System.out.print(listOfTasks);
		listOfTasks.clear();
		System.out.println("Done");
		return null;
	}

}
