package org.gbo.topt;

import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.AsciiTable;

public class TableOptimizer {

//	private static final int personsNumber = 9;
//	private static final int tableCapacity = 3;
//	private static final int tablesNumber = 3;

	private static final int personsNumber = 42;
	private static final int tableCapacity = 6;
	private static final int tablesNumber = 7;

	public static void main(String[] args) {

		String names[] = { "Adamski Yoaan", "Sanches Bérangère", "Schneider Pierre", "Barbosa Laurie", "Job Margot",
				"Weyders Valentine", "Frem Hayat", "El Filali Camilia", "Smili Catherine", "Dubois-Julien Maeva",
				"Lemal Daniel", "Garbi Anne-Sohpie", "Malaj Cléa", "Loukili El Mehdi", "Ze Npoah Rose Christelle",
				"Escure Tatiana", "Pignatelli Gina", "Rock Mélissa", "Sauter Léa", "Gillet Sabrina",
				"Habibova Banovsha", "Stelitano Yoann", "Casanova Samuel", "Csehi Mégane", "Skrijelj Sabrina",
				"Beck Nicolas", "Lett Eva", "Ziri Nabila", "Gravejat Mélanie", "Laria Claire", "Taube Fabienne",
				"Denis Justine", "Mascarell Mélanie", "Delaleux Héléna", "Ndombelé Aicha", "Mangin Alice",
				"Bret Brandon", "Garattoni Charlène", "Scholtus Tiphaine" };

		int contacts[][] = new int[personsNumber][personsNumber];
		for (int person = 0; person < personsNumber; person++) {
			contacts[person][person] = 1;
		}

		boolean tablesUsage[][] = new boolean[tablesNumber][personsNumber];

		List<Integer> tables[] = new List[tablesNumber];

		int turn = 1;

		while (!isEveryBodyContact(contacts)) {

			System.out.println(String.format("**** TURN %s ****", turn));

			showContacts(contacts);

			resetTablesUsage(tablesUsage);
			resetTables(tables);

			for (int table = 0; table < tablesNumber; table++) {

				for (int capacity = 0; capacity < tableCapacity; capacity++) {
					int person = findFirstFreePerson(tablesUsage, table, contacts);

					if (person == -1) {
						person = findAPerson(tables);
					}

					usePerson(tablesUsage, table, person);
					importUsage(tablesUsage, table, contacts, person);
					contactTable(tables[table], contacts, person);

					tables[table].add(person);
				}
			}

			showTables(tables, names);

			turn++;
		}

	}

	private static boolean isEveryBodyContact(int contacts[][]) {

		for (int y = 0; y < personsNumber; y++) {
			for (int x = 0; x < personsNumber; x++) {
				if (contacts[x][y] == 0) {
					return false;
				}
			}
		}

		return true;
	}

	private static void importUsage(boolean tablesUsage[][], int table, int contacts[][], int person) {
		for (int contact = 0; contact < personsNumber; contact++) {
			if (contacts[person][contact] > 0) {
				tablesUsage[table][contact] = true;
			}
		}
	}

	private static void contactTable(List<Integer> table, int contacts[][], int person) {
		for (Integer personTable : table) {
			contacts[person][personTable]++;
			contacts[personTable][person]++;
		}
	}

	private static void usePerson(boolean tablesUsage[][], int table, int person) {
		for (int ctable = table; ctable < tablesNumber; ctable++) {
			tablesUsage[ctable][person] = true;
		}
	}

	private static int findAPerson(List<Integer> tables[]) {
		for (int person = 0; person < personsNumber; person++) {
			if (!isPersonIn(tables, person)) {
				return person;
			}
		}
		return -1;
	}

	private static boolean isPersonIn(List<Integer> tables[], int person) {
		for (int table = 0; table < tablesNumber; table++) {
			if (tables[table].contains(person)) {
				return true;
			}
		}
		return false;
	}

	private static int poids(int contacts[][], int person) {
		int p = Integer.MAX_VALUE;
		for (int cp = 0; cp < personsNumber; cp++) {
			 //p += contacts[person][cp];

			p = Math.min(contacts[person][cp], p);
		}
		return p;
	}

	private static int findFirstFreePerson(boolean tablesUsage[][], int table, int contacts[][]) {

		int min = Integer.MAX_VALUE;
		int selected = -1;

		for (int person = 0; person < personsNumber; person++) {
			if (!tablesUsage[table][person]) {
				int poids = poids(contacts, person);
				if (poids < min) {
					selected = person;
					min = poids;
				}
			}
		}

		return selected;
	}

	private static void resetTables(List<Integer> tables[]) {
		for (int table = 0; table < tablesNumber; table++) {
			tables[table] = new ArrayList<Integer>();
		}
	}

	private static void resetTablesUsage(boolean groupUsage[][]) {
		for (int y = 0; y < tablesNumber; y++) {
			for (int x = 0; x < personsNumber; x++) {
				groupUsage[y][x] = false;
			}
		}
	}

	private static void showContacts(int contacts[][]) {
		AsciiTable at = new AsciiTable();

		System.out.println("Contact matrix :");
		at.addRule();

		for (int y = 0; y < personsNumber; y++) {

			List<String> row = new ArrayList<String>();
			for (int x = 0; x < personsNumber; x++) {
				row.add(Integer.toString(contacts[x][y]));
			}

			at.addRow(row);
			at.addRule();

		}

		System.out.println(at.render(300));
	}

	private static void showTables(List<Integer> tables[], String names[]) {
		AsciiTable at = new AsciiTable();

		System.out.println("Tables : ");
		at.addRule();
		at.addRow("Table", "Persons");
		at.addRule();

		for (int table = 0; table < tablesNumber; table++) {

			at.addRow(table, listToString(tables[table]));
			at.addRule();
		}

		System.out.println(at.render(120));
	}

	private static String listToString(List l) {
		StringBuffer sb = new StringBuffer();

		for (Object o : l) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(o.toString());
		}

		return sb.toString();
	}
}
