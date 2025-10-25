package com.weather_app.weather_api;

import jakarta.json.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name="weatherAPI", urlPatterns = {"/weatherAPI"})
public class WeatherAPIServlet extends HttpServlet {
    private HttpURLConnection httpConnection;

    private String longitude;
    private String latitude;

    private void initHttpConnection() throws IOException {
        URL weatherForecastAPIURL = new URL(
                "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude +
                        "&daily=temperature_2m_max,temperature_2m_min&timezone=auto"
        );

        this.httpConnection = (HttpURLConnection) weatherForecastAPIURL.openConnection();
        this.httpConnection.setRequestMethod("GET");
        this.httpConnection.setRequestProperty("Accept", "application/json");
    }

    private String getWeatherAPIData() throws IOException {
        int responseCode = this.httpConnection.getResponseCode();
        StringBuilder meteoDataResponseStringBuilder = new StringBuilder();

        if(responseCode == HttpURLConnection.HTTP_OK){
            try(BufferedReader meteoReader = new BufferedReader(new InputStreamReader(this.httpConnection.getInputStream()))){
                String line;
                while((line = meteoReader.readLine()) != null){
                    meteoDataResponseStringBuilder.append(line);
                }
            }
        } else {
            meteoDataResponseStringBuilder.append("Error calling meteo data API from weather_api servlet: ").append(responseCode);
        }

        this.httpConnection.disconnect();
        return meteoDataResponseStringBuilder.toString();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        longitude = request.getParameter("lon");
        latitude = request.getParameter("lat");

        if (latitude == null || longitude == null || latitude.isEmpty() || longitude.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing lat/lon parameters");
            return;
        }

        this.initHttpConnection();
        String weatherAPIResponse = this.getWeatherAPIData();

        try (JsonReader jsonReader = Json.createReader(new StringReader(weatherAPIResponse))) {
            JsonObject jsonObject = jsonReader.readObject();

            JsonObject daily = jsonObject.getJsonObject("daily");
            JsonArray dates = daily.getJsonArray("time");
            JsonArray tempMax = daily.getJsonArray("temperature_2m_max");
            JsonArray tempMin = daily.getJsonArray("temperature_2m_min");

            JsonArrayBuilder jsonWeatherData = Json.createArrayBuilder();

            for(int i = 0; i < dates.size(); i++){
                jsonWeatherData.add(Json.createObjectBuilder()
                        .add("date", dates.getString(i))
                        .add("max", tempMax.getJsonNumber(i))
                        .add("min", tempMin.getJsonNumber(i))
                );
            }
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("forecast", jsonWeatherData)
                    .build();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        }
    }
}
