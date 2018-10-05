package com.dictionaryservice.persistors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryDataStorFileImpl implements IDictionaryDataStore {
	private File file;
	private Map<String, LinkedList<String>> cacheMap = new HashMap<>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	
	public DictionaryDataStorFileImpl(String fileStr) throws IOException {
		file = new File(fileStr);
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	@Override
	public List<String> getAllKeys(String pattern) {
		Pattern p = Pattern.compile(pattern);
		return cacheMap.entrySet().stream()
				.filter(x -> p.matcher(x.getKey()).find())
				.map(x->x.getKey())
				.collect(Collectors.toList());
	}

	@Override
	public void rightAdd(String key, String v) {
		if (cacheMap.containsKey(key)) {
			cacheMap.get(key).addLast(v);
		}else {
			LinkedList<String> toAdd = new LinkedList<>();
			toAdd.add(v);
			cacheMap.put(key, toAdd);
		}
		setAsynch(key, cacheMap.get(key));
	}

	@Override
	public void leftAdd(String key, String v) {
		if (cacheMap.containsKey(key)) {
			cacheMap.get(key).addFirst(v);
		}else {
			LinkedList<String> toAdd = new LinkedList<>();
			toAdd.add(v);
			cacheMap.put(key, toAdd);
		}
		setAsynch(key, cacheMap.get(key));
	}

	@Override
	public void set(String key, List<String> value) {
		cacheMap.put(key, new LinkedList<>(value));
		setAsynch(key, value);
	}



	private void setAsynch(String key, List<String> value) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				Properties prop = loadFile();
				prop.setProperty(key, getStringRepresentation(value));
				saveFile(prop);
			}
		};
		executor.submit(runnable);
	}

	private String getStringRepresentation(List<String> value) {
		StringBuilder res = new StringBuilder();
		for (String string : value) {
			res.append(string).append(",");
		}
		return res.subSequence(0, res.length()-1).toString();
	}

	private void saveFile(Properties prop){
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			prop.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
	}

	private Properties loadFile() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(file);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	@Override
	public List<String> get(String k) {
		return cacheMap.get(k);
	}

}
