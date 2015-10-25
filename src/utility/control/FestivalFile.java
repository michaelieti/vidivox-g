package utility.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import utility.media.MediaFile;

public class FestivalFile {

	private File scmPath;
	private StringBuilder inputBuilder = new StringBuilder();

	public enum VoiceActor {
		Jono("voice_akl_nz_jdt_diphone"), Robot("voice_kal_diphone"), Gordon(
				"voice_rab_diphone");

		private String voiceName;

		private VoiceActor(String voice) {
			voiceName = voice;
		}

		public String getVoice() {
			return "(" + voiceName + ")\n";
		}
	}

	public enum VoiceEmotion {
		Happy("Happy"), Neutral("Neutral");

		private String emotionTag;

		private VoiceEmotion(String emotion) {
			emotionTag = emotion;
		}

		public String getEmotion() {
			return emotionTag;
		}
	}

	public static void main(String[] args) throws IOException {
		FestivalFile ff = new FestivalFile(
				new File(System.getProperty("user.home")
						+ "/SoftEng206/ffmpeg/derp.scm"));
		ff.save(new File(System.getProperty("user.home")
				+ "/SoftEng206/ffmpeg/ddderp.wav"), VoiceActor.Gordon, VoiceEmotion.Happy, "Hello world");
	}

	public FestivalFile() {
		scmPath = new File(System.getProperty("user.dir") + "/.temp/"
				+ this.hashCode() + ".scm");
		while (scmPath.exists()) {
			scmPath = new File(System.getProperty("user.dir") + "/.temp/"
					+ scmPath.hashCode() + ".scm");
		}
	}

	public FestivalFile(File path) {
		this.scmPath = path;
	}

	public File getPath() {
		return scmPath;
	}

	public void save(File output, VoiceActor actor, VoiceEmotion emotion,
			String text) throws IOException {
		inputBuilder
				.append("(require '/netmount/usr/local/festival/initcustom)\n");
		inputBuilder.append(actor.getVoice());
		Formatter appendWithFormat = new Formatter(inputBuilder);
		appendWithFormat.format("(utt.save.wave ");
		appendWithFormat.format("(SayEmotional '%s \"%s\" 2) \"%s\" 'riff)\n",
				emotion, text, output.getAbsolutePath());
		appendWithFormat.close();
		FileWriter scmFile = new FileWriter(scmPath);
		scmFile.write(inputBuilder.toString());
		scmFile.close();
		System.out.println(inputBuilder.toString());
	}

}
