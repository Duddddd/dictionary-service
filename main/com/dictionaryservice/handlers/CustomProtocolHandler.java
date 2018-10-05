package com.dictionaryservice.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.dictionaryservice.persistors.DictionaryDataStorFileImpl;
import com.dictionaryservice.persistors.IDictionaryDataStore;

public class CustomProtocolHandler implements ICallHandler {
	private IDictionaryDataStore dataStore = null;
	private final String DEFAULT_FILE_NAME = "dataFile.properties";

	public CustomProtocolHandler() throws IOException {
		dataStore = new DictionaryDataStorFileImpl(DEFAULT_FILE_NAME);
	}

	@Override
	public String handleCall(String call) throws Exception {
		String[] params = call.split(",");
		String method = params[0];
		String res = null;
		switch (method) {
		case "getAllKeys": {
			List<String> keys = dataStore.getAllKeys(params[1]);
			res = fromListToStringByProtocol(keys);
			break;
		}
		case "rightAdd": {
			dataStore.rightAdd(params[1], params[2]);
			res = "success";
			break;
		}
		case "leftAdd": {
			dataStore.leftAdd(params[1], params[2]);
			res = "success";
			break;
		}
		case "set": {
			String key = params[1];
			List<String> values = Arrays.asList(params).subList(2, params.length);
			dataStore.set(key, values);
			res = "success";
			break;
		}
		case "get": {
			String key = params[1];
			List<String> values = dataStore.get(key);
			res = fromListToStringByProtocol(values);
			break;
		}
		default:
			throw new Exception("the method " + method + "is not recognized");
		}
		return res;
	}

	private String fromListToStringByProtocol(List<String> values) {
		StringBuilder builder = new StringBuilder();
		values.stream().forEach(k -> builder.append(k).append(","));
		return builder.toString();
	}

}
