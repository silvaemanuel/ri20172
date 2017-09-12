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
		teste.search("http://store.steampowered.com/");
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
				currentUrl = this.visitNextUrl();

				//Garante que os links visitados devem conter a URL inicial para serem visitados.
				while(!currentUrl.contains(url) && checkExceptions(url, crawlerAux)) {
					currentUrl = this.visitNextUrl();
				}
			}
			crawlerAux.crawl(currentUrl); 
			//boolean relevantLink = crawlerAux.searchForWord(searchWord);
			boolean relevantLink = crawlerAux.searchGameBodyText();
			if(relevantLink){
				System.out.println(String.format("Link %s � um an�ncio de jogo", currentUrl));
			}
			this.pagesToVisit.addAll(crawlerAux.getLinks());

		}
		System.out.println(String.format("%s p�ginas visitadas.", this.pagesVisited.size()));
	}

	public boolean checkExceptions(String currentUrl, CrawlerAux crawlerAux){
		boolean valReturn = true;
		for(String exception: crawlerAux.getExceptions()) {
			String url = currentUrl;
			if(exception.charAt(0) == '*') {
				exception = exception.substring(1, exception.length());
			}
			if (exception.contains("*")) {
				boolean notException = false;
				while(exception.contains("*") && !notException) {
					String aux = exception.substring(0, exception.indexOf("*"));
					exception = exception.substring(exception.indexOf("*") + 1, exception.length());
					if(url.contains(aux)) {
						url = url.substring(url.indexOf(aux) + aux.length(), url.length());
					}else {
						notException = true;
					}
					if (notException) {
						valReturn = false;
					}
				}
			}else {
				if(url.contains(exception)) {
					url = url.substring(url.indexOf(exception) + exception.length(), url.length());
				}else {
					valReturn = true;
				}
			}

		}
		return valReturn;
	}
}
