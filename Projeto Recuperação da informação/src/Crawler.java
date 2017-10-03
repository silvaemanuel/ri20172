import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private static int pageLimit = 1000;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<CrawlerAux.Node> pagesToVisit = new LinkedList<CrawlerAux.Node>();

	public static void main(String[] args) throws MalformedURLException {

		Crawler teste = new Crawler();
		teste.search("https://www.americanas.com.br/");

	}

	private String visitNextUrl(){
		String nextUrl = this.pagesToVisit.remove(0).getLink();
		while(this.pagesVisited.contains(nextUrl)){
			nextUrl = this.pagesToVisit.remove(0).getLink();
		}
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}

	public void search(String url) throws MalformedURLException{
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
				currentUrl = this.visitNextUrl();
				URL aURL = new URL(url);

				//Garante que os links visitados devem conter a URL inicial para serem visitados.
				while(!currentUrl.contains(aURL.getHost())) {
					currentUrl = this.visitNextUrl();
				}
				URL currentURL = new URL(currentUrl);
				if (!(currentURL.getHost() + currentURL.getFile()).equals(aURL.getHost())) {
					newUrl = currentURL.getFile();	
				}
			}
			if(crawlerAux.checkExceptions(newUrl)) {
				crawlerAux.crawl(currentUrl);
				this.pagesToVisit.addAll(crawlerAux.getLinks());
				Collections.sort(this.pagesToVisit, new CrawlerAux.Node.CompareScore());
			}
		}
		System.out.println(String.format("%s páginas visitadas.", this.pagesVisited.size()));
	}

}
