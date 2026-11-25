package com.example.wifi_net_analyzer_backend.constants;

public class PacketSnifferConstants {
    public static final int LINK_TYPE_ETHERNET = 1;

    public static final int ETHERNET_HEADER_LENGTH = 14;
    public static final int IPV4_MIN_HEADER_LENGTH = 20;
    public static final int TCP_MIN_HEADER_LENGTH = 20;

    public static final int ETHER_TYPE_IPV4 = 0x0800;
    public static final int ETHER_TYPE_ARP = 0x0806;
    public static final int ETHER_TYPE_IPV6 = 0x86DD;
    public static final int ETHER_TYPE_EAPOL = 0x888E;

    public static final int IP_PROTOCOL_ICMP = 1;
    public static final int IP_PROTOCOL_TCP = 6;
    public static final int IP_PROTOCOL_UDP = 17;

    public static final int HTTP_PORT = 80;
}
