package extrator;
/*
 * @author efis@cin.ufpe.br
 */

//Imports list
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import base.Jogo;

import java.io.File;
import java.io.IOException;

public class Kabum_Ext {
	
	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/kabum/KaBuM5.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://kabum.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Elements name = doc.getElementsByClass("titulo_det");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByClass("preco_normal");
		jg.setPreço(price.text());
		
		//Getting platform
		Elements plat = doc.select("div:matches(id=\"shout_.+?\")");
		jg.setPlataforma(plat.text());
		
		//Getting genre
		Elements genre = doc.getElementsContainingOwnText("Gênero:");
		jg.setGenero(genre.text());
		
		//Getting dev
		Elements dev = doc.getElementsContainingOwnText("Desenvolvedor:");
		jg.setDesenvolvedor(dev.text());
		
		System.out.println(jg.getNome());
		System.out.println(jg.getPreço());
		System.out.println(jg.getPlataforma());
		System.out.println(jg.getGenero());
		System.out.println(jg.getDesenvolvedor());
	}
	
}
