package com.example.weather_preferences_app.servlets;

import com.example.weather_preferences_app.data.Coordinates;
import com.example.weather_preferences_app.data.Forecast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ForecastsServlet", urlPatterns = {"/forecasts"})
public class ForecastsServlet extends HttpServlet {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String latParam = request.getParameter("lat");
        String lonParam = request.getParameter("lon");
        String minParam = request.getParameter("minTemp");
        String maxParam = request.getParameter("maxTemp");

        if (latParam == null || lonParam == null || minParam == null || maxParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        double lat = Double.parseDouble(latParam);
        double lon = Double.parseDouble(lonParam);
        double min = Double.parseDouble(minParam);
        double max = Double.parseDouble(maxParam);

        List<Forecast> allForecasts = this.getWeatherAPIForecasts(new Coordinates(lat, lon));
        List<Forecast> filtered = this.filterForecasts(allForecasts, min, max);

        out.print(new Gson().toJson(filtered));
    }
}
