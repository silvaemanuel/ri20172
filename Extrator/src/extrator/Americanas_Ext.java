package extrator;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.Jogo;

public class Americanas_Ext {

	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/americanas/americanas5.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://kabum.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Elements name = doc.getElementsByClass("product-name");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByClass("sales-price");
		jg.setPreço(price.text());
		
		//Getting platform
		Element plat = doc.getElementById("351534");
		jg.setPlataforma(plat.text());
		
		//Getting genre
		Elements genre = doc.getElementsByAttributeValue("data-reactid", "288"); //n
		jg.setGenero(genre.text());
		
		//Getting dev
		Elements dev = doc.getElementsByAttributeValue("data-reactid", "303"); //n
		jg.setDesenvolvedor(dev.text());
		
		System.out.println(jg.getNome());
		System.out.println(jg.getPreço());
		System.out.println(jg.getPlataforma());
		System.out.println(jg.getGenero());
		System.out.println(jg.getDesenvolvedor());
	}
	
}
