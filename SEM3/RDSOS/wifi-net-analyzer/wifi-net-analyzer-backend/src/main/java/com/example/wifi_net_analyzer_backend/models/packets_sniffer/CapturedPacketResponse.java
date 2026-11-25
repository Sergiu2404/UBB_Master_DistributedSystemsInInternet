package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapturedPacketResponse {
    private long id;
    private long timestampMillis;
    private int length;
    private String protocol;
    private EthernetHeader ethernet;
    private IpHeader ip;
    private TcpHeader tcp;
    private String httpPayload;
}