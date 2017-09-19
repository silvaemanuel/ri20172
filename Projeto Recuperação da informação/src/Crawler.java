import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private static int pageLimit = 100;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public static void main(String[] args) {

		String exception = "*/busca*filtro*value*";
		String url = "*https://www.americanas.com.br/"; 



		Crawler teste = new Crawler();
		teste.search("https://www.americanas.com.br/");
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
				System.out.println(String.format("Link %s é um anúncio de jogo", currentUrl));
			}
			this.pagesToVisit.addAll(crawlerAux.getLinks());

		}
		System.out.println(String.format("%s páginas visitadas.", this.pagesVisited.size()));
	}

	public boolean checkExceptions(String urlPrefix, CrawlerAux crawlerAux){
		boolean valReturn = true;
		for(String exception: crawlerAux.getExceptions()) {
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
			System.out.println(regex);
			if(urlPrefix.matches(regex)) {
				return false;
			}
		}
		return valReturn;
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
}
