package com.example.wifi_net_analyzer_backend.native_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WlanInterfaceInfo {
    private byte[] guid;
    private String description;
    private int state;
}
