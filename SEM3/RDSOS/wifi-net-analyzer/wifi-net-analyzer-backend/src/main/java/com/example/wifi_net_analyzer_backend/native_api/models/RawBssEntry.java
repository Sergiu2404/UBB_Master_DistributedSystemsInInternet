package com.example.wifi_net_analyzer_backend.native_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RawBssEntry {
    private byte[] ssid;
    private byte[] bssid;
    private int phyType;
    private int rssi;
    private int linkQuality;
    private int capability;
    private int frequencyKhz;
    private byte[] informationElements;
}
