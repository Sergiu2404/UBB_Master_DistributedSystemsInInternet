package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpHeader {
    private int version;
    private int headerLength;
    private int dscp;
    private int ecn;
    private int totalLength;
    private int identification;
    private boolean dontFragment;
    private boolean moreFragments;
    private int fragmentOffset;
    private int timeToLive;
    private int protocol;
    private String protocolName;
    private int headerChecksum;
    private String sourceIp;
    private String destinationIp;
}
