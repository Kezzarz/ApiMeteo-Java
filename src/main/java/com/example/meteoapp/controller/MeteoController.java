package com.example.meteoapp.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@RestController
public class MeteoController {

    @GetMapping("/api/meteo")
    public Map<String, Object> getMeteo() {
        Map<String, Object> result = new HashMap<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

            //Urls des différentes localisations

            String urlBordeaux = "https://api.open-meteo.com/v1/forecast?latitude=44.8404&longitude=-0.5805&hourly=temperature_2m,precipitation_probability";
            String urlParis = "https://api.open-meteo.com/v1/forecast?latitude=48.8534&longitude=2.3488&hourly=temperature_2m,precipitation_probability";
            String urlAnnecy = "https://api.open-meteo.com/v1/forecast?latitude=45.9088&longitude=6.1257&hourly=temperature_2m,precipitation_probability";

            // -- Paris --
            JsonObject hourlyParis = fetchMeteo(client, urlParis);
            JsonArray times = hourlyParis.getAsJsonArray("time");
            JsonArray tempsParis = hourlyParis.getAsJsonArray("temperature_2m");
            JsonArray precipParis = hourlyParis.getAsJsonArray("precipitation_probability");

            // ---Bordeaux---
            JsonObject hourlyBordeaux = fetchMeteo(client, urlBordeaux);
            JsonArray tempsBordeaux = hourlyBordeaux.getAsJsonArray("temperature_2m");
            JsonArray precipBordeaux = hourlyBordeaux.getAsJsonArray("precipitation_probability");

            // ----Annecy----
            JsonObject hourlyAnnecy = fetchMeteo(client, urlAnnecy);
            JsonArray tempsAnnecy = hourlyAnnecy.getAsJsonArray("temperature_2m");
            JsonArray precipAnnecy = hourlyAnnecy.getAsJsonArray("precipitation_probability");

            List<String> labels = new ArrayList<>();
            List<Double> valuesParis = new ArrayList<>();
            List<Double> valuesBordeaux = new ArrayList<>();
            List<Double> valuesAnnecy = new ArrayList<>();
            List<Double> precipitationParis = new ArrayList<>();
            List<Double> precipitationBordeaux = new ArrayList<>();
            List<Double> precipitationAnnecy = new ArrayList<>();

            for (int i=0; i < times.size(); i++) {
                labels.add(times.get(i).getAsString());

                // Pour les températures
                valuesParis.add(tempsParis.get(i).getAsDouble());
                valuesBordeaux.add(tempsBordeaux.get(i).getAsDouble());
                valuesAnnecy.add(tempsAnnecy.get(i).getAsDouble());

                // Pour les risques de précipitations
                precipitationParis.add(precipParis.get(i).getAsDouble());
                precipitationBordeaux.add(precipBordeaux.get(i).getAsDouble());
                precipitationAnnecy.add(precipAnnecy.get(i).getAsDouble());
            }

            // Résultat
            result.put("labels", labels);

            //Données des températures
            result.put("valuesParis", valuesParis);
            result.put("valuesBordeaux", valuesBordeaux);
            result.put("valuesAnnecy", valuesAnnecy);

            // Données de précipitation
            result.put("precipitationParis", precipitationParis);
            result.put("precipitationBordeaux", precipitationBordeaux);
            result.put("precipitationAnnecy", precipitationAnnecy);

        } catch (Exception e){
            e.printStackTrace();
            result.put("error", e.getMessage());
        }
        return result;
    }

    private JsonObject fetchMeteo(HttpClient client, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
        return obj.getAsJsonObject("hourly");
    }
}
