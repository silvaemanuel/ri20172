
public class Main {

	public static void main(String[] args) {
		Crawler c1 = new Crawler("http://store.steampowered.com/", "steam");
		Crawler c2 = new Crawler("https://www.nuuvem.com/", "Nuuvem");
		Crawler c3 = new Crawler("https://www.americanas.com.br/", "Americanas");
		Crawler c4 = new Crawler("https://www.livrariacultura.com.br/", "cultura");
		Crawler c5 = new Crawler("https://www.saraiva.com.br/", "Saraiva");
		Crawler c6 = new Crawler("https://www.submarino.com.br/", "Submarino");
		Crawler c7 = new Crawler("https://www.fastshop.com.br/", "Fastfhop");
		Crawler c8 = new Crawler("https://www.kabum.com.br/", "kabum");
		Crawler c9 = new Crawler("https://www.walmart.com.br/", "Walmart");
		//Crawler c10 = new Crawler("https://www.origin.com/", "Origin");
		c1.start();
		c2.start();
		c3.start();
		c4.start();
		c5.start();
		c6.start();
		c7.start();
		c8.start();
		c9.start();
		//c10.start();

	}

}
