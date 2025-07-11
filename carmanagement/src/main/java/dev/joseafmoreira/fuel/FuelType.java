package dev.joseafmoreira.fuel;

import dev.joseafmoreira.language.LanguageManager;

public enum FuelType {
    REGULAR_UNLEADED_GASOLINE_95,
    PREMIUM_UNLEADED_GASOLINE_95,
    PREMIUM_UNLEADED_GASOLINE_98,
    PREMIUM_PLUS_UNLEADED_GASOLINE_98,
    REGULAR_DIESEL,
    PREMIUM_DIESEL,
    LPG;

    @Override
    public String toString() {
        return LanguageManager.getInstance().getTranslation("fuel." + this.name().toLowerCase());
    }
}
