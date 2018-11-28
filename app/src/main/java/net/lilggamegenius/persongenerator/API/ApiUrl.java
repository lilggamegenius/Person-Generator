package net.lilggamegenius.persongenerator.API;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ApiUrl {
	private final String API_URL;
	private final String API_KEY;
	private final String API_PATH;

	Map<String, String> map = new HashMap<>();

	public ApiUrl(String apiUrl) {
		API_URL = apiUrl;
		API_PATH = "/";
		API_KEY = null;
	}

	ApiUrl(String apiUrl, String apiPath) {
		API_URL = apiUrl;
		API_PATH = apiPath;
		API_KEY = null;
	}

	public ApiUrl(String apiUrl, String apiPath, String apiKey) {
		API_URL = apiUrl;
		API_PATH = apiPath;
		API_KEY = apiKey;
	}

	public String getAPI_PATH() {
		return API_PATH;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	void setParameter(String key, String value) {
		map.put(key, value);
	}

	public String getParameter(String key) {
		return map.get(key);
	}

	@NonNull
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(API_URL);
		builder.append(API_PATH).append('?');
		if (API_KEY != null) {
			builder.append("API_KEY=").append(API_KEY).append("&");
		}
		StringBuilder acc = new StringBuilder();
		for (Map.Entry<String, String> p : map.entrySet()) {
			String value = p.getValue();
			String s = value == null ? p.getKey() : p.getKey() + "=" + value;
			acc.append(s).append("&");
		}
		builder.append(acc);
		return builder.toString();
	}
}
