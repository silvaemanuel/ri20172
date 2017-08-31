import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private static int pageLimit = 100;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public static void main(String[] args) {
		Crawler spider = new Crawler();
        spider.search("http://arstechnica.com/", "computer");

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
		while(this.pagesVisited.size() < pageLimit){
			String currentUrl;
			CrawlerAux crawlerAux = new CrawlerAux();
			if(this.pagesToVisit.isEmpty()){
				currentUrl = url;
				this.pagesVisited.add(url);
			}
			else{
				currentUrl = this.visitNextUrl();
			}
			crawlerAux.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
			// SpiderLeg
			boolean success = crawlerAux.searchForWord(searchWord);
			if(success){
				System.out.println(String.format("Palavra %s encontrada em %s", searchWord, currentUrl));
				break;
			}
			this.pagesToVisit.addAll(crawlerAux.getLinks());
		}
		System.out.println(String.format("%s páginas visitadas.", this.pagesVisited.size()));
	}


}
