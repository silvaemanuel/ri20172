package extrator;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import base.Jogo;

public class Submarino {

	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/submarino.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://submarino.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Elements name = doc.getElementsByClass("product-name");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByClass("sales-price");
		jg.setPreço(price.text());
		
		//Getting platform
		Elements plat = doc.getElementsByClass("table table-striped"); //Tratar String
		jg.setPlataforma(plat.text());
		
		//Getting genre
		Elements genre = doc.getElementsContainingOwnText("Gênero:");
		jg.setGenero(genre.text());
		
		//Getting dev
		Elements dev = doc.getElementsContainingOwnText("Desenvovedor");
		jg.setDesenvolvedor(dev.text());
		
		System.out.println(jg.getNome());
		System.out.println(jg.getPreço());
		System.out.println(jg.getPlataforma());
		System.out.println(jg.getGenero());
		System.out.println(jg.getDesenvolvedor());
	}
}
