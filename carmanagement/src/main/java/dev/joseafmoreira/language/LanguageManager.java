package dev.joseafmoreira.language;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class LanguageManager {
    private static final String LANGUAGE_FOLDER = "./assets/languages";
    private static final String DEFAULT_LANGUAGE_CODE = "pt_pt";
    private final Logger languageLogger;
    private Map<String, Language> availableLanguages;
    private Language defaultLanguage;
    private Language currentLanguage;
    private static LanguageManager instance;

    @SuppressWarnings("unchecked")
    private LanguageManager() {
        languageLogger = LoggerFactory.getLogger(LanguageManager.class);
        try {
            Gson gson = new Gson();
            availableLanguages = new HashMap<>();
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(LANGUAGE_FOLDER),
                    "*.json")) {
                for (Path path : directoryStream) {
                    try (Reader reader = Files.newBufferedReader(path)) {
                        Map<String, String> data = gson.fromJson(reader, Map.class);
                        String code = path.getFileName().toString().replace(".json", "").trim().toLowerCase();
                        String name = data.remove("language.name");
                        String region = data.remove("language.region");
                        if (code.isEmpty() || name == null || region == null) {
                            languageLogger.warn("Invalid language file {}", path.getFileName());
                            continue;
                        }
                        Language language = new Language(code, name, region);
                        if (availableLanguages.containsKey(code)) {
                            languageLogger.warn("Duplicate language detected {}", language);
                            continue;
                        }
                        if (code.equals(DEFAULT_LANGUAGE_CODE)) {
                            language.setTranslations(data);
                            defaultLanguage = language;
                            languageLogger.info("Loaded default language {}", language);
                        }
                        availableLanguages.put(code, language);
                        languageLogger.info("Added language {}", language);
                    }
                }
            }
            currentLanguage = defaultLanguage;
            languageLogger.info("Set current language to {}", currentLanguage);
            languageLogger.info("Loaded {} languages from {}", availableLanguages.size(), LANGUAGE_FOLDER);
        } catch (IOException e) {
            languageLogger.error("Error loading languages path", e);
            System.exit(0);
        }
    }

    public static LanguageManager getInstance() {
        if (instance == null)
            instance = new LanguageManager();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void selectLanguage(String code) {
        Language language = availableLanguages.get(code);
        if (language == null) {
            languageLogger.warn("Unknown language code {}", code);
            return;
        }
        if (language.getTranslations() == null) {
            Gson gson = new Gson();
            try (Reader reader = Files.newBufferedReader(Paths.get(LANGUAGE_FOLDER + "/" + code + ".json"))) {
                Map<String, String> data = gson.fromJson(reader, Map.class);
                data.remove("language.name");
                data.remove("language.region");
                language.setTranslations(data);
            } catch (IOException e) {
                languageLogger.warn("Error loading language translations {}",
                        language.getName() + " (" + language.getRegion() + ")");
                return;
            }
        }
        currentLanguage = language;
        languageLogger.info("Set current language to {}", currentLanguage);
    }

    public String getTranslation(String key) {
        String translation = currentLanguage.getTranslation(key);
        if (translation != null) {
            languageLogger.debug("Loaded translation {} from current language {}", key, currentLanguage);
            return translation;
        }
        if (!currentLanguage.equals(defaultLanguage)) {
            languageLogger.debug("Unknown translation in current language {}\nFalling back to default language {}",
                    currentLanguage, defaultLanguage);
            translation = defaultLanguage.getTranslation(key);
            if (translation != null) {
                languageLogger.debug("Loaded translation {} from default language {}", key, defaultLanguage);
                return translation;
            }
        }
        languageLogger.warn("Unknown translation key {}", key);
        return "[[ " + key + " ]]";
    }
}
