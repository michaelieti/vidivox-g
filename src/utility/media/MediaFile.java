package utility.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.scene.media.Media;

import editor.MediaConverter;

/**
 * A MediaFile object represents a media source located on disk. Through file
 * type checking, a valid media object can be formed.
 * 
 * @author adav194
 * 
 */
public class MediaFile {

	private File path;
	private MediaFormat format = MediaFormat.Unknown;
	private Double duration = 0.0;

	public MediaFile(File location) {
		path = location;
		if (!location.exists() || !location.isFile()) {
			return;
		} else {
			String typeFromProbe = probeForType();
			format = MediaFormat.processTypeFromString(typeFromProbe);
		}
	}

	/*
	 * Convenience constructor used to create a MediaFile at a temporary
	 * location.
	 */
	private MediaFile(MediaFormat desiredFormat) {
		File file = new File(System.getProperty("user.dir") + "/.temp/"
				+ +this.hashCode() + "." + desiredFormat.getExtension());
		while (file.exists()) {
			file = new File(System.getProperty("user.dir") + "/.temp/"
					+ file.hashCode() + "." + desiredFormat.getExtension());
		}
	}

	/**
	 * @see javafx.scene.media.MediaPlayer MediaPlayer
	 * @return A MediaPlayer compatible Media object.
	 */
	public Media getMedia() {
		return new Media(path.toURI().toString());
	}

	/**
	 * @see utility.media.MediaType MediaType
	 * @return The type of the Media source.
	 */
	public MediaType getType() {
		return format.getType();
	}

	/**
	 * @see utility.media.MediaFormat MediaFormat
	 * @return The format of the Media source.
	 */
	public MediaFormat getFormat() {
		return format;
	}

	/**
	 * @return The physical location of the Media source on disk.
	 */
	public File getPath() {
		return path;
	}

	/**
	 * @return The duration of the Media source.
	 */
	public Double getDuration() {
		return duration;
	}

	/**
	 * @return True if the MediaFile is a valid Media Container.
	 * @see utility.control.MediaFormat#isValid() isValid()
	 */
	public boolean isValid() {
		return format.isValid();
	}
	/**
	 * Deletes the Media source which the MediaFile is pointing to.
	 */
	public void delete() {
		path.delete();
		format = MediaFormat.Unknown;
	}

	/**
	 * This function can be used to create an empty media container of a
	 * selected type.
	 * 
	 * @param desiredFormat
	 *            The desired format of the media container.
	 * @param desiredLocation
	 *            The desired location of the media container.
	 * @see utility.media.MediaFormat MediaFormat
	 * @return An UnknownMedia object with attached container (if successfully
	 *         created).
	 */
	public static MediaFile createMediaContainer(MediaFormat desiredFormat,
			File desiredLocation) {
		File correctLocation = addExtension(desiredLocation, desiredFormat);
		String expansion = "ffmpeg -y -filter_complex \"aevalsrc=0::duration=0.1\" "
				+ correctLocation.getAbsolutePath();
		String[] cmd = { "bash", "-c", expansion };
		ProcessBuilder build = new ProcessBuilder(cmd);
		Process p;
		try {
			p = build.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return new MediaFile(correctLocation);
	}

	/**
	 * This is a convenience method for creating a temporary MediaFile on disk.
	 * 
	 * @param desiredFormat
	 *            The desired format of the media container.
	 * @see utility.media.MediaFormat MediaFormat
	 * @return An UnknownMedia object with attached container (if successfully
	 *         created).
	 */
	public static MediaFile createMediaContainer(MediaFormat desiredFormat) {
		MediaFile output = new MediaFile(desiredFormat);
		output.path = addExtension(output.path, desiredFormat);
		String expansion = "ffmpeg -y -filter_complex \"aevalsrc=0::duration=0.1\" "
				+ output.path.getAbsolutePath();
		String[] cmd = { "bash", "-c", expansion };
		ProcessBuilder build = new ProcessBuilder(cmd);
		Process p;
		try {
			p = build.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return output;
	}

	/*
	 * A helper function which probes an UnknownMedia for its true file type.
	 */
	private String probeForType() {
		String expansion = "ffprobe " + path.getAbsolutePath();
		String[] cmd = { "bash", "-c", expansion };
		ProcessBuilder build = new ProcessBuilder(cmd);
		build.redirectErrorStream(true);
		Process p;
		try {
			p = build.start();
			BufferedReader processOutput = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			String currentLineOfOutput;
			String[] splitOfCurrentLine;
			while ((currentLineOfOutput = processOutput.readLine().trim()) != null) {

				if (currentLineOfOutput.startsWith("Duration:")) {
					// Example changes are as shown.
					// Duration: 00:00:01.01, bitrate: 256 kb/s
					splitOfCurrentLine = currentLineOfOutput.split(",");
					// [Duration: 00:00:01.01] [bitrate: 256 kb/s]

					splitOfCurrentLine = splitOfCurrentLine[0].split(" ");
					// [Duration:] [00:00:01.01]
					duration = MediaConverter
							.timeToSeconds(splitOfCurrentLine[1]);

				}
				if (currentLineOfOutput.startsWith("Input #0,")) {
					splitOfCurrentLine = currentLineOfOutput.split(",");
					if (splitOfCurrentLine.length >= 2) {
						return splitOfCurrentLine[1].trim().toUpperCase();
					}
					return "";
				}
			}
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * A helper function which takes a file path and a media format, and
	 * attempts to apply the correct extension syntax.
	 */
	private static File addExtension(File toAddExtension,
			MediaFormat validFormatExtension) {
		String absolutePath = toAddExtension.getAbsolutePath();
		if (!absolutePath.endsWith(validFormatExtension.getExtension())) {
			absolutePath = absolutePath.concat("."
					+ validFormatExtension.getExtension());
			return new File(absolutePath);
		} else {
			return toAddExtension;
		}
	}

}