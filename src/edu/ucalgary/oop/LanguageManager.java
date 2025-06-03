package edu.ucalgary.oop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * LanguageManager loads translation strings from simple XML files
 * in the data directory, e.g. en-CA.xml or fr-CA.xml.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class LanguageManager {

    private String currentLocaleCode;
    private Map<String, String> translations;

    /**
     * Constructs a LanguageManager, defaulting to locale code "en-CA"
     * with an empty translations map.
     */
    public LanguageManager() {
        this.currentLocaleCode = "en-CA";
        this.translations = new HashMap<>();
    }

    /**
     * Attempts to load a locale file from data/<localeCode>.xml
     * using a naive parser. If the file is invalid or missing,
     * falls back to en-CA.
     *
     * @param localeCode must match [a-z]{2}-[A-Z]{2}, e.g. en-CA
     */
    public void loadLocale(String localeCode) {
        if (localeCode == null || !localeCode.matches("[a-z]{2}-[A-Z]{2}")) {
            System.out.println("");
            System.out.println("Invalid language code. Defaulting to en-CA.");
            loadFallbackLocale();
            return;
        }
        File file = new File("data/" + localeCode + ".xml");
        if (!file.exists()) {
            loadFallbackLocale();
            return;
        }
        try {
            parseSimpleXmlFile(file);
            this.currentLocaleCode = localeCode;
        } catch (Exception e) {
            System.out.println("Error parsing " + localeCode + ".xml => " + e.getMessage()
                    + " => defaulting to en-CA.");
            loadFallbackLocale();
        }
    }

    /**
     * Retrieves a string for the given key.
     *
     * @param key the translation key
     * @return the translated string, or null if not found
     */
    public String getString(String key) {
        return translations.get(key);
    }

    /**
     * Gets the current locale code, e.g. "en-CA".
     *
     * @return the current locale code
     */
    public String getCurrentLocaleCode() {
        return currentLocaleCode;
    }

    /**
     * Loads en-CA.xml as a fallback if the desired file fails or is missing.
     */
    private void loadFallbackLocale() {
        File fallback = new File("data/en-CA.xml");
        if (!fallback.exists()) {
            System.out.println("CRITICAL: cannot find data/en-CA.xml. Proceeding with no translations...");
            this.translations.clear();
            return;
        }
        try {
            parseSimpleXmlFile(fallback);
            this.currentLocaleCode = "en-CA";
        } catch (Exception e) {
            System.out.println("Fallback parse error => no translations loaded: " + e.getMessage());
            this.translations.clear();
        }
    }

    /**
     * @param file the XML file to parse
     * @throws IOException if reading fails
     */
    private void parseSimpleXmlFile(File file) throws IOException {
        this.translations.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inTranslation = false;
            String currentKey = null;
            String currentValue = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("<translation>")) {
                    inTranslation = true;
                    currentKey = null;
                    currentValue = null;
                }
                else if (line.startsWith("</translation>")) {
                    if (inTranslation && currentKey != null && currentValue != null) {
                        translations.put(currentKey, currentValue);
                    }
                    inTranslation = false;
                }
                else if (inTranslation) {
                    if (line.startsWith("<key>") && line.contains("</key>")) {
                        String extractedKey = extractTagValue(line, "key");
                        if (extractedKey != null && !extractedKey.isEmpty()) {
                            currentKey = extractedKey;
                        }
                    } else if (line.startsWith("<value>") && line.contains("</value>")) {
                        String extractedVal = extractTagValue(line, "value");
                        if (extractedVal != null && !extractedVal.isEmpty()) {
                            currentValue = extractedVal;
                        }
                    }
                }
            }
        }
    }

    /**
     * @param line the line containing the tag
     * @param tagName the tag name (e.g. "key", "value")
     * @return the extracted text, or null if not found
     */
    private String extractTagValue(String line, String tagName) {
        String open = "<" + tagName + ">";
        String close = "</" + tagName + ">";

        int start = line.indexOf(open);
        int end   = line.indexOf(close);
        if (start < 0 || end < 0) {
            return null;
        }
        int contentStart = start + open.length();
        return line.substring(contentStart, end).trim();
    }
}
