package com.filter_forecasts.filter_forecasts;

import com.filter_forecasts.filter_forecasts.models.Forecast;
import jakarta.json.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="filter_forecasts", urlPatterns = {"/filter_forecasts"})
public class FilterForecastsServlet extends HttpServlet {
    private List<Forecast> filterForecasts(List<Forecast> forecasts, double minInputTemp, double maxInputTemp){
        List<Forecast> filteredForecasts = new ArrayList<>();
        for(int i = 0; i < forecasts.size(); i++){
            if(minInputTemp < forecasts.get(i).getMinTemp() && maxInputTemp > forecasts.get(i).getMaxTemp()){
                filteredForecasts.add(forecasts.get(i));
            }
        }

        return filteredForecasts;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonResponse;
        try(
            BufferedReader requestReader = request.getReader();
            JsonReader jsonReader = Json.createReader(requestReader);
            ) {
            JsonObject jsonRequest = jsonReader.readObject();
            System.out.println("Json: " + jsonRequest);

            JsonArray jsonForecasts = jsonRequest.getJsonArray("forecast");
            double minInputTemp = Double.NEGATIVE_INFINITY;
            double maxInputTemp = Double.POSITIVE_INFINITY;
            if (jsonRequest.containsKey("userMinTemp") && !jsonRequest.isNull("userMinTemp")) {
                String minStr = jsonRequest.getString("userMinTemp", "").trim();
                if (!minStr.isEmpty()) {
                    minInputTemp = Double.parseDouble(minStr);
                }
            }

            if (jsonRequest.containsKey("userMaxTemp") && !jsonRequest.isNull("userMaxTemp")) {
                String maxStr = jsonRequest.getString("userMaxTemp", "").trim();
                if (!maxStr.isEmpty()) {
                    maxInputTemp = Double.parseDouble(maxStr);
                }
            }
            List<Forecast> forecasts = new ArrayList<>();

            for (int i = 0; i < jsonForecasts.size(); i++) {
                JsonObject forecast = jsonForecasts.get(i).asJsonObject();
                String date = forecast.getString("date");
                double max = forecast.getJsonNumber("max").doubleValue();
                double min = forecast.getJsonNumber("min").doubleValue();
                forecasts.add(new Forecast(
                        date, min, max
                ));
            }
            List<Forecast> filteredForecasts = this.filterForecasts(forecasts, minInputTemp, maxInputTemp);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (int i = 0; i < filteredForecasts.size(); i++){
                JsonObject forecastJson = Json.createObjectBuilder()
                        .add("date", filteredForecasts.get(i).getDate())
                        .add("min", filteredForecasts.get(i).getMinTemp())
                        .add("max", filteredForecasts.get(i).getMaxTemp())
                        .build();
                jsonArrayBuilder.add(forecastJson);
            }
            jsonResponse = Json.createObjectBuilder()
                    .add("forecast", jsonArrayBuilder)
                    .build();
        } catch(Exception exception){
            System.out.println("Error in filter servlet: " + exception.getMessage());
            jsonResponse = Json.createObjectBuilder()
                    .add("status", 400)
                    .add("error", exception.getMessage())
                    .build();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try(JsonWriter writer = Json.createWriter(response.getWriter())){
            writer.writeObject(jsonResponse);
        }
    }
}
