import java.util.Comparator;

class ComparadorPar implements Comparator<Par> {
	double epsilon = 0.0001;
	@Override
	public int compare(Par p1, Par p2) {
		if (Math.abs(p1.value - p2.value) < epsilon) {
			return 0;
		} else if (p1.value < p2.value) {
			return -1;
		} else {
			return 1;
		}
	}
}
