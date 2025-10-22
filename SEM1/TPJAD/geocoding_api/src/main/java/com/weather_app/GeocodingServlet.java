package com.weather_app;

import com.weather_app.models.Coordinates;
import com.weather_app.models.Forecast;
import jakarta.json.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name="geocoding", urlPatterns = {"/geocoding"})
public class GeocodingServlet extends HttpServlet {
    private final String API_KEY = "2844d71109c245fbb01dad5afa62b5a8";
    private HttpURLConnection httpConnection;

    private void initHttpConnection(URL url) throws IOException {
        this.httpConnection = (HttpURLConnection) url.openConnection();
        this.httpConnection.setRequestMethod("GET");
        this.httpConnection.setRequestProperty("Accept", "application/json");
    }

    private JsonObject getWeatherAPIDataFromServlet(double longitude, double latitude) throws IOException {
        URL url = new URL("http://localhost:8081/weather_app/weatherAPI?lat=" + latitude + "&lon=" + longitude);
        this.initHttpConnection(url);
//        HttpURLConnection weatherForecastServletHttpConnection = (HttpURLConnection) url.openConnection();
//        weatherForecastServletHttpConnection.setRequestMethod("GET");
//        weatherForecastServletHttpConnection.setRequestProperty("Accept", "application/json");

        int responseCode = this.httpConnection.getResponseCode();
        String weatherForecastAPIResponse;

        if(responseCode == 200){
            weatherForecastAPIResponse = new String(httpConnection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            StringBuilder errorText = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream(), StandardCharsets.UTF_8))){
                String errorLine;
                while((errorLine = errorReader.readLine()) != null){
                    errorText.append(errorLine);
                    System.out.println("Error: " + errorLine);
                }
                this.httpConnection.disconnect();
                return Json.createObjectBuilder()
                        .add("status", 400)
                        .add("error", errorText.toString())
                        .build();
            }
        }
        this.httpConnection.disconnect();

        try(JsonReader jsonReader = Json.createReader(new StringReader(weatherForecastAPIResponse))){
            return jsonReader.readObject();
        }
    }

    private Coordinates getGeocodingAPIData(String location) throws IOException {
        URL geocodingURL = new URL("https://api.geoapify.com/v1/geocode/search?text=" + location + "&apiKey=" + API_KEY);
        this.initHttpConnection(geocodingURL);
        String geocodingAPIResponse;

        int responseCode = httpConnection.getResponseCode();
        if(responseCode == 200){
            geocodingAPIResponse = new String(httpConnection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            StringBuilder errorText = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()))){
                String errorLine;
                while((errorLine = errorReader.readLine()) != null){
                    errorText.append(errorLine);
                    System.out.println("Error: " + errorLine);
                }
                this.httpConnection.disconnect();
                throw new IOException("Geocoding API error in geocoding servlet: " + errorText);
            }
        }
        this.httpConnection.disconnect();

        try(JsonReader jsonReader = Json.createReader(new StringReader(geocodingAPIResponse))){
            JsonObject geocodingResponse = jsonReader.readObject();
            JsonArray geocodingFeatures = geocodingResponse.getJsonArray("features");
            if(geocodingFeatures == null || geocodingFeatures.isEmpty()){
                throw new IOException("No coords found for location " + location);
            }

            JsonObject geocodingProperties = geocodingFeatures
                    .getJsonObject(0)
                    .getJsonObject("properties");
            double latitude = geocodingProperties.getJsonNumber("lat").doubleValue();
            double longitude = geocodingProperties.getJsonNumber("lon").doubleValue();

            return new Coordinates(latitude, longitude);
        }
    }

    private List<Forecast> getFilteredForecastsFrom(JsonObject forecastsJsonObject) throws IOException {
        URL filterForecastsServletURL = new URL("http://localhost:8083/filter_forecasts/filter_forecasts");
        HttpURLConnection filterForecastsConnection = (HttpURLConnection) filterForecastsServletURL.openConnection();

        filterForecastsConnection.setRequestMethod("POST");
        filterForecastsConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        filterForecastsConnection.setRequestProperty("Accept", "application/json; utf-8");
        filterForecastsConnection.setDoOutput(true);

        try (OutputStream outputStream = filterForecastsConnection.getOutputStream();
             JsonWriter writer = Json.createWriter(outputStream)) {
            writer.writeObject(forecastsJsonObject);
        }
//        try(OutputStreamWriter writer = new OutputStreamWriter(filterForecastsConnection.getOutputStream(), StandardCharsets.UTF_8)){
//            writer.write(forecastsJsonObject.toString());
//            writer.flush();
//        }

        int responseCode = filterForecastsConnection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Filter servlet returned error: " + responseCode);
        }

        String filteredResponse;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(filterForecastsConnection.getInputStream(), StandardCharsets.UTF_8))) {
            filteredResponse = reader.lines().collect(Collectors.joining());
        }
        filterForecastsConnection.disconnect();

        try (JsonReader jsonReader = Json.createReader(new StringReader(filteredResponse))) {
            JsonObject jsonResponse = jsonReader.readObject();
            JsonArray filteredArray = jsonResponse.getJsonArray("filteredForecasts");

            List<Forecast> filteredForecasts = new ArrayList<>();
            for (JsonValue v : filteredArray) {
                JsonObject obj = v.asJsonObject();
                filteredForecasts.add(new Forecast(
                        obj.getString("date"),
                        obj.getJsonNumber("max").doubleValue(),
                        obj.getJsonNumber("min").doubleValue()
                ));
            }
            return filteredForecasts;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String location = request.getParameter("location");
        if(location == null || location.isEmpty())
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Please enter a location");
            return;
        }

        try{
            Coordinates coordinates = this.getGeocodingAPIData(location);
            double latitude = coordinates.getLatitude();
            double longitude = coordinates.getLongitude();

            JsonObject forecastsObject = this.getWeatherAPIDataFromServlet(longitude, latitude);
            List<Forecast> filteredForecasts = this.getFilteredForecastsFrom(forecastsObject);


        } catch(Exception exception){
            response.getWriter().println("Getting data form APIs went wrong: " + exception.getMessage());
            return;
        }

    }
}
