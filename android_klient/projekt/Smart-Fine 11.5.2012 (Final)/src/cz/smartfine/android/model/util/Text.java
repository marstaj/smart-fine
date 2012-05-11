package cz.smartfine.android.model.util;

import java.util.HashMap;

/**
 * Pomocná třída, která poskytuje metody pro práci s textem.
 * 
 * @author Martin Štajner
 * 
 */
public class Text {

	/**
	 * Metoda odstraní diakritiku z textu
	 * 
	 * @param originalString
	 *            Text s diakritikou
	 * @return Text bez diakritiky
	 */
	public static String removeCzechLowerCaseDiacritics(String originalString) {
		HashMap<Character, Character> charReplacementMap = new HashMap<Character, Character>();
		charReplacementMap.put('á', 'a');
		charReplacementMap.put('é', 'e');
		charReplacementMap.put('í', 'i');
		charReplacementMap.put('ó', 'o');
		charReplacementMap.put('ů', 'u');
		charReplacementMap.put('ý', 'y');
		charReplacementMap.put('ú', 'u');
		charReplacementMap.put('ž', 'z');
		charReplacementMap.put('š', 's');
		charReplacementMap.put('č', 'c');
		charReplacementMap.put('ř', 'r');
		charReplacementMap.put('ď', 'd');
		charReplacementMap.put('ť', 't');
		charReplacementMap.put('ň', 'n');

		StringBuilder builder = new StringBuilder();

		for (char currentChar : originalString.toCharArray()) {
			Character replacementChar = charReplacementMap.get(currentChar);
			builder.append(replacementChar != null ? replacementChar : currentChar);
		}

		return builder.toString();
	}

}
