import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private static int pageLimit = 1000;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<CrawlerAux.Node> pagesToVisit = new LinkedList<CrawlerAux.Node>();

	public static void main(String[] args) {

		Crawler teste = new Crawler();
		teste.search("https://store.steampowered.com");
		//		CrawlerAux aux = new CrawlerAux();
		//		aux.getExceptions("https://www.origin.com");

	}

	private String visitNextUrl(){
		String nextUrl = this.pagesToVisit.remove(0).getLink();
		while(this.pagesVisited.contains(nextUrl)){
			nextUrl = this.pagesToVisit.remove(0).getLink();
		}
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}

	public void search(String url){
		String newUrl = "";
		CrawlerAux crawlerAux = new CrawlerAux();
		crawlerAux.createExceptions(url);
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

				//Garante que os links visitados devem conter a URL inicial para serem visitados.
				while(!currentUrl.contains(url.subSequence(7, url.length()))) {
					currentUrl = this.visitNextUrl();
				}
				if (!currentUrl.substring(currentUrl.indexOf("://"), currentUrl.length()).equals(url.substring(url.indexOf("://"), url.length()))) {
					newUrl = currentUrl.substring(url.length() + 1, currentUrl.length());	
				}
			}
			if(crawlerAux.checkExceptions(newUrl)) {
				crawlerAux.crawl(currentUrl); 
				boolean relevantLink = crawlerAux.searchGameBodyText();
				if(relevantLink){
					System.out.println(String.format("Link %s � um an�ncio de jogo", currentUrl));
				}
				this.pagesToVisit.addAll(crawlerAux.getLinks());
			}
		}
		System.out.println(String.format("%s p�ginas visitadas.", this.pagesVisited.size()));
	}

}
