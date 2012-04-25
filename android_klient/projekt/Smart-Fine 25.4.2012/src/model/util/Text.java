package model.util;

import java.util.HashMap;

/**
 * Pomocná tøída pracující s textem
 * 
 * @author Martin Štajner
 * 
 */
public class Text {

	/**
	 * Odstraní diakritiku z textu
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
		charReplacementMap.put('ú', 'u');
		charReplacementMap.put('ı', 'y');
		charReplacementMap.put('ù', 'u');
		charReplacementMap.put('', 'z');
		charReplacementMap.put('š', 's');
		charReplacementMap.put('è', 'c');
		charReplacementMap.put('ø', 'r');
		charReplacementMap.put('ï', 'd');
		charReplacementMap.put('', 't');
		charReplacementMap.put('ò', 'n');

		StringBuilder builder = new StringBuilder();

		for (char currentChar : originalString.toCharArray()) {
			Character replacementChar = charReplacementMap.get(currentChar);
			builder.append(replacementChar != null ? replacementChar : currentChar);
		}

		return builder.toString();
	}

}
