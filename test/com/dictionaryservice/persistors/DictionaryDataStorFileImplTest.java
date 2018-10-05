package com.dictionaryservice.persistors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class DictionaryDataStorFileImplTest {

	public static void main(String[] args) throws IOException {
		DictionaryDataStorFileImpl dic = new DictionaryDataStorFileImpl("DicTestFile.txt");
		
		dic.set("anna", Arrays.asList("aa", "bb"));
		dic.set(" 5456 cat ", Arrays.asList("uuu"));
		
		System.out.println(dic.getAllKeys("\\bcat\\b"));
		
		

	}

}
