package dev.joseafmoreira.language;

import java.util.Map;

public class Language {
    private final static int PRIME_NUMBER = 31;
    private final String code;
    private final String name;
    private final String region;
    private Map<String, String> translations;

    public Language(String code, String name, String region) {
        this.code = code;
        this.name = name;
        this.region = region;
        translations = null;
    }

    public String getTranslation(String key) {
        return (translations == null ? null : translations.get(key));
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = PRIME_NUMBER * hashCode + code.hashCode();
        hashCode = PRIME_NUMBER * hashCode + name.hashCode();
        hashCode = PRIME_NUMBER * hashCode + region.hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof Language))
            return false;
        Language otherLanguage = (Language) obj;
        return code.equals(otherLanguage.code) && name.equals(otherLanguage.name)
                && region.equals(otherLanguage.region);
    }

    @Override
    public String toString() {
        return name + " (" + region + ")";
    }
}
