package utility.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import java.nio.file.Path;
import java.util.Scanner;

import utility.control.BackgroundTask;
import utility.control.FFMPEG;

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

	/**
	 * This constructor should be called if you want to create a Media File from
	 * a file that is already on disk.
	 * <P>
	 * If you wish to initialize an empty MediaFile use
	 * {@link #createMediaContainer()}.
	 * 
	 * @param location
	 *            Locaiton of a pre-existing media file.
	 */
	public MediaFile(File location) {
		path = location;
		if (!location.exists() || !location.isFile()) {
			return;
		} else {
			String typeFromProbe = probeForType();
			format = MediaFormat.processTypeFromString(typeFromProbe);
		}
	}

	/**
	 * This constructor should be called if you want to create a Media File from
	 * a {@link javafx.scene.media.Media} object.
	 * <P>
	 * If you wish to initialize an empty MediaFile use
	 * {@link #createMediaContainer()}.
	 * 
	 * @param location
	 *            Location of a pre-existing media file.
	 */
	public MediaFile(Media media) throws URISyntaxException {
		this(Paths.get(new URI(media.getSource())).toFile());
	}

	/*
	 * Convenience constructor used to create a MediaFile at a temporary
	 * location.
	 */
	private MediaFile(MediaFormat desiredFormat) {
		path = new File(System.getProperty("user.dir") + "/.temp/"
				+ Math.abs(this.hashCode()) + "."
				+ desiredFormat.getExtension());
		while (path.exists()) {
			path = new File(System.getProperty("user.dir") + "/.temp/"
					+ Math.abs(path.hashCode()) + "."
					+ desiredFormat.getExtension());
		}
		format = desiredFormat;
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
	 * This method is to handle the cases where an Absolute Path may contain
	 * spaces. This is problematic for bash programs.
	 * 
	 * @return The absolute path of this MediaFile surrounded by quotations.
	 */
	public String getQuoteOfAbsolutePath() {
		return "\"" + path.getAbsolutePath() + "\"";
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

	/*
	 * A helper function which probes an UnknownMedia for its true file type.
	 */
	private String probeForType() {
		String output = null;
		String expansion = "ffprobe \"" + path.getAbsolutePath() + "\"";
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
			while ((currentLineOfOutput = processOutput.readLine()) != null) {
				currentLineOfOutput = currentLineOfOutput.trim();
				if (currentLineOfOutput.startsWith("Duration:")) {
					// Example changes are as shown.
					// Duration: 00:00:01.01, bitrate: 256 kb/s
					splitOfCurrentLine = currentLineOfOutput.split(",");
					// [Duration: 00:00:01.01] [bitrate: 256 kb/s]

					splitOfCurrentLine = splitOfCurrentLine[0].split(" ");
					// [Duration:] [00:00:01.01]
					duration = timeToSeconds(splitOfCurrentLine[1]);

				}
				if (currentLineOfOutput.startsWith("Input #0,")
						&& output == null) {
					splitOfCurrentLine = currentLineOfOutput.split(",");
					if (splitOfCurrentLine.length < 2) {
						output = "";
					} else if (splitOfCurrentLine.length > 3) {
						for (String s : splitOfCurrentLine) {
							if (MediaFormat.getFromString(s).isValid()) {
								output = MediaFormat.getFromString(s)
										.getExtension().toUpperCase();
								break;
							}
						}
						if (output == null) {
							output = "";
						}
					} else {
						output = splitOfCurrentLine[1].trim().toUpperCase();
					}
				}
				if (currentLineOfOutput.startsWith("major_brand     :")
						&& output == null) {
					splitOfCurrentLine = currentLineOfOutput.split(":");
					if (splitOfCurrentLine.length < 2) {
						output = "";
					} else {
						output = splitOfCurrentLine[1];
					}
				}
			}
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return output;
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

	public static MediaFile createMediaContainer(BackgroundTask queue, MediaFormat desiredFormat,
			File desiredLocation, String fileName) {
		fileName = addExtension(fileName, desiredFormat);

		if (desiredLocation.exists()) {
			System.out.println("A file/directory by the name at '" + desiredFormat + "' already exists");
			return new MediaFile(desiredLocation);
		}
		desiredLocation = new File(desiredLocation.getAbsolutePath() + fileName);

		// Not sure if this is needed. If the user specifies a location would
		// they want the location to be changed to accommodate for extension?
		//
		// File desiredLocation = addExtension(desiredLocation, desiredFormat);

		String ffmpegCommand = "ffmpeg -y -filter_complex \"aevalsrc=0::duration=0.1\" \""
				+ desiredLocation.getAbsolutePath() + "\"";
		FFMPEG cmd = new FFMPEG(new SimpleDoubleProperty(0), ffmpegCommand, 0.0);
		cmd.queueTo(queue);
		cmd.setName("Create Empty " + desiredFormat);
		return new MediaFile(desiredLocation);
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
	public static MediaFile createMediaContainer(BackgroundTask queue,
			MediaFormat desiredFormat) {
		/* Initializing Media Container */
		MediaFile output = new MediaFile(desiredFormat);
		output.path = addExtension(output.path, desiredFormat);

		/* Determining type of container to make */
		String ffmpegCommand;
		if (desiredFormat.getType().equals(MediaType.Video)) {
			ffmpegCommand = "ffmpeg -y -f lavfi -i nullsrc -frames:v 1 "
					+ output.getQuoteOfAbsolutePath();
		} else {
			ffmpegCommand = "ffmpeg -y -filter_complex \"aevalsrc=0::duration=0.1\" \""
					+ output.path.getAbsolutePath() + "\"";
		}

		/* Constructing Container */
		FFMPEG cmd = new FFMPEG(new SimpleDoubleProperty(0), ffmpegCommand, 0.0);
		queue.addTask(cmd);
		cmd.setName("Create Empty " + desiredFormat);
		output.format = desiredFormat;
		output.duration = 0.0;
		return output;
	}

	private static String addExtension(String name, MediaFormat formatNeeded) {
		if (!name.endsWith(formatNeeded.getExtension())) {
			name = name + formatNeeded.getExtension();
			return name;
		} else {
			return name;
		}
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
	
	/**
	 * This helper function takes a string in the form "hh:mm:ss" and returns
	 * the amount of seconds this amounts to.
	 * 
	 * @param time
	 * @return
	 */
	public static Double timeToSeconds(String time) {
		time = time.replace(":", " ");
		time = time.replace(".", " ");
		Scanner scan = new Scanner(time);
		// Calculating hours
		int seconds = scan.nextInt() * 3600;
		// Calculating minutes
		seconds += scan.nextInt() * 60;
		// Calculating seconds
		seconds += scan.nextInt();
		scan.close();
		return (double) seconds;
	}

}
