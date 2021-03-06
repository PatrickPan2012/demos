package com.patrick;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Patrick Pan
 *
 */
public class SpeakerRecognitionUtils {
	private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();
	private static final String IDENTIFICATION_PROFILE_IDS = buildIdentificationProfileIds();

	private static final String IDENTIFICATION_API_URL = new StringBuilder(
			"https://westus.api.cognitive.microsoft.com/spid/v1.0/identify?identificationProfileIds=")
					.append(IDENTIFICATION_PROFILE_IDS).append("&shortAudio=").append(true).toString();
	private static final String OPERATION_STATUS_API_URL = "https://westus.api.cognitive.microsoft.com/spid/v1.0/operations/";

	private static final Logger LOGGER = LoggerFactory.getLogger("speakerRecognitionLogger");
	private static final String STATUS = "status";

	private static String identify(String filePath) {
		try {
			return invokeIdentifyAPI(filePath);
		} catch (IOException | URISyntaxException e) {
			LOGGER.error(
					"Exception occurs during invoking Microsoft Cognitive Service - Speaker Recognition Identification.",
					e);
			return "";
		}
	}

	private static String invokeIdentifyAPI(String filePath) throws IOException, URISyntaxException {
		try (InputStream inputStream = new FileInputStream(filePath)) {
			return invokeIdentifyAPI(inputStream);
		}
	}

	private static String invokeIdentifyAPI(InputStream inputStream) throws URISyntaxException, IOException {
		URI uri = new URIBuilder(IDENTIFICATION_API_URL).build();

		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.getMimeType());
		request.setHeader("Ocp-Apim-Subscription-Key", Authentication.SUBSCRIPTION_KEY);

		request.setEntity(new InputStreamEntity(inputStream));

		HttpResponse response = HTTP_CLIENT.execute(request);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 202) {
			String responseBody = EntityUtils.toString(response.getEntity());
			LOGGER.info(responseBody);
			return "";
		}

		Header header = response.getFirstHeader("Operation-Location");
		if (header == null) {
			LOGGER.warn("'Operation-Location' cannot be found in response header.");
			return "";
		}

		String value = header.getValue();
		return value.substring(value.lastIndexOf('/'));
	}

	private static JSONObject getOperationStatus(String operationId) {
		try {
			return invokeGetOperationStatusAPI(operationId);
		} catch (URISyntaxException | IOException e) {
			LOGGER.error(
					"Exception occurs during invoking Microsoft Cognitive Service - Speaker Recognition Get Operation Status.",
					e);
			return new JSONObject();
		}
	}

	private static JSONObject invokeGetOperationStatusAPI(String operationId) throws URISyntaxException, IOException {
		URI uri = new URIBuilder(new StringBuilder(OPERATION_STATUS_API_URL).append(operationId).toString()).build();

		HttpGet request = new HttpGet(uri);
		request.setHeader("Ocp-Apim-Subscription-Key", Authentication.SUBSCRIPTION_KEY);

		HttpResponse response = HTTP_CLIENT.execute(request);

		String responseEntity = EntityUtils.toString(response.getEntity());
		if (responseEntity.isEmpty()) {
			return new JSONObject();
		}

		return new JSONObject(responseEntity);
	}

	private static String buildIdentificationProfileIds() {
		StringBuilder identificationProfileIds = new StringBuilder();

		Authentication.IdentificationProfile[] identificationProfiles = Authentication.IdentificationProfile.values();

		String delimiter = "";
		for (int i = 0; i < identificationProfiles.length; i++) {
			identificationProfileIds.append(delimiter).append(identificationProfiles[i].getProfileId());
			delimiter = ",";
		}

		return identificationProfileIds.toString();
	}

	public static JSONObject getIdentification(String filePath) {
		String operationId = identify(filePath);

		if (operationId.isEmpty()) {
			return new JSONObject();
		}

		JSONObject operationStatus;

		do {
			waitForOneSecond();
			operationStatus = getOperationStatus(operationId);
		} while (operationStatus.has(STATUS) && ("notstarted".equalsIgnoreCase(operationStatus.getString(STATUS))
				|| "running".equalsIgnoreCase(operationStatus.getString(STATUS))));

		LOGGER.debug("Operation Status is: {}.", operationStatus);
		return operationStatus;
	}

	private static void waitForOneSecond() {
		try {
			TimeUnit.SECONDS.sleep(1l);
		} catch (InterruptedException e) {
			LOGGER.error("Exception occurs in TimeUnit#sleep.", e);
			Thread.currentThread().interrupt();
		}
	}

	private SpeakerRecognitionUtils() {
	}
}
