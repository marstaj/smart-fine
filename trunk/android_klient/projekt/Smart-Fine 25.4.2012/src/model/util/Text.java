package model.util;

import java.util.HashMap;

/**
 * Pomocn� t��da pracuj�c� s textem
 * 
 * @author Martin �tajner
 * 
 */
public class Text {

	/**
	 * Odstran� diakritiku z textu
	 * 
	 * @param originalString
	 *            Text s diakritikou
	 * @return Text bez diakritiky
	 */
	public static String removeCzechLowerCaseDiacritics(String originalString) {
		HashMap<Character, Character> charReplacementMap = new HashMap<Character, Character>();
		charReplacementMap.put('�', 'a');
		charReplacementMap.put('�', 'e');
		charReplacementMap.put('�', 'i');
		charReplacementMap.put('�', 'o');
		charReplacementMap.put('�', 'u');
		charReplacementMap.put('�', 'y');
		charReplacementMap.put('�', 'u');
		charReplacementMap.put('�', 'z');
		charReplacementMap.put('�', 's');
		charReplacementMap.put('�', 'c');
		charReplacementMap.put('�', 'r');
		charReplacementMap.put('�', 'd');
		charReplacementMap.put('�', 't');
		charReplacementMap.put('�', 'n');

		StringBuilder builder = new StringBuilder();

		for (char currentChar : originalString.toCharArray()) {
			Character replacementChar = charReplacementMap.get(currentChar);
			builder.append(replacementChar != null ? replacementChar : currentChar);
		}

		return builder.toString();
	}

}
