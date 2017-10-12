package extrator;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.Jogo;

public class Origin_Ext {

	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/origin/origin.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://origin.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Elements name = doc.getElementsByClass("otktitle-page origin-pdp-hero-title origin-pdp-hero-info-block origin-lineclamp");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByAttributeValue("itemprop", "price"); //n
		jg.setPreço(price.text());
		
		//Getting platform
		Elements plat = doc.getElementsContainingOwnText("Plataforma:");
		jg.setPlataforma(plat.text());
		
		//Getting genre
		Elements genre = doc.getElementsContainingOwnText("Gênero:");
		jg.setGenero(genre.text());
		
		//Getting dev
		Elements dev = doc.getElementsContainingOwnText("Desenvolvedora:");
		jg.setDesenvolvedor(dev.text());
		
		System.out.println(jg.getNome());
		System.out.println(jg.getPreço());
		System.out.println(jg.getPlataforma());
		System.out.println(jg.getGenero());
		System.out.println(jg.getDesenvolvedor());
	}

}
