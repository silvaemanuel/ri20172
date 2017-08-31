import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerAux {
	private static final String USER_AGENT =
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private Document htmlDocument; 

	public void crawl(String url){
		try{
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			//System.out.println("Página web recebida em " + url);

			Elements linksOnPage = htmlDocument.select("a[href]");
			//System.out.println("Encontrou (" + linksOnPage.size() + ") links");
			for(Element link : linksOnPage)
			{
				this.links.add(link.absUrl("href"));
			}
		}
		catch(IOException ioe)
		{
			// We were not successful in our HTTP request
			System.out.println("Erro na saída HTTP request " + ioe);
		}
	}

	public boolean searchForWord(String searchWord){
		System.out.println("Procurando a palavra " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

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
			if(count >= 7) {
				returnPage = true;
			}
		}
		return returnPage;
	}

	public List<String> getLinks(){
		return this.links;
	}

}
