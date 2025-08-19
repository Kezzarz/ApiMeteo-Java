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
            String urlParis = "https://api.open-meteo.com/v1/forecast?latitude=48.85&longitude=2.35&hourly=temperature_2m";
            String urlAnnecy = "https://api.open-meteo.com/v1/forecast?latitude=45.8992&longitude=6.1294&hourly=temperature_2m";

            // -- Paris --
            JsonObject hourlyParis = fetchMeteo(client, urlParis);
            JsonArray times = hourlyParis.getAsJsonArray("time");
            JsonArray tempsParis = hourlyParis.getAsJsonArray("temperature_2m");

            // ---Bordeaux---
            JsonObject hourlyBordeaux = fetchMeteo(client, urlBordeaux);
            JsonArray tempsBordeaux = hourlyBordeaux.getAsJsonArray("temperature_2m");

            // ----Annecy----
            JsonObject hourlyAnnecy = fetchMeteo(client, urlAnnecy);
            JsonArray tempsAnnecy = hourlyAnnecy.getAsJsonArray("temperature_2m");

            List<String> labels = new ArrayList<>();
            List<Double> valuesParis = new ArrayList<>();
            List<Double> valuesBordeaux = new ArrayList<>();
            List<Double> valuesAnnecy = new ArrayList<>();

            for (int i=0; i < times.size(); i++) {
                labels.add(times.get(i).getAsString());
                valuesParis.add(tempsParis.get(i).getAsDouble());
                valuesBordeaux.add(tempsBordeaux.get(i).getAsDouble());
                valuesAnnecy.add(tempsAnnecy.get(i).getAsDouble());
            }

            result.put("labels", labels);
            result.put("valuesParis", valuesParis);
            result.put("valuesBordeaux", valuesBordeaux);
            result.put("valuesAnnecy", valuesAnnecy);

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
