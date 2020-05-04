package de.blafoo.bingo;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BingoModel {
	
	public static final String BESPRECHUNGS_DATEN = "Besprechungen";
	public static final String STAEDTE_DATEN = "Städte";
	
	private static final Map<String, List<String>> DATA = new HashMap<>();
	
	static {
		// https://de.wikipedia.org/wiki/Liste_der_gr%C3%B6%C3%9Ften_deutschen_St%C3%A4dte#Die_gr%C3%B6%C3%9Ften_deutschen_St%C3%A4dte_2015
		DATA.put(STAEDTE_DATEN, Arrays.asList(
				"Berlin", "Hamburg", "München", "Köln", 
				"Frankfurt", "Stuttgart", "Düsseldorf", "Dortmund",
				"Essen", "Leipzig", "Bremen", "Dresden", 
				"Hannover", "Nürnberg", "Duisburg", "Bochum"));
		
		// https://www.besprechungsbingo.de/
		DATA.put(BESPRECHUNGS_DATEN, Arrays.asList(
				"Synergie", "Sich schlau machen", "Wertschöpfung", "Ball zuspielen", 
				"Global Player", "Kommunizieren", "Kunden orientiert", "Target",
				"Problematik", "Proaktiv", "Visionen", "Fokussieren", 
				"Zielführend", "Bilateral", "Hut aufhaben", "Wertschätzend"));
	}
	
	public static List<String> getData(String key) {
		return DATA.get(key);
	}
	
}
