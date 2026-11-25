package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnifferStatusResponse {
    private boolean capturing;
    private String deviceName;
}
