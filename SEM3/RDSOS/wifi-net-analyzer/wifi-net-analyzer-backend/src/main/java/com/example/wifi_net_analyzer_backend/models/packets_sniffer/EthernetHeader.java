package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthernetHeader{
    private String destinationMac;
    private String sourceMac;
    private int etherType;
    private String etherTypeName;
}
