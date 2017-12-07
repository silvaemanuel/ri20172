package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Invert {

	public static void main(String[] args) throws FileNotFoundException {
		Map<String, Set<Doc>> wordDocMap = new HashMap<>();
		String[] atrib = new String[] { "gênero", "desenvolvedor", "classificação", "plataforma", "jogadores" };
		
		File folder = new File("C:\\Users\\eaa4\\Desktop\\Txts");

		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			for (int i = 1; i <= 2; i++) {
				Scanner tdsc = new Scanner(new File(file.getPath()));
				Doc document = new Doc("doc" + i);
				while (tdsc.hasNext()) {
					String word = tdsc.next();
					boolean flag = false;
					for (i = 0; i < atrib.length; i++) {
						if (word.equals(atrib[i])) {
							flag = true;
						}
					}
					if (flag) {
						document.put(word);
						Set<Doc> documents = wordDocMap.get(word);
						if (documents == null) {
							documents = new HashSet<>();
							wordDocMap.put(word, documents);
						}
						documents.add(document);
					}
				}
				tdsc.close();
			}
		}

		StringBuilder builder = new StringBuilder();
		for (String word : wordDocMap.keySet()) {
			Set<Doc> documents = wordDocMap.get(word);
			builder.append(word + ":");
			for (Doc document : documents) {
				builder.append(document.getDocName() + ":" + document.getCount(word));
				builder.append(", ");
			}
			builder.delete(builder.length() - 2, builder.length() - 1);
			builder.append("\n");
		}
		System.out.println(builder);
	}

	static class Doc {
		String docName;
		Map<String, Integer> m = new HashMap<>();

		public Doc(String docName) {
			this.docName = docName;
		}

		public void put(String word) {
			Integer freq = m.get(word);
			m.put(word, (freq == null) ? 1 : freq + 1);
		}

		public Integer getCount(String word) {
			return m.get(word);
		}

		public String getDocName() {
			return this.docName;
		}
	}

}
