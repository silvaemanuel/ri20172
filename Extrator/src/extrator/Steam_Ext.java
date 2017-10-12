package extrator;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import base.Jogo;

public class Steam_Ext {

	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/steam/steam3.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://steam.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Elements name = doc.getElementsByClass("apphub_AppName");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByClass("game_purchase_price price");
		jg.setPreço(price.text());
		
		//Getting platform
		Elements plat = doc.getElementsContainingOwnText("Plataforma"); //N/A
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
