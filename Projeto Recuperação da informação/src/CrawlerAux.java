import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	private List<Node> linksList = new LinkedList<Node>();
	private List<String> exceptions = new LinkedList<String>();
	private List<String> regexExceptions = new LinkedList<String>();
	private Document htmlDocument; 
	
	private String name;
	private Integer count = 0;
	
	public CrawlerAux(String name) {
		this.name = name;
	}

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
			
			savePage(htmlDocument);
			
			System.out.println(url);

			Elements divsions = htmlDocument.select("div");
			String[] keyWords = {"jogo", "plataforma", "gênero",
					"requisitos", "game", "jogador", "mutiplayer",
					"desenvolvedor", "videogame", "app"};
			int score = 0;
			for(Element div : divsions){
				if (div.toString().contains("</a>")){
					for(int i = 0; i < keyWords.length; i++) {
						if(div.toString().contains(keyWords[i])) {
							score++;
						}
					}
					Elements linksOnPage = div.select("a[href]");

					for(Element link : linksOnPage){
						int scoreAux = score;
						for(int i = 0; i < keyWords.length; i++) {
							if(link.absUrl("href").toString().toLowerCase().contains(keyWords[i].toLowerCase())) {
								if(link.absUrl("href").toString().contains("filtro")) {
									scoreAux = scoreAux + 1;
								}else{
									scoreAux = scoreAux + 100;
								}
							}
						}
						insertList(new Node(link.absUrl("href"), scoreAux));
					}
				}
			}
			Collections.sort(this.linksList, new Node.CompareScore());
			System.out.println("");
		}
		catch(IOException ioe)
		{
			System.out.println("Erro na saída HTTP request " + ioe);
		}
	}
	
	public void savePage(Document doc) throws IOException {
        File f = new File("C:\\Users\\lrb\\Downloads\\" + this.name + count + ".html");
        f.getParentFile().mkdirs();
        Writer out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        out.write(doc.outerHtml() + '\n');
        out.write("site_url: " + doc.baseUri()); // saves page url
        out.close();
        count +=1;
    }
	
	public void insertList(Node n) {
		boolean exists = false;
		List<Node> linksListAux = new LinkedList(this.linksList);
		for(Node link : linksListAux){
			if (link.getLink().equals(n.getLink())) {
				this.linksList.remove(link);
				this.linksList.add(n);
				exists = true;
			}
		}
		if(!exists) {
			this.linksList.add(n);
		}
	}

	//Método que retorna todos os links
	public List<Node> getLinks(){
		return this.linksList;
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
				regex = regex + exception + ").*";
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

	public static class Node{
		private String link;
		private Integer score;
		
		 public static class CompareScore implements Comparator< Node >
         {
             @Override
             public int compare( Node o1,  Node o2 )
             {
                 return o2.score.compareTo( o1.score );
             }
         }

		public Node(String link, Integer score) {
			this.link = link;
			this.score = score;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
	}
}

