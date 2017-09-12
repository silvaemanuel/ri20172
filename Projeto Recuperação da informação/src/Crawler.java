import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private static int pageLimit = 100;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public static void main(String[] args) {
		
		String exception = "*/browse?*,*,*,*,*,,,,";
		String url = "https://www.origin.com/browse?dfdf,dfdf,dfdf,dfdfd";

		
		if(exception.charAt(0) == '*') {
			exception = exception.substring(1, exception.length());
		}
		while(exception.contains("*")) {
			String aux = exception.substring(0, exception.indexOf("*"));
			exception = exception.substring(exception.indexOf("*") + 1, exception.length());
			
			if(url.contains(aux)) {
				url = url.substring(url.indexOf(aux) + aux.length(), url.length());
			}else {
				System.out.println("acesa o site");
			}
		}
		System.out.println("não acesa o site");
		//Crawler spider = new Crawler();
		//spider.search("http://store.steampowered.com/", "");
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

	public void search(String url, String searchWord){
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
	
	public boolean checkExceptions(String currentUrl, CrawlerAux crawlerAux){
		
		for(String exception: crawlerAux.getExceptions()) {
			
			if(exception.charAt(0) == '*') {
				exception = exception.substring(1, exception.length());
			}
			while(exception.contains("*")) {
				String aux = exception.substring(0, exception.indexOf("*"));
			}
			
		}
		
		
		return true;
	}
	
	
	


}
