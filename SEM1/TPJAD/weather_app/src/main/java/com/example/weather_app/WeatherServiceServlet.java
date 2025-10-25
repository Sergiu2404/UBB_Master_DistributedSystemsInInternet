package com.example.weather_app;

import com.example.weather_app.models.Coordinates;
import com.example.weather_app.models.Forecast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "weather_service", urlPatterns = {"/weather_service"})
public class WeatherServiceServlet extends HttpServlet {
    private final String API_KEY = "2844d71109c245fbb01dad5afa62b5a8";

    private Coordinates getGeocodingAPICoordinates(String location) throws IOException {
        URL geocodingURL = new URL("https://api.geoapify.com/v1/geocode/search?text=" + location + "&apiKey=" + API_KEY);
        HttpURLConnection geocodingHttpConnection = (HttpURLConnection) geocodingURL.openConnection();
        geocodingHttpConnection.setRequestMethod("GET");
        geocodingHttpConnection.setRequestProperty("Accept", "application/json");
        geocodingHttpConnection.setRequestProperty("Accept", "application/json");

        StringBuilder geocodingResponseText = new StringBuilder();
        int responseCode = geocodingHttpConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(geocodingHttpConnection.getInputStream()))){
                String line;
                while((line = reader.readLine()) != null){
                    geocodingResponseText.append(line);
                }
            }
        } else {
            try(BufferedReader errorReader = new BufferedReader(new InputStreamReader(geocodingHttpConnection.getErrorStream()))){
                StringBuilder errorText = new StringBuilder();
                String errorLine;
                while((errorLine = errorReader.readLine()) != null){
                    errorText.append(errorLine);
                }
                throw new IOException("Geocoding API error: " + errorText);
            }
        }
        geocodingHttpConnection.disconnect();

        JsonObject geocodingResponseJson = JsonParser.parseString(geocodingResponseText.toString()).getAsJsonObject();
        JsonArray features = geocodingResponseJson.getAsJsonArray("features");
        if(features == null || features.isEmpty()){
            throw new IOException("No coordinates fetched for the given location");
        }

        JsonObject propertiesJson = features.get(0).getAsJsonObject().getAsJsonObject("properties");
        double latitude = propertiesJson.get("lat").getAsDouble();
        double longitude = propertiesJson.get("lon").getAsDouble();

        return new Coordinates(latitude, longitude);
    }

    private List<Forecast> getWeatherAPIForecasts(Coordinates coordinates) throws IOException {
        URL weatherURL = new URL(
                "https://api.open-meteo.com/v1/forecast?latitude=" + coordinates.getLatitude() + "&longitude=" + coordinates.getLongitude() +
                        "&daily=temperature_2m_max,temperature_2m_min&timezone=auto"
        );
        HttpURLConnection weatherHttpConnection = (HttpURLConnection) weatherURL.openConnection();
        weatherHttpConnection.setRequestMethod("GET");
        weatherHttpConnection.setRequestProperty("Content-Type", "application/json");
        weatherHttpConnection.setRequestProperty("Accept", "application/json");

        StringBuilder weatherResponseText = new StringBuilder();
        int responseCode = weatherHttpConnection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(weatherHttpConnection.getInputStream()))){
                String line;
                while((line = reader.readLine()) != null){
                    weatherResponseText.append(line);
                }
            }
        } else {
            try(BufferedReader errorReader = new BufferedReader(new InputStreamReader(weatherHttpConnection.getErrorStream()))){
                String errorLine;
                StringBuilder errorText = new StringBuilder();
                while((errorLine = errorReader.readLine()) != null){
                    errorText.append(errorLine);
                }
                throw new IOException("Error fetching weather API data: " + errorText);
            }
        }
        weatherHttpConnection.disconnect();

        JsonObject weatherResponseJson = JsonParser.parseString(weatherResponseText.toString()).getAsJsonObject();
        JsonObject dailyWeather = weatherResponseJson.getAsJsonObject("daily");

        JsonArray datesArray = dailyWeather.getAsJsonArray("time");
        JsonArray tempMaxArray = dailyWeather.getAsJsonArray("temperature_2m_max");
        JsonArray tempMinArray = dailyWeather.getAsJsonArray("temperature_2m_min");

        List<Forecast> forecasts = new ArrayList<>();
        for(int i = 0; i < datesArray.size(); i++){
            forecasts.add(new Forecast(
                    datesArray.get(i).getAsString(),
                    tempMinArray.get(i).getAsDouble(),
                    tempMaxArray.get(i).getAsDouble()
            ));
        }
        return forecasts;
    }
    private List<Forecast> filterForecasts(List<Forecast> forecasts, double userMinTemp, double userMaxTemp){
        List<Forecast> filteredForecasts = new ArrayList<>();
        for(int i = 0; i < forecasts.size(); i++){
            if(userMinTemp <= forecasts.get(i).getMinTemp() && userMaxTemp >= forecasts.get(i).getMaxTemp()){
                filteredForecasts.add(forecasts.get(i));
            }
        }
        return filteredForecasts;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String location = request.getAttribute("location").toString();
            String minParam = request.getAttribute("min_temp").toString();
            String maxParam = request.getAttribute("max_temp").toString();

            if (location == null || minParam == null || maxParam == null) {
                request.setAttribute("error", "Missing required parameters");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            Double minTemp = Double.parseDouble(minParam);
            Double maxTemp = Double.parseDouble(maxParam);
            System.out.println("weather service servlet: " + location + ", " + minTemp + ", " + maxTemp);

            Coordinates locationCoords = this.getGeocodingAPICoordinates(location);
            List<Forecast> forecasts = this.getWeatherAPIForecasts(locationCoords);
            List<Forecast> filteredForecasts = this.filterForecasts(forecasts, minTemp, maxTemp);

            if(filteredForecasts.isEmpty()){
                request.setAttribute("error", "No records found for given data");
                request.getRequestDispatcher("index.jsp").forward(request, response);

            }

//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            Gson gson = new Gson();
//            String jsonResponse = gson.toJson(filteredForecasts);
//            response.getWriter().write(jsonResponse);

            request.setAttribute("location", location);
            request.setAttribute("min_temp", minTemp);
            request.setAttribute("max_temp", maxTemp);
            request.setAttribute("filteredForecasts", filteredForecasts);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
