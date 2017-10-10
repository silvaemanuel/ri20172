package extrator;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.Jogo;

public class Cultura_Ext {
	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/cultura/cultura5.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://cultura.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Elements name = doc.getElementsByTag("title");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByClass("price");
		jg.setPreço(price.text());
		
		//Getting platform
		Elements plat = doc.getElementsByClass("details-column"); //Tratar Strings
		jg.setPlataforma(plat.text());
		
		//Getting genre
		Elements genre = doc.getElementsByClass("details-column"); //n
		
		jg.setGenero(genre.text());
		
		//Getting dev
		Elements dev = doc.getElementsByClass("details-column"); //n
		jg.setDesenvolvedor(dev.text());
		
		System.out.println(jg.getNome());
		System.out.println(jg.getPreço());
		System.out.println(jg.getPlataforma());
		System.out.println(jg.getGenero());
		System.out.println(jg.getDesenvolvedor());
	}
	
}
	

