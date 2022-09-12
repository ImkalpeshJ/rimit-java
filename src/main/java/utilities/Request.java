package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Request {

	Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

		@Override
		public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
			if (src == src.longValue())
				return new JsonPrimitive(src.longValue());
			return new JsonPrimitive(src);
		}
	}).create();

	public JsonObject confirmTransaction(String head, String result, String data, String uri, String key) {

		Map<String, Object> content = new HashMap<String, Object>();
		content.put("result", gson.fromJson(result, HashMap.class));
		content.put("data", gson.fromJson(data, HashMap.class));

		Map<String, Object> encryptData = new HashMap<String, Object>();
		encryptData.put("content", content);

		final String stringData = gson.toJson(encryptData);

		System.out.println("---------------------");
		System.out.println("DATA TO BE ENCRYPTED");
		System.out.println(uri);
		System.out.println(stringData);
		System.out.println("---------------------");

		final String encrypted = Crypto.encryptRimitData(stringData, key);

		Map<String, Object> encryptDetail = new HashMap<String, Object>();
		encryptDetail.put("head", gson.fromJson(head, HashMap.class));
		encryptDetail.put("encrypted_data", gson.fromJson(encrypted, HashMap.class));

		String requestBody = gson.toJson(encryptDetail);

		System.out.println("REQUEST BODY");
		System.out.println(requestBody);

		URL url;
		try {
			url = new URL(uri);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "application/json");

			byte[] out = requestBody.getBytes(StandardCharsets.UTF_8);

			OutputStream stream = http.getOutputStream();
			stream.write(out);

			System.out.println("Response");
			System.out.println(http.getResponseCode() + " ");

			BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();

			http.disconnect();

			System.out.println(sb.toString());

			JsonElement responseElement = gson.fromJson(sb.toString(), JsonElement.class);

			JsonObject decrypted = Crypto
					.decryptRimitData(responseElement.getAsJsonObject().getAsJsonObject("encrypted_data"), key);

			return decrypted.getAsJsonObject("content");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
}
