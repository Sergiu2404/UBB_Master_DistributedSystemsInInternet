package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawPacket {
    private long timestampMillis;
    private int originalLength;
    private byte[] data;
}
