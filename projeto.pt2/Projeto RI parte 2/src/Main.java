import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Main {

	private static int TAATOrCount = 0;
	private static int TAATOrCountOpt = 0;
	private static ArrayList<String> documentosEncontrados = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		double x [] = {1,2,3,4,5,6,7,8,9,10};
		double y [] = {2,3,1,5,4,7,8,10,6,9};
		//System.out.println(spearman(x, y));
		String sCurrentLine;
		ArrayList<ListaIndexada> newIndex = new ArrayList<>();
		Doc post;
		Reader input = new FileReader("C:\\Users\\Ribeiro\\Downloads\\term.txt");
		Reader input2 = new FileReader("C:\\Users\\Ribeiro\\Downloads\\entrada.txt");
		BufferedReader inputReader = new BufferedReader(input2);
		BufferedReader indexReader = new BufferedReader(input);

		while ((sCurrentLine = indexReader.readLine()) != null) {
			ListaIndexada temp = new ListaIndexada();
			String term[] = sCurrentLine.split("\\\\");
			temp.term = term[0];
			String out = sCurrentLine.substring(sCurrentLine.indexOf("[")+1,sCurrentLine.indexOf("]")); 
			String[] parts = out.split(", ");
			temp.count = parts.length;
			temp.frequency = 0;
			for (int k = 0; k<parts.length; k++) {
				post = new Doc();
				String temp1[] = parts[k].split("/");
				post.id = temp1[0];
				temp.frequency += Integer.parseInt(temp1[1]);
				post.number = Integer.parseInt(temp1[1]);
				if(temp.head == null){
					temp.head = post;
					temp.last = temp.head;           
				}else {
					temp.last.next = post;
					post.previous = temp.last;
					post.next = null;
					temp.last = post;
				}
			}
			newIndex.add(temp); 
		}

		newIndex = sortByCount(newIndex);
		HashMap<String, ListaIndexada> IndexMap = new HashMap<>();
		for (ListaIndexada tIndex : newIndex) {
			IndexMap.put(tIndex.getTerm(), tIndex);
		}
		while ((sCurrentLine = inputReader.readLine()) != null){
			if(!sCurrentLine.equals("")){
				String terms[] = sCurrentLine.split(" ");
				boolean abc = true;

				for(String term :terms){
					if(IndexMap.get(term)!= null){

					}else {
						abc=false;
					}
				}
				if(abc){
					String TAATAnd = termAtATimeAnd(terms, IndexMap);
					String TAATOr = termAtATimeOR(terms, IndexMap);
					System.out.println(TAATAnd);
				}else {
					String resp = "";
					for(String term : terms){
						resp += term + ", ";
					}
					System.out.println("Consulta " + resp);
					System.out.println("Documentos encontrados 0");
				}
			}
		}
		indexReader.close();
		inputReader.close();
		Reader sinput = new FileReader("C:\\Users\\Ribeiro\\Downloads\\term.txt");
		Reader sinput2 = new FileReader("C:\\Users\\Ribeiro\\Downloads\\entrada.txt");
		BufferedReader sinputReader = new BufferedReader(sinput2);
		BufferedReader sindexReader = new BufferedReader(sinput);
		String str [] = new String[documentosEncontrados.size()];
		str = documentosEncontrados.toArray(str);
		tf(sindexReader, sinputReader, str);
	}

	public static void tf(BufferedReader file, BufferedReader query, String[] doc) throws IOException {
		String sCurrentLine;
		Doc post;
		ArrayList<ListaIndexada> newIndex = new ArrayList<>();
		ArrayList<String> termos = new ArrayList<>();
		ArrayList<Lista> newLista = new ArrayList<>();
		Lista l1 = new Lista();
		while ((sCurrentLine = file.readLine()) != null) {
			for(int i = 0; i < doc.length; i++) {
				if (sCurrentLine.contains(doc[i])) {
					ListaIndexada temp = new ListaIndexada();
					String term[] = sCurrentLine.split("\\\\");
					temp.term = term[0];
					String out = sCurrentLine.substring(sCurrentLine.indexOf("[")+1,sCurrentLine.indexOf("]")); 
					String[] parts = out.split(", ");
					temp.count = parts.length;
					temp.frequency = 0;
					if(!termos.contains(temp.term)) {
						termos.add(temp.term);
					}

					for (int k = 0; k<parts.length; k++) {
						post = new Doc();
						String temp1[] = parts[k].split("/");
						post.id = temp1[0];
						if (post.id.equals(doc[i])) {
							temp.frequency += Integer.parseInt(temp1[1]);
							post.number = Integer.parseInt(temp1[1]);
							//l1.insert(new Lista(post.id, temp.term, Double.parseDouble(temp1[1])));
							newLista.add(new Lista(post.id, temp.term, Double.parseDouble(temp1[1])));
							if(temp.head == null){
								temp.head = post;
								temp.last = temp.head;           
							}else {
								temp.last.next = post;
								post.previous = temp.last;
								post.next = null;
								temp.last = post;
							}
						}
					}
					newIndex.add(temp); 
				}
			}
		}
		System.out.println(termos);
		double tabelao[][] = new double[doc.length + 1][termos.size()];
		for(int j = 0; j < termos.size(); j++) {
			for(int i = 0; i < doc.length; i++) {
				tabelao[i][j] = procurarTermo(newLista, doc[i], termos.get(j));
			}
		}
		double idf[] = new double[termos.size()];
		for(int j = 0; j < termos.size(); j++) {
			double sum = 0;
			for(int i = 0; i < doc.length; i++) {
				if(tabelao[i][j] != 0) {
					sum++;
				}
			}
			idf[j] = (Math.log(doc.length/sum) / Math.log(2));
		}
		String sCurrentQuery = query.readLine();
		String[] queryArr;
		if (sCurrentQuery.split(" ") == null) {
			queryArr = new String[0];
			queryArr[0] = sCurrentQuery;
		}else {
			queryArr = sCurrentQuery.split(" ");
		}

		for(int j = 0; j < termos.size(); j++) {
			double freq = 0;
			double freqQuery = 0;
			for(int i = 0; i < doc.length; i++) {
				if (tabelao[i][j] != 0) {
					freq++;
				}
			}
			for(int h = 0; h < queryArr.length; h++) {
				if (termos.get(j).equals(queryArr[h])) {
					freqQuery++;
				}
			}
			if (freq != 0) {
				tabelao[doc.length][j] = freqQuery/freq;
			}else {
				tabelao[doc.length][j] = 0;
			}
		}

		for(int i = 0; i < doc.length + 1; i++) {
			for(int j = 0; j < termos.size(); j++) {
				tabelao[i][j] = tabelao[i][j] * idf[j];
			}
		}

		double idftf[] = new double[termos.size()];
		for(int j = 0; j < termos.size(); j++) {
			double freqQuery = 0;
			double freq = 0;
			for(int h = 0; h < queryArr.length; h++) {
				if (termos.get(j).equals(queryArr[h])) {
					freqQuery++;
				}
			}
			for(int i = 0; i < doc.length; i++) {
				if (tabelao[i][j] != 0) {
					freq++;
				}
			}

			if (freq != 0) {
				idftf[j] = idf[j] * freqQuery/freq;
			}else {
				idftf[j] = 0;
			}
		}


		double finalmegablast[] = new double[doc.length];

		double tamanho[] = new double[doc.length + 1];
		for(int i = 0; i < doc.length + 1; i++) {
			for(int j = 0; j < termos.size(); j++) {
				tamanho[i] += tabelao[i][j]*tabelao[i][j];
			}
			tamanho[i] = Math.sqrt(tamanho[i]);
		}

		for(int i = 0; i < doc.length; i++) {
			for(int j = 0; j < termos.size(); j++) {
				finalmegablast[i] = finalmegablast[i] + tabelao[i][j]*tabelao[doc.length][j]/tamanho[i]*tamanho[doc.length];
				System.out.print(tabelao[i][j] + "*" + tabelao[doc.length][j] + " + ");
			}
			System.out.println();
		}


		System.out.println(spearman(idf, idftf));
	}

	static double procurarTermo(ArrayList<Lista> newLista, String id, String busca) {
		for (int i = 0; i < newLista.size(); i++) {
			if (id.equals(newLista.get(i).docId) && busca.equals(newLista.get(i).termo)) {
				return newLista.get(i).frequencia;
			}
		}
		return 0;
	}


	public static String termAtATimeAnd(String[] terms, HashMap<String, ListaIndexada> indexMap){
		ListaIndexada temp = new ListaIndexada();
		for(int i =0; i<terms.length;i++){
			ListaIndexada addToTemp = indexMap.get(terms[i]);

			if(i==0){
				if(addToTemp != null){
					for(Doc p = addToTemp.head; p != null; p = p.next) {
						temp = addTAATOr(p.id, p.number, temp);
					}
				}
				else {
					System.out.println("term not found");
				}
			}else{
				if(temp.head == null){
					return null;
				}
				else{
					boolean isPresent=false;
					for(Doc p = temp.head; p!=null; p = p.next){
						for(Doc m = addToTemp.head; m != null; m = m.next){
							if(p.id.equals(m.id)){
								isPresent = true;
							}

						}
						if(!isPresent){
							if(p.next != null) {
								p.next.previous = p.previous;
							}else{
								temp.last = p.previous;
								if(temp.last != null) {
									temp.last.next = null;
								}
							}
							if(p.previous != null){
								p.previous.next = p.next;
							}else{
								temp.head = p.next;
								if(temp.head != null){
									temp.head.previous = null;
								}
							}
						}
						isPresent = false;
					}
				}

			}
		}
		int c=0;
		for(Doc p = temp.head; p!=null;p=p.next) {
			c++;
		}
		String TAATAnd = "Consulta ";
		for(String term : terms){
			TAATAnd += term + ", ";
		}
		TAATAnd += sortedByDF_list(temp) + "\n";
		TAATAnd += "Documentos encontrados " + c;

		return TAATAnd;
	}

	public static String termAtATimeOR(String[] terms, HashMap<String, ListaIndexada> indexMap){
		ListaIndexada temp = new ListaIndexada();
		TAATOrCount=0;
		int comp=0;

		for(String term :terms)
		{
			ListaIndexada addToTemp = indexMap.get(term);
			if(addToTemp != null)
			{    for(Doc p = addToTemp.head; p != null; p = p.next)
				temp = addTAATOr(p.id, p.number, temp);
			}
			else
				System.out.println("term not found");
		}
		String results[]= sortedByDF_list(temp).split(", ");

		String last = "FUNCTION: termAtATimeQueryOr ";
		for(String term :terms)
		{
			last += term + ", ";
		}

		last += "\n" + results.length + " documents are found \n";
		for(String result:results){
			documentosEncontrados.add(result);
			last += result + ", ";
		}
		last += "\n"+TAATOrCount + " comparisons are made\n";

		return last;
	}

	public static ListaIndexada addTAATOr(String id,int number,ListaIndexada abc){
		Doc temp = new Doc(id,number,null,null);
		if(abc.head == null){
			abc.head = new Doc();
			abc.head.id = id;
			abc.head.number = number;
			abc.last = abc.head;
		}else{
			if(!ifInList(id, abc))
			{            
				abc.last.next = temp;
				temp.previous = abc.last;
				temp.next = null;
				abc.last = temp;

			}
		}
		return abc;
	}

	public static String sortedByDF_list(ListaIndexada newIndex){

		int size = 0;
		for(Doc p = newIndex.head; p != null; p = p.next) {
			size++;
		}

		boolean flag = true;
		String tempDocID;
		int tempTimes;
		while ( flag ){
			flag= false;
			for(int j=0;  j < size -1;  j++ ){
				Doc p1 = newIndex.head.getAt(newIndex.head, j);
				Doc p2 = newIndex.head.getAt(newIndex.head, j+1);

				if ( (newIndex.head.getAt(newIndex.head, j)).getId() < (newIndex.head.getAt(newIndex.head, j+1)).getId() ){
					tempDocID = p1.id;
					tempTimes = p1.number;
					p1.id = p2.id;
					p1.number = p2.number;
					p2.id = tempDocID;
					p2.number = tempTimes;
					flag = true;
				}
			}
		}
		String buf="";
		for(Doc p = newIndex.head; p != null; p = p.next){
			buf+=p.id+", ";
		}

		return buf;
	}

	public static boolean ifInList(String id,ListaIndexada abc){
		for(Doc p = abc.head; p != null; p = p.next){
			TAATOrCount++;
			TAATOrCountOpt++;
			if(p.id.equals(id))
				return true;
		}
		return false;
	}

	public static ArrayList<ListaIndexada> sortByCount(ArrayList<ListaIndexada> a){
		Collections.sort(a, new Comparator<ListaIndexada>() {
			public int compare(ListaIndexada a1, ListaIndexada a2) {
				if(a1.count > a2.count)
					return -1;
				else if (a1.count < a2.count)
					return +1;
				else
					return 0;
			}});
		return a;
	}

	private static Double spearman(double [] X, double [] Y) {
		if (X == null || Y == null || X.length != Y.length) {
			return null;
		}

		int [] rankX = getRanks(X);
		int [] rankY = getRanks(Y);

		int n = X.length;
		double numerator = 0;
		for (int i = 0; i < n; i++) {
			numerator += Math.pow((rankX[i] - rankY[i]), 2);
		}
		numerator *= 6;
		return 1 - numerator / (n * ((n * n) - 1));
	}

	public static int[] getRanks(double [] array) {
		int n = array.length;

		Par [] par = new Par[n];
		for (int i = 0; i < n; i++) {
			par[i] = new Par(i, array[i]);
		}
		Arrays.sort(par, new ComparadorPar());

		int [] ranks = new int[n];
		int rank = 1;
		for (Par p : par) {
			ranks[p.index] = rank++;
		}
		return ranks;
	}
}
