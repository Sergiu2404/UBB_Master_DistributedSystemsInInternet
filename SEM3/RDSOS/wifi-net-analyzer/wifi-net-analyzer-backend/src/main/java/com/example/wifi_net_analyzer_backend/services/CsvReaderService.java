package com.example.wifi_net_analyzer_backend.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvReaderService {
    private static final String OUI_FILE = "classpath:oui.csv";
    private static final int ASSIGNMENT_COLUMN = 1;
    private static final int ORGANIZATION_NAME_COLUMN = 2;
    private static final int LOCALLY_ADMINISTERED_BIT = 0x02;
    private static final String LOCALLY_ADMINISTERED = "Locally administered";
    private static final String UNKNOWN = "Unknown";

    @Autowired
    private ResourceLoader resourceLoader;

    private final Map<String, String> manufacturersByOui = new HashMap<>();

    @PostConstruct
    public void readCsv() throws IOException {
        Resource resource = resourceLoader.getResource(OUI_FILE);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                List<String> columns = splitCsvLine(line);

                if (columns.size() > ORGANIZATION_NAME_COLUMN) {
                    manufacturersByOui.put(
                            columns.get(ASSIGNMENT_COLUMN).toUpperCase(),
                            columns.get(ORGANIZATION_NAME_COLUMN).trim()
                    );
                }
            }
        }
    }

    public String getManufacturer(byte[] mac) {
        if ((mac[0] & LOCALLY_ADMINISTERED_BIT) != 0) {
            return LOCALLY_ADMINISTERED;
        }

        String oui = String.format("%02X%02X%02X", mac[0] & 0xFF, mac[1] & 0xFF, mac[2] & 0xFF);

        return manufacturersByOui.getOrDefault(oui, UNKNOWN);
    }

    private List<String> splitCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                columns.add(current.toString()); // coma is delimiter outisde quotes
                current.setLength(0);
            } else {
                current.append(c); // add coma if in quotes
            }
        }

        columns.add(current.toString());

        return columns;
    }
}
