package com.example.wifi_net_analyzer_backend.models;

import com.example.wifi_net_analyzer_backend.domain.Band;
import com.example.wifi_net_analyzer_backend.domain.SignalLevel;
import lombok.Data;

@Data
public class WifiNetworkResponse {
    private String ssid;
    private String bssid;
    private String manufacturer;
    private String phyType;
    private int rssi;
    private int linkQuality;
    private SignalLevel signalLevel;
    private int frequencyKhz;
    private Band band;
    private int channel;
}
