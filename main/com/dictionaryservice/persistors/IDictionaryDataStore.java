package com.dictionaryservice.persistors;

import java.util.List;

public interface IDictionaryDataStore {
	
	List<String> getAllKeys(String pattern);
	
	void rightAdd(String key, String v);
	
	void leftAdd(String key, String v);
	
	void set(String key, List<String> value);
	
	List<String> get(String k);
	
}
