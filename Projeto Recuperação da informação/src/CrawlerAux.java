import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerAux {
	private static final String USER_AGENT =
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private List<String> exceptions = new LinkedList<String>();
	private List<String> regexExceptions = new LinkedList<String>();
	private Document htmlDocument; 

	public void crawl(String url){
		try{
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			System.out.println(url);

			Elements linksOnPage = htmlDocument.select("a[href]");
			//System.out.println("Encontrou (" + linksOnPage.size() + ") links");
			for(Element link : linksOnPage){
				this.links.add(link.absUrl("href"));
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Erro na saída HTTP request " + ioe);
		}
	}

	public boolean searchForWord(String searchWord){
		System.out.println("Procurando a palavra " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	//Método que pesquisa no corpo do HTML se o mesmo contém as palavras chave indicadas. 
	public boolean searchGameBodyText(){
		boolean returnPage = false;
		String[] keyWords = {"R$", "cartão", "boleto", "jogo", "plataforma", "gênero", "requisitos", "game", "jogador", "mutiplayer", "desenvolvedor"};
		if (this.htmlDocument != null) {
			String bodyText = this.htmlDocument.body().text();
			int count = 0;
			for(int i = 0; i < keyWords.length; i++) {
				if(bodyText.toLowerCase().contains(keyWords[i])) {
					count++;
				}
			}
			//System.out.println(" Esse link tem " + count + " keywords");
			if(count >= 7) {
				returnPage = true;
			}
		}
		return returnPage;
	}

	//Método que retorna todos os links
	public List<String> getLinks(){
		return this.links;
	}

	//Método que retorna todos os exceções
	public List<String> getExceptions(){
		return this.exceptions;
	}

	//Método que remove todos os links da classe
	public void removeLinks(){
		this.links = new LinkedList<String>();;
	}

	//Método que utiliza a url inicial para acessar o robots.txt e salvar sua exceções.
	public void createExceptions(String url) {
		try{
			Connection connection = Jsoup.connect(url + "/robots.txt").userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			System.out.println(url + "/robots.txt");

			String body = htmlDocument.body().text();

			while (body.contains("Disallow: ")) {
				body = body.substring(body.indexOf("Disallow: ") + 10);
				if (body.contains(" ")) {
					String exception = body.substring(0, body.indexOf(" "));
					body = body.substring(body.indexOf(" "), body.length());
					this.exceptions.add(exception);
					//System.out.println(exception);
				}else {
					String exception = body;
					this.exceptions.add(exception);
				}
			}
			createRegex(url);
			
			System.out.println(regexExceptions);
		}
		catch(IOException ioe)
		{
			System.out.println("Erro na saída HTTP request " + ioe);
		}
	}
	
	public void createRegex(String urlPrefix){
		
		for(String exception: exceptions) {
			String regex = "";

			if(exception.charAt(0) == '*') {
				exception = exception.substring(1, exception.length());
				if(!exception.contains("*")) {
					regex = ".*(";
				}
			}else {
				regex = "^(";
			}
			if(exception.contains("*")) {
				while(exception.contains("*")) {
					regex = regex + ".*(" + exception.substring(0, exception.indexOf("*")) + ")";
					exception = exception.substring(exception.indexOf("*") + 1, exception.length());
					if(!exception.contains("*")) {
						regex = regex + ".*(" + exception + ")";
						exception = exception.substring(exception.indexOf("*") + 1, exception.length());
						if(regex.charAt(0) == '^') {
							regex = regex + ")";
						}
					}
				}
			}else {
				regex = regex + exception + ")";
			}
			regex = fixRegex(regex);
			this.regexExceptions.add(regex);
		}
	}

	public String fixRegex(String regex) {
		String fixedRegex = "";
		for(int i = 0; i < regex.length(); i++) {
			if((regex.charAt(i) + "").matches("[?]")) {
				fixedRegex = fixedRegex + "\\" + regex.charAt(i);
			}else {
				fixedRegex = fixedRegex + regex.charAt(i);
			}
		}
		return fixedRegex;
	}
	
	public boolean checkExceptions(String prefix) {
		boolean valReturn = true;
		for(String exception: regexExceptions) {
			if(prefix.matches(exception)) {
				valReturn = false;
			}
		}
		return valReturn;
	}
}
