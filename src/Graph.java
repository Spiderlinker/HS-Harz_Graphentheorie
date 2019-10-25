
public class Graph extends Environment {

	/** Eingelesene Knoten im eingelesenen Graphen */
	private int anzahlKnoten;
	/** Eingelesener Graph als Matrix in int[][] abgespeichert */
	private int[][] graph;
	/** Array mit Anordnungsnummern aller Knoten im Graphen */
	private int[] knotenanordnungNummern;

	/**
	 * Liest einen Graphen �ber den {@link TextStandardInputStream} ein und
	 * speichert diesen in der Klassenvariable {@link Graph#graph} ab.
	 */
	public void readGraph() {
		// In der ersten Zeile ist die Anzahl der Knoten im Graphen angegeben
		anzahlKnoten = stdin.readInt();
		graph = new int[anzahlKnoten][anzahlKnoten];

		// Alle Knoten des Graphens einlesen und in graph-Variable abspeichern
		for (int i = 0; i < anzahlKnoten; i++) {
			for (int j = 0; j < anzahlKnoten; j++) {
				graph[i][j] = stdin.readInt();
			}
		}
	}

	/**
	 * Berechnet f�r alle Knoten im eingelesenen Graphen eine passende
	 * Anordnungsnummer, sofern kein Kreis existiert. Falls dieser Graph einen Kreis
	 * haben sollte, so werden einige Knoten keine Knotenanordnungsnummer erhalten.
	 */
	public void berechneLineareKnotenanordnung() {
		// Neue Berechnung: Anordnungsnummern werde neu initialisiert
		initialisiereKnotenanordnungNummern();

		// Allen Knoten soll nun eine Anordnungsnummer zugewiesen werden:
		// - Anordnungsnummer soll von 1 beginnend verwegeben werden
		// - Die Knoten werden allerdings von hinten nach vorne betrachtet
		berechneLineareKnotenanordnung(1, anzahlKnoten - 1);

		// Bei jedem Durchlauf dieser Schleife wird der Schleifenindex
		// anordnungsnummer als KnotenanordnungNummer vergeben (sofern kein Kreis
		// vorhanden ist)
		// for (int anordnungsnummer = 1; anordnungsnummer <= anzahlKnoten;
		// anordnungsnummer++) {
		// for (int aktuellerKnoten = anzahlKnoten - 1; aktuellerKnoten >= 0;
		// aktuellerKnoten--) {
		// if (knotenanordnungsNummern[aktuellerKnoten] < 0 &&
		// !hatKnotenVorgaenger(aktuellerKnoten)) {
		// loescheKnotenwerte(aktuellerKnoten);
		// knotenanordnungsNummern[aktuellerKnoten] = i ;
		// break;
		// }
		// }
		// }
	}

	/**
	 * Berechnet rekursiv die Anordnungsnummern von allen Knoten im Graphen.
	 * 
	 * @param anordnungsnummer Aktuelle Knotenanordnungsnummer, die dem n�chsten
	 *                         gefundenen Knoten zugewiesen werden soll
	 * @param aktuellerKnoten  Aktuelle Knoten der untersucht werden soll
	 */
	private void berechneLineareKnotenanordnung(int anordnungsnummer, int aktuellerKnoten) {
		// Abbrechen, falls kein g�ltiger Knoten(index) �bergeben wurde
		if (aktuellerKnoten < 0) {
			return;
		}

		// Falls der gegebene Knoten noch keine Anordnungsnummer bekommen hat UND der
		// aktuelle Knoten keinen Vorg�nger hat, so erh�lt dieser Knoten die n�chste
		// Anordnungsnummer
		if (knotenanordnungNummern[aktuellerKnoten] < 0 && !hatKnotenVorgaenger(aktuellerKnoten)) {
			loescheKnotenwerteAusGraph(aktuellerKnoten);
			knotenanordnungNummern[aktuellerKnoten] = anordnungsnummer;

			// Berechnung von Vorne beginnen mit neuer (inkrementierter)
			// Knotenanordnungsnummer
			berechneLineareKnotenanordnung(anordnungsnummer + 1, anzahlKnoten - 1);
		} else {
			// Diesem Knoten wurde schon eine Anordnungsnumemr zugewiesen.
			// Der n�chst niedrigere Knoten soll nun untersucht werden
			berechneLineareKnotenanordnung(anordnungsnummer, aktuellerKnoten - 1);
		}
	}

	/**
	 * Das Array {@link Graph#knotenanordnungNummern} wird initialisiert (Die Gr��e
	 * des Arrays entspricht der Anzahl der vorhandenen Knoten) und mit dem Wert -1
	 * bef�llt. Alle Werte des Arrays werden also auf den Wert -1 gesetzt.
	 */
	private void initialisiereKnotenanordnungNummern() {
		knotenanordnungNummern = new int[anzahlKnoten];
		fillArray(knotenanordnungNummern, -1);
	}

	/**
	 * Der gegebene Knoten wird aus dem Graphen gel�scht. Hierf�r wird die komplette
	 * Zeile des Kontens mit 0en bef�llt. Damit ist dieser Knoten entfernt.
	 * 
	 * @param knoten Knoten dessen Werte (Zeile) aus dem Graphen entfernt werden
	 *               soll
	 */
	private void loescheKnotenwerteAusGraph(int knoten) {
		// Die Zeile des angegebenen Knotens wird komplett auf 0 gesetzt.
		// Beispiel: Knoten 1 (2. Knoten) soll gel�scht werden:
		// Vorher ---------------> Nachher
		// [[0, 1, 1, 0, 1] -->--> [[ 0, 1, 1, 0, 1]
		// _[1, 0, 0, 1, 1] -->--> _[ 0, 0, 0, 0, 0]
		// _[ . . . . . . ]] ----> _[ . . . . . . ]]
		fillArray(graph[knoten], 0);
	}

	/**
	 * Bef�llt das gegebene Array mit dem gegebenen Wert. Alle Werte im Array werden
	 * auf den Wert val gesetzt.
	 * 
	 * @param array Array, das mit dem gegebenen Wert bef�llt werden soll
	 * @param val   Wert zur Bef�llung des Arrays
	 */
	private void fillArray(int[] array, int val) {
		for (int i = 0; i < array.length; i++) {
			array[i] = val;
		}
	}

	/**
	 * Prueft, ob in dem gegebenen Graphen eine Schleife existiert. Eine Schleife
	 * existiert, sobald einem Knoten eine Anordnungsnummer zugewiesen werden
	 * konnte.
	 * 
	 * @return true, falls eine Schleife existiert; andernfalls false
	 */
	private boolean hatGraphEinenKreis() {
		for (int n : knotenanordnungNummern) {
			// Falls ein Wert im knotenanordnungNummern-Array noch den default-Wert -1
			// besitzt, dann besitzt dieser Graph einen Kreis
			if (n < 0) {
				return true;
			}
		}
		// Allen Knoten wurde eine Anordnungsnummer zugewiesen. Es existiert kein Kreis
		return false;
	}

	/**
	 * Liefert einen boolen der angibt, ob der gegebene Knoten einen Vorgaenger
	 * besitzt. Falls also ein beliebiger Knoten im {@link Graph#graph} auf diesen
	 * Knoten(index) verweist, so hat der gegebene Knoten einen Vorgaenger.
	 * 
	 * @param knoten Knoten der geprueft werden soll, ob dieser einen Vorgeanger
	 *               besitzt
	 * @return true, falls der gegebene Knoten einen Vorgaenger besitzt; sonst false
	 */
	private boolean hatKnotenVorgaenger(int knoten) {

		for (int i = 0; i < anzahlKnoten; i++) {
			// Wenn dieser Knoten auf den gegebenen Knoten verweist, dann hat der gegebene
			// Knoten einen Vorgaenger; und zwar den Knoten an der Stelle i
			if (graph[i][knoten] == 1) {
				return true;
			}
		}

		// es wurde kein Vorgaenger gefunden
		return false;
	}

	/**
	 * Gibt die berechneten KnotenanordnungNummern aus. Dies geschieht in folgender
	 * Form:<br>
	 * Knotenindex: .... 1 2 3 4 <br>
	 * --------------------------- (Trennstrich) <br>
	 * Anordnungsnummer: 1 3 2 4
	 */
	public void printLineareKnotenanordnungNummern() {

		// Pr�fen, ob der Graph einen Kreis hat. Falls der Graph einen Kreis haben
		// sollte, so wird statt den KnotenanordnungNummern eine entsprechende Meldung
		// ausgegeben
		if (hatGraphEinenKreis()) {
			System.out.println("Der Graph hat einen Kreis!");
			return;
		}

		// KnotenanordnungNummern ausgeben
		printKnotenindex();
		printTrennstrich();
		printKnotenanordnung();

	}

	private void printKnotenindex() {
		StringBuilder knotenIndexBuilder = new StringBuilder("Knotenindex:\t\t");
		for (int i = 1; i <= anzahlKnoten; i++) {
			knotenIndexBuilder.append(i).append('\t');
		}
		System.out.println(knotenIndexBuilder.toString());
	}

	private void printTrennstrich() {
		StringBuilder trennStrichBuilder = new StringBuilder();
		for (int i = 0; i < anzahlKnoten; i++) {
			trennStrichBuilder.append("------------");
		}
		System.out.println(trennStrichBuilder.toString());
	}

	private void printKnotenanordnung() {
		StringBuilder anordnungsnummerBuilder = new StringBuilder("Anordnungsnummer:\t");
		for (int k : knotenanordnungNummern) {
			anordnungsnummerBuilder.append(k).append('\t');
		}
		System.out.println(anordnungsnummerBuilder.toString());
	}

}
