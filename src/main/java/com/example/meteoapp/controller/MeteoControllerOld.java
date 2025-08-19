package com.example.meteoapp.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class MeteoControllerOld {

    @GetMapping("/meteo")
    public String getMeteo() {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=48.85&longitude=2.35&hourly=temperature_2m";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();

                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                String timezone = obj.get("timezone").getAsString();
                var hourly = obj.getAsJsonObject("hourly");
                var temps = hourly.getAsJsonArray("temperature_2m");

                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                sb.append("<h1>Météo Bordeaux 🌤️</h1>");
                sb.append("<p>Timezone : ").append(timezone).append("</p>");
                sb.append("<h2>Températures horaires :</h2>");
                sb.append("<ul>");
                for (int i = 0; i < 5; i++) {
                    sb.append("<li>").append(temps.get(i).getAsDouble()).append(" °C</li>");
                }
                sb.append("</ul>");
                sb.append("</body></html>");

                return sb.toString();
            } else {
                return "❌ Erreur HTTP : " + response.statusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erreur : " + e.getMessage();
        }
    }
}
