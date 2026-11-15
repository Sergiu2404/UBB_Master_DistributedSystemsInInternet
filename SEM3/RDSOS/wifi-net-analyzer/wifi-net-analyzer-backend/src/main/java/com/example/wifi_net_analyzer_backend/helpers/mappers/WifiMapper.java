package com.example.wifi_net_analyzer_backend.helpers.mappers;

import com.example.wifi_net_analyzer_backend.models.ChannelsResponse;
import com.example.wifi_net_analyzer_backend.models.WifiInterfaceResponse;
import com.example.wifi_net_analyzer_backend.models.WifiNetworkResponse;
import com.example.wifi_net_analyzer_backend.native_api.models.RawBssEntry;
import com.example.wifi_net_analyzer_backend.native_api.models.WlanInterfaceInfo;
import com.example.wifi_net_analyzer_backend.services.CsvReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class WifiMapper {
    private static final int CHANNEL_HALF_WIDTH = 2;

    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private SignalLevelMapper signalLevelMapper;
    @Autowired
    private CsvReaderService csvReaderService;

    public WifiInterfaceResponse toInterfaceResponse(WlanInterfaceInfo info) {
        WifiInterfaceResponse response = new WifiInterfaceResponse();

        response.setGuid(formatGuid(info.getGuid()));
        response.setDescription(info.getDescription());
        response.setState(interfaceStateName(info.getState()));

        return response;
    }

    public WifiNetworkResponse toNetworkResponse(RawBssEntry entry) {
        WifiNetworkResponse response = new WifiNetworkResponse();

        response.setSsid(decodeSsid(entry.getSsid()));
        response.setBssid(formatMac(entry.getBssid()));
        response.setManufacturer(csvReaderService.getManufacturer(entry.getBssid()));
        response.setPhyType(phyTypeName(entry.getPhyType()));
        response.setRssi(entry.getRssi());
        response.setLinkQuality(entry.getLinkQuality());
        response.setFrequencyKhz(entry.getFrequencyKhz());

        int frequencyMhz = entry.getFrequencyKhz() / 1000;
        response.setBand(channelMapper.band(frequencyMhz));
        response.setChannel(channelMapper.channel(frequencyMhz));
        response.setSignalLevel(signalLevelMapper.fromRssi(entry.getRssi()));

        return response;
    }

    public ChannelsResponse toChannelResponse(WifiNetworkResponse network) {
        ChannelsResponse response = new ChannelsResponse();

        response.setSsid(network.getSsid());
        response.setBssid(network.getBssid());
        response.setChannel(network.getChannel());
        response.setFrom(network.getChannel() - CHANNEL_HALF_WIDTH);
        response.setTo(network.getChannel() + CHANNEL_HALF_WIDTH);
        response.setRssi(network.getRssi());

        return response;
    }

    String formatGuid(byte[] g) {
        return String.format("%02x%02x%02x%02x-%02x%02x-%02x%02x-%02x%02x-%02x%02x%02x%02x%02x%02x",
                g[3], g[2], g[1], g[0],
                g[5], g[4],
                g[7], g[6],
                g[8], g[9],
                g[10], g[11], g[12], g[13], g[14], g[15]);
    }

    String formatMac(byte[] mac) {
        StringBuilder sb = new StringBuilder(17);

        for (int i = 0; i < mac.length; i++) {
            if (i > 0) sb.append(':');
            sb.append(String.format("%02x", mac[i] & 0xFF));
        }

        return sb.toString();
    }

    String decodeSsid(byte[] ssid) {
        return new String(ssid, StandardCharsets.UTF_8);
    }

    //WLAN_INTERFACE_STATE enum
    String interfaceStateName(int state) {
        return switch (state) {
            case 0 -> "not_ready";
            case 1 -> "connected";
            case 2 -> "ad_hoc_network_formed";
            case 3 -> "disconnecting";
            case 4 -> "disconnected";
            case 5 -> "associating";
            case 6 -> "discovering";
            case 7 -> "authenticating";
            default -> "unknown (" + state + ")";
        };
    }

    //DOT11_PHY_TYPE enum
    String phyTypeName(int phyType) {
        return switch (phyType) {
            case 1 -> "FHSS";
            case 2 -> "DSSS";
            case 3 -> "IR baseband";
            case 4 -> "802.11a";
            case 5 -> "802.11b";
            case 6 -> "802.11g";
            case 7 -> "802.11n";
            case 8 -> "802.11ac";
            case 9 -> "802.11ad";
            case 10 -> "802.11ax";
            case 11 -> "802.11be";
            default -> "unknown (" + phyType + ")";
        };
    }
}
