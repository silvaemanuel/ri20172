package com.myParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class HtmlToTxt {
	public static String html2PlainText(String html) throws IOException {
		String result = html;
		result = cleanTagPerservingLineBreaks(result);
		result = StringEscapeUtils.unescapeHtml4(result);
		result = removeUrl(result);

		return result;
	}

	public static String cleanTagPerservingLineBreaks(String html) {
		String result = html;
		if (html == null)
			return html;
		Document document = Jsoup.parse(html,"UTF-8");
		document.outputSettings(new Document.OutputSettings().prettyPrint(false));
		document.select("br").append("\n");
		document.select("p").append(" ");
		document.select("ul").append("\n");
		document.select("li").append(" ");
		document.select("a").append(" ");
		document.select("tr").append("\n");
		document.select("td").append(" ");
		document.select("header").append(" ");
		document.select("spam").append(" ");
		result = document.html().replaceAll("\\n", " ");
		result = document.html().replaceAll("\\\n", " ");
		result = document.html().replaceAll("\\\\n", " ");
		result = Jsoup.clean(result, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
		return result;
	}

	public static String removeUrl(String str) {
		String regex = "\\b(https?|ftp|file|telnet|http|Unsure)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		str = str.replaceAll(regex, "");
		return str;
	}

	public static void folderScan(File folder) throws IOException {

		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			StringBuilder contentBuilder = new StringBuilder();
			BufferedReader in = new BufferedReader(new FileReader(file.getPath()));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
			String content = contentBuilder.toString();
			String resultado = html2PlainText(content);
			try (PrintWriter out = new PrintWriter(
					// "C:/Users/Eduardo/Desktop/WebPages/Americanas/Pos/Txt/" + file.getName() +
					// ".txt"))
					"C:/Users/Eduardo/Desktop/WebPages/Sites/Walmart/Txts/Neg/" + file.getName() + ".txt"))  {
				out.println(resultado.toLowerCase());
			}
		}
	}

	public static void main(String[] argh) throws IOException {

		// File folder = new
		// File("C:/Users/Eduardo/eclipse-workspace/TesteJava/Americanas/Pos");
		File folder = new File("C:/Users/Eduardo/Desktop/WebPages/Sites/Walmart/Neg/");
		folderScan(folder);
	}
}