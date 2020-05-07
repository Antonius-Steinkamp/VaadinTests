package de.anst.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import lombok.extern.java.Log;

/**
 * Provide Some Lists as Key-Value-Pair
 * 
 * @author Antonius
 *
 */
@Log
public enum ValueLists {
	;
	
    /**
     * @return {@link List} of System properties.
     */
    public static List<KeyValue<String, String>> getProperties() {
    	final List<KeyValue<String, String>> result = new ArrayList<>();
    	final Properties sysProps = System.getProperties();
    	for (String key: sysProps.stringPropertyNames()) {
    		String value = sysProps.getProperty(key);
    		result.add(new KeyValue<>(key, value));
    	}
    	
    	result.sort(Comparator.comparing(KeyValue<String, String>::getKey));
    	
    	log.info(formatResults(result.size(), " system properties"));
    	
    	return result;
    }

	/**
	 * @return {@link List} of Environment properties.
	 */
	public static List<KeyValue<String, String>> getEnvironmen() {
    	final List<KeyValue<String, String>> result = new ArrayList<>();
    	
    	final Map<String, String> env = System.getenv();
    	
    	for (Map.Entry<String, String> entry: env.entrySet()) {
    		result.add(new KeyValue<>(entry.getKey(), entry.getValue()));
    	}
    	
    	log.info(formatResults(result.size(), " environmen properties"));

    	return result;
    }

	/**
	 * @return {@link List} of {@link Locale}
	 */
	public static List<KeyValue<String, String>> getLocales() {
    	final List<KeyValue<String, String>> result = new ArrayList<>();
    	for (Locale locale: Locale.getAvailableLocales()) {
    		
    		final String key = locale.getDisplayName();
    		final String value = locale.getCountry() + ":" + locale.getDisplayLanguage();
    		
    		final KeyValue<String, String> kv = new KeyValue<>(key, value); 
    		
    		result.add(kv);
    	}

    	log.info(formatResults(result.size(), " locales"));
    	
    	return result;
    }

	private static String formatResults(final int size, final String what) {
		return "return " + size + " " + what;
	}
}
