package extrator;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.Jogo;

public class FastShop_Ext {
	
	public static void main(String[] args) throws IOException {
		//Getting HTML from file
		File input = new File("/home/emanuel/htmls/fastshop/fastshop5.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://fastshop.com/");	
		
		//Initializing database
		Jogo jg = new Jogo();
		
		//Getting name
		Element name = doc.getElementById("newTitleBarClose");
		jg.setNome(name.text());
		
		//Getting price
		Elements price = doc.getElementsByAttributeValue("itemprop", "price"); //não encontrado
		jg.setPreço(price.text());
		
		//Getting platform
		Elements plat = doc.getElementsContainingOwnText("Plataforma:"); //Tratar strings
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
