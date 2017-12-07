public class ListaIndexada {
	public String term;
	public int count;
	public int frequency;
	public Doc head;
	public Doc last;

	public int getCount(){
		return count;
	}

	public String getTerm(){
		return term;
	}
}
