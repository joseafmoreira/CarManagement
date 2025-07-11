package dev.joseafmoreira.fuel;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuelManager {
    private static final String FUEL_WEBSITE = "https://www.maisgasolina.com/";
    private final Logger fuelLogger;
    private Map<FuelType, Fuel> fuels;
    private static FuelManager instance;

    private FuelManager() {
        fuelLogger = LoggerFactory.getLogger(FuelManager.class);
        try {
            Document document = Jsoup.connect(FUEL_WEBSITE).get();
            Element homeAverage = document.getElementById("homeAverage");
            if (homeAverage == null)
                throw new IOException("Error obtaining homeAverage div");
            Elements values = homeAverage.select(".homePricesValue");
            FuelType[] fuelsType = FuelType.values();
            fuels = new EnumMap<>(FuelType.class);
            for (int i = 0; i < fuelsType.length; i++) {
                String priceText = values.get(i).ownText().trim().replace("€", "").replace(",", ".");
                try {
                    double price = Double.parseDouble(priceText);
                    Fuel fuel = new Fuel(fuelsType[i], price);
                    fuels.put(fuelsType[i], fuel);
                    fuelLogger.info("Fuel {} initialized with price {}€", fuelsType[i], price);
                } catch (NumberFormatException e) {
                    throw new IOException("Error parsing fuel price");
                }
            }
        } catch (IOException e) {
            fuelLogger.error("Error loading fuel prices page", e);
            System.exit(0);
        }
    }

    public static FuelManager getInstance() {
        if (instance == null)
            instance = new FuelManager();
        return instance;
    }
}
