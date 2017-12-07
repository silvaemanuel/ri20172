
public class Lista {
	String docId;
	String termo;
	double frequencia;
	
	public Lista() {}
	
	public Lista(String docId, String termo, double frequencia) {
		this.docId = docId;
		this.termo = termo;
		this.frequencia = frequencia;
	}
	
	Lista next;
	
	void insert(Lista l) {
		if (next == null) {
			next = l;
		}else {
			next.insert(l);
		}
	}
	
	double procurarTermo(String id, String busca) {
		if (id.equals(docId) && busca.equals(termo)) {
			return frequencia;
		}else {
			if (next != null) {
				return next.procurarTermo(id, busca);
			}else {
				return 0;
			}
		}
	}
}
