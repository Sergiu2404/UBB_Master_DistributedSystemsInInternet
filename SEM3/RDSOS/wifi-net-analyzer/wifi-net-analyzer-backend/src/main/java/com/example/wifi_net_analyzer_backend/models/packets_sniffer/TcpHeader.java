package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcpHeader {
    private int sourcePort;
    private int destinationPort;
    private long sequenceNumber;
    private long acknowledgmentNumber;
    private int headerLength;
    private List<String> flags;
    private int windowSize;
    private int checksum;
    private int urgentPointer;
}
