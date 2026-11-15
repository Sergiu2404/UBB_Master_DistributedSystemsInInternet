package com.example.wifi_net_analyzer_backend.models;

import lombok.Data;

@Data
public class ChannelsResponse {
    private String ssid;
    private String bssid;
    private int channel;
    private int from;
    private int to;
    private int rssi;
}
