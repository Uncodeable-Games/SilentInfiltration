import javax.swing.JOptionPane;
import java.util.*;

/*public*/class Elem {
	Elem() {
	}

	Elem(Object obj) {
		setObject(obj);
	}

	private Object obj;
	private Elem next;

	public void setObject(Object newObj) {
		obj = newObj;
	}

	public Object getObject() {

		return obj;
	}

	public void setNext(Elem nextElem) {
		next = nextElem;
	}

	public Elem getNext() {
		return next;
	}

	public String toString() {
		return obj.toString();
	}
}

/* public */class Queue2 {

	Queue2() {
	}

	private Elem start, ende;

	public void enQueue(Object newObj) {
		Elem newElem = new Elem(newObj);
		if (start == null)
			start = newElem;
		else
			ende.setNext(newElem);
		ende = newElem;
	}

	public Object deQueue() {
		if (start != null) {
			Elem temp = start;
			start = start.getNext();
			if (start == null)
				ende = null;
			return temp.getObject();
		} else
			return null;
	}

	boolean isEmpty() {
		return (start == null);
	}

	boolean isFull() {
		return false;
	}

	int size() {
		if (start == null)
			return 0;
		int count = 1;
		Elem temp = start;
		while (temp.getNext() != null) {
			count++;
			temp = temp.getNext();
		}
		return count;
	}

	boolean hasString(String s) {
		// int endett = (Integer) ende.getObject();
		for (int i = 0; i < size(); i++) {
			if (start.getObject() != null)
				if (start.getObject().toString().equals(s))
					return true;
		}
		return false;

		// Die .equals() Methode überprüft den tatsächlichen Inhalt des Strings.
		// Der "==" Operator überprüft ob die Referenzen der einzelnen Objekte
		// gleich sind
		// Da ein String gleich sein kann, ohne das die Referenzen gleich sind,
		// benutze ich die .equals() Methode
	}

	public void sort() {
		for (int i = 1; i < size(); i++) {
			for (int j = 0; j < size() - i; j++) {
				Elem current = getAtIndex(j);
				Elem next = getAtIndex(j + 1);
				if (next.toString().compareTo(current.toString()) < 0) {
					if (current == start) {
						current.setNext(next.getNext());
						next.setNext(current);
						start = next;
					} else {
						Elem prev = start;
						while (prev.getNext() != current)
							prev = prev.getNext();
						current.setNext(next.getNext());
						next.setNext(current);
						prev.setNext(next);
						if (next == ende)
							ende = current;
					}
				}
			}
		}

	}

	Elem getAtIndex(int index) {
		Elem temp = start;
		for (int i = 0; i < index; i++)
			temp = temp.getNext();
		return temp;
	}

	public String toString() {
		Elem position = start;
		String str = "";
		while (position != null) {
			str += position.getObject().toString() + "  ";
			position = position.getNext();
		}
		return str;
	}
}

/* public */class Queue3 {

	Queue3() {
	}

	public int start, frei, laenge = 2;
	private boolean leer = true, voll = false;
	private Object feld[] = new Object[laenge];

	public void enQueue(Object newObj) {
		if (!voll) {
			feld[frei] = newObj;
			frei = (frei + 1) % laenge;
			leer = false;
			voll = (start == frei);
			if (voll) {
				voll = false;
				frei = 0;
				laenge = 2 * laenge;
				Object[] tfeld = new Object[laenge];
				int startt = start;
				for (int i = 0; i < feld.length; i++) {
					tfeld[i] = feld[startt];
					if (startt == feld.length - 1) {
						startt = 0;
					} else {
						startt++;
					}
					frei++;
				}
				feld = tfeld;
			}
		}
	}

	public Object deQueue() {
		if (!leer) {
			Object temp = feld[start];
			start = (start + 1) % laenge;
			voll = false;
			leer = (start == frei);

			return temp;
		} else
			return null;
	}

	boolean isEmpty() {
		return leer;
	}

	boolean isFull() {
		return voll;
	}

	int size() {
		if (leer)
			return 0;
		int count = 0;
		for (int i = 0; i < feld.length; i++) {
			if (feld[i] != null)
				count++;
		}
		return count;
	}

	boolean hasString(String s) {
		for (int i = 0; i < feld.length; i++) {
			if (feld[i] != null)
				if (feld[i].toString().equals(s))
					return true;
		}
		return false;

		// Die .equals() Methode überprüft den tatsächlichen Inhalt des Strings.
		// Der "==" Operator überprüft ob die Referenzen der einzelnen Objekte
		// gleich sind
		// Da ein String gleich sein kann, ohne das die Referenzen gleich sind,
		// benutze ich die .equals() Methode
	}

	public String toString() {
		String str = "";
		int i = start;
		if (!leer)
			do {
				str += feld[i] + "  ";
				i = (i + 1) % laenge;
			} while (i != frei);
		return str;
	}

}
