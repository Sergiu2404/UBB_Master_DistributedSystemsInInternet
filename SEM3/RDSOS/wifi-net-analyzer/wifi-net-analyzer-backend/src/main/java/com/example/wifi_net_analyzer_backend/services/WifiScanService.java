package com.example.wifi_net_analyzer_backend.services;

import com.example.wifi_net_analyzer_backend.domain.Band;
import com.example.wifi_net_analyzer_backend.helpers.mappers.WifiMapper;
import com.example.wifi_net_analyzer_backend.models.ChannelsResponse;
import com.example.wifi_net_analyzer_backend.models.WifiInterfaceResponse;
import com.example.wifi_net_analyzer_backend.models.WifiNetworkResponse;
import com.example.wifi_net_analyzer_backend.native_api.WlanClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WifiScanService {
    private static final long SCAN_DURATION_SECONDS = 5;
    private static final long PAUSE_BETWEEN_SCANS_SECONDS = 5;

    private volatile List<WifiNetworkResponse> latestNetworks = List.of();

    private final WlanClient wlanClient;
    private final WifiMapper wifiMapper;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public WifiScanService(WlanClient wlanClient, WifiMapper wifiMapper) {
        this.wlanClient = wlanClient;
        this.wifiMapper = wifiMapper;
    }

    @PostConstruct
    void startScanning() {
        this.scheduler.scheduleWithFixedDelay(this::scanNetworks, 0, PAUSE_BETWEEN_SCANS_SECONDS, TimeUnit.SECONDS);
    }

    @PreDestroy
    void stopScanning() {
        this.scheduler.shutdownNow();
    }

    public List<WifiInterfaceResponse> getInterfaces() {
        return wlanClient.listInterfaces().stream()
                .map(wifiMapper::toInterfaceResponse)
                .toList();
    }

    public List<WifiNetworkResponse> getNetworks() {
        return latestNetworks;
    }

    public List<ChannelsResponse> getChannels() {
        return latestNetworks.stream()
                .filter(network -> network.getBand() == Band.GHz_2_4)
                .map(wifiMapper::toChannelResponse)
                .toList();
    }

    private void scanNetworks() {
        try {
            byte[] interfaceGuid = this.wlanClient.listInterfaces().getFirst().getGuid();

            this.wlanClient.scan(interfaceGuid);
            Thread.sleep(TimeUnit.SECONDS.toMillis(SCAN_DURATION_SECONDS));

            this.latestNetworks = this.wlanClient.getBssList(interfaceGuid).stream()
                    .map(this.wifiMapper::toNetworkResponse)
                    .sorted(Comparator.comparingInt(WifiNetworkResponse::getRssi).reversed())
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.warn("WiFi scan failed: {}", e.getMessage());
        }
    }
}
