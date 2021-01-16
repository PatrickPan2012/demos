package com.patrick;

import static com.patrick.Authentication.LOCATION;
import static com.patrick.Authentication.SUBSCRIPTION_KEY;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;;

/**
 * 
 * @author Patrick Pan
 *
 */
@WebServlet("/text")
public class SpeechToTextServlet extends HttpServlet {

	private static final String DEST = Config.getInstance().getDest();
	private static final Logger LOGGER = LoggerFactory.getLogger("speechToTextLogger");
	private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.fromSubscription(SUBSCRIPTION_KEY, LOCATION);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2570959304963464443L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		LOGGER.info("Receive a new request.");
		resp.setContentType("application/json");
		if (DEST.isEmpty()) {
			printResponse(resp, 500, INTERNAL_SERVER_ERROR);
			return;
		}

		String filename = (new Date()).getTime() + "";
		String filePath = new StringBuilder(DEST).append(File.separatorChar).append(filename).toString();

		try {
			saveAudio(req, filePath);
		} catch (IOException e) {
			LOGGER.error("Exception occurs in SpeechToTextServlet.saveAudio.", e);
			printResponse(resp, 500, INTERNAL_SERVER_ERROR);
			return;
		}

		LOGGER.debug("The audio file is saved to '{}'.", filePath);
		LOGGER.info("Use FFmpeg to transcode.");

		String wavFilePath = FFmpegUtils.transcode(filename);

		if (wavFilePath.isEmpty()) {
			printResponse(resp, 500, INTERNAL_SERVER_ERROR);
			return;
		}

		LOGGER.debug("The audio file '{}' is transcoded to wav and new audio file is '{}'.", filePath, wavFilePath);
		LOGGER.info("Invoke Microsoft Cognitive Services Speech-to-text API.");

		try {
			buildResponse(resp, wavFilePath);
		} catch (JSONException e) {
			LOGGER.error("Exception occurs in SpeechToTextServlet.buildResponse.", e);
			printResponse(resp, 500, INTERNAL_SERVER_ERROR);
		}

		if (!LOGGER.isDebugEnabled()) {
			new File(filePath).delete();
			new File(wavFilePath).delete();
		}
	}

	private void buildResponse(HttpServletResponse resp, String wavFilePath) {
		AudioConfig audioConfig = AudioConfig.fromWavFileInput(wavFilePath);
		String result = null;
		try {
			result = buildResponse(resp, audioConfig);
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Exception occurs in Future.get.", e);
		} catch (Exception e) {
			LOGGER.error("Exception occurs in SpeechToTextServlet.buildResponse.", e);
		}

		if (result == null) {
			printResponse(resp, 500, INTERNAL_SERVER_ERROR);
			return;
		}

		printResponse(resp, 200, result);
	}

	private String buildResponse(HttpServletResponse resp, AudioConfig audioConfig)
			throws InterruptedException, ExecutionException {
		try (SpeechRecognizer recognizer = new SpeechRecognizer(SPEECH_CONFIG, audioConfig)) {
			Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
			SpeechRecognitionResult result = task.get();
			String text = result.getText();

			switch (result.getReason()) {
			case NoMatch:
				LOGGER.warn("NOMATCH: Speech could not be recognized.");
				break;
			case Canceled: {
				CancellationDetails cancellation = CancellationDetails.fromResult(result);
				LOGGER.warn(new StringBuilder("CANCELLED: Reason=").append(cancellation.getReason()).toString());

				if (cancellation.getReason() == CancellationReason.Error) {
					LOGGER.error(new StringBuilder("ErrorCode=").append(cancellation.getErrorCode()).toString());
					LOGGER.error(new StringBuilder("ErrorDetails=").append(cancellation.getErrorDetails()).toString());
				}
			}
				break;
			default:
				LOGGER.debug("The text is '{}'.", text);
			}

			return text;
		}
	}

	private void saveAudio(HttpServletRequest req, String filePath) throws IOException {
		try (InputStream inputStream = req.getInputStream();
				OutputStream outputStream = new FileOutputStream(filePath)) {
			byte[] bytes = new byte[2048];
			int hasRead = 0;
			while ((hasRead = (inputStream.read(bytes))) > 0) {
				outputStream.write(bytes, 0, hasRead);
			}
		}
	}

	private void printResponse(HttpServletResponse resp, int statusCode, String msg) {
		PrintWriter pw = null;

		try {
			pw = resp.getWriter();
		} catch (IOException e) {
			LOGGER.error("Exception occurs in HttpServletResponse.getWriter.", e);
			resp.setStatus(500);
			return;
		}

		resp.setStatus(statusCode);
		JSONObject json = new JSONObject();
		json.put("msg", msg);
		pw.println(json.toString());
		pw.flush();
	}
}
