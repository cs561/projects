package utils;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONManager {
	private static JSONParser parser = new JSONParser();

	public static JSONObject parseJSONStringToJSONObject(final String str) {
		try {
			return (JSONObject) parser.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject loadJSONObject(final String path) {
		FileReader file = null;
		file = FileManager.readFile(path);
		try {
			return (JSONObject) parser.parse(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(file);
		}
		return null;
	}

	public static JSONObject getObjectByID(final JSONArray arr, final String id) {
		JSONObject obj = null;
		for (int i = 0; i < arr.size(); i++) {
			obj = (JSONObject) arr.get(i);
			if (id.equals(String.valueOf(obj.get("id")))) {
				return obj;
			}
		}
		return obj;
	}

	public static JSONObject getCurrentStageObject() {
		// JSONArray elements = (JSONArray) ((JSONObject)
		// Desk.th.JSONContent.get("stage")).get("elements");
		JSONObject stage = null; // = JSONManager.getObjectByID(elements,
								 // Desk.th.currentStageID);
		return stage;
	}

	public static String getSelectionID(final JSONArray arr) {
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = (JSONObject) arr.get(i);
			if (obj.containsKey("active")) {
				if ((boolean) obj.get("active")) {
					return (String) obj.get("id");
				}
			}
		}
		return null;
	}

	public static JSONObject getSelectionObj(final JSONArray arr) {
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = (JSONObject) arr.get(i);
			if (obj.containsKey("active")) {
				if ((boolean) obj.get("active")) {
					return obj;
				}
			}
		}
		return null;
	}
}
