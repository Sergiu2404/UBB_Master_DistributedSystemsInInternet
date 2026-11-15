package com.example.wifi_net_analyzer_backend.models;

import lombok.Data;

@Data
public class WifiInterfaceResponse {
    private String guid;
    private String description;
    private String state;
}
