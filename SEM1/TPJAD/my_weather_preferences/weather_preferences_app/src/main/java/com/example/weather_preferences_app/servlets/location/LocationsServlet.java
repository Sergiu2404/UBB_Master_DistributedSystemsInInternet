package com.example.weather_preferences_app.servlets.location;

import com.example.weather_preferences_app.data.Coordinates;
import com.example.weather_preferences_app.entities.Location;
import com.example.weather_preferences_app.services.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet(name="LocationsServlet", urlPatterns = {"/locations"})
public class LocationsServlet extends HttpServlet {
    @EJB
    private Service service;

    private Coordinates getGeocodingAPICoordinates(String location) throws IOException {
        Properties props = new Properties();

        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("local.properties")) {
            if (input == null) {
                throw new FileNotFoundException("local.properties not found on the classpath.");
            }
            props.load(input);
        }
        String apiKey = props.getProperty("API_KEY");

        URL url = new URL("https://api.geoapify.com/v1/geocode/search?text=" + URLEncoder.encode(location, StandardCharsets.UTF_8) + "&apiKey=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            BufferedReader errReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errText = new StringBuilder();
            String line;
            while ((line = errReader.readLine()) != null) errText.append(line);
            throw new IOException("Geocoding API error: " + errText);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder respText = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) respText.append(line);
        conn.disconnect();

        JsonObject json = new Gson().fromJson(respText.toString(), JsonObject.class);
        JsonArray features = json.getAsJsonArray("features");
        if (features == null || features.isEmpty()) throw new IOException("No coordinates found for location");

        JsonObject properties = features.get(0).getAsJsonObject().getAsJsonObject("properties");
        double lat = properties.get("lat").getAsDouble();
        double lon = properties.get("lon").getAsDouble();

        return new Coordinates(lat, lon);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String countryIdParam = request.getParameter("countryId");
        String locationIdParam = request.getParameter("locationId");

        try {
            if (locationIdParam != null) {
                Long locationId = Long.parseLong(locationIdParam);
                Location location = this.service.getLocationById(locationId);
                if (location != null) {
                    out.print(new Gson().toJson(location));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found with id: " + locationIdParam);
                }
            } else if (countryIdParam != null) {
                Long countryId = Long.parseLong(countryIdParam);
                List<Location> locations = this.service.getLocationsByCountryId(countryId);
                out.print(new Gson().toJson(locations));
            } else {
                List<Location> locations = service.getAllLocations();
                out.print(new Gson().toJson(locations));
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID parameter: " + countryIdParam + ", locId: " + locationIdParam);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching locations");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String countryIdParam = request.getParameter("countryId");

        if (name == null || name.isBlank() || countryIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Missing required parameters")));
            return;
        }

        try {
            Long countryId = Long.parseLong(countryIdParam);
            Coordinates coords = this.getGeocodingAPICoordinates(name);

            Location location = new Location();
            location.setName(name);
            location.setLatitude(coords.getLatitude());
            location.setLongitude(coords.getLongitude());
            location.setCountryId(countryId);

            boolean success = this.service.saveLocation(location);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(new Gson().toJson(location));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new Gson().toJson(Map.of("error", "Failed to save location")));
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid location or API error: " + e.getMessage())));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid country ID")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(Map.of("error", "Error creating location")));
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String locationIdParam = request.getParameter("locationId");
        String newName = request.getParameter("name");

        if (locationIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Location ID required")));
            return;
        }

        try {
            Long locationId = Long.parseLong(locationIdParam);
            Location location = service.getLocationById(locationId);

            if (location == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(new Gson().toJson(Map.of("error", "Location not found")));
                return;
            }

            // Update name and coordinates if provided
            if (newName != null && !newName.isBlank()) {
                Coordinates coords = getGeocodingAPICoordinates(newName);
                location.setLatitude(coords.getLatitude());
                location.setLongitude(coords.getLongitude());
            }

            boolean found = this.service.existsLocationByName(newName);

            if (!found) {
                service.updateLocation(location);
                out.print(new Gson().toJson(location));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new Gson().toJson(Map.of("error", "Failed to update location — name might already exist")));
            }

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid location or API error: " + e.getMessage())));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid location ID")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(Map.of("error", "Error updating location")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String locationIdParam = request.getParameter("locationId");
        if (locationIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Location ID required");
            return;
        }

        try {
            Long locationId = Long.parseLong(locationIdParam);
            Location location = service.getLocationById(locationId);
            if (location == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found");
                return;
            }

            boolean success = service.removeLocation(location.getId());
            if (success) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete location");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid location ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting location");
        }
    }
}