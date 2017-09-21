import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private static int pageLimit = 100;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public static void main(String[] args) {

		Crawler teste = new Crawler();
		teste.search("https://www.americanas.com.br");
		//		CrawlerAux aux = new CrawlerAux();
		//		aux.getExceptions("https://www.origin.com");

	}

	private String visitNextUrl(){
		String nextUrl = this.pagesToVisit.remove(0);
		while(this.pagesVisited.contains(nextUrl)){
			nextUrl = this.pagesToVisit.remove(0);
		}
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}

	public void search(String url){
		CrawlerAux crawlerAux = new CrawlerAux();
		crawlerAux.checkExceptions(url);
		while(this.pagesVisited.size() < pageLimit){
			String currentUrl;
			crawlerAux.removeLinks();
			if(this.pagesToVisit.isEmpty()){
				currentUrl = url;
				this.pagesVisited.add(url);
			}
			else{
				String https = "https://";
				currentUrl = this.visitNextUrl();
				if(currentUrl.substring(0, https.length()).equals(https)) {

				}else {

				}

				if (!currentUrl.equals(url)) {
					String newUrl = currentUrl.substring(url.length(), currentUrl.length());
					System.out.println("======" + newUrl);
				}
				//Garante que os links visitados devem conter a URL inicial para serem visitados.
				while(!currentUrl.contains(url)) {
					currentUrl = this.visitNextUrl();
				}
			}
			crawlerAux.crawl(currentUrl); 
			//boolean relevantLink = crawlerAux.searchForWord(searchWord);
			boolean relevantLink = crawlerAux.searchGameBodyText();
			if(relevantLink){
				System.out.println(String.format("Link %s é um anúncio de jogo", currentUrl));
			}
			this.pagesToVisit.addAll(crawlerAux.getLinks());

		}
		System.out.println(String.format("%s páginas visitadas.", this.pagesVisited.size()));
	}

}
