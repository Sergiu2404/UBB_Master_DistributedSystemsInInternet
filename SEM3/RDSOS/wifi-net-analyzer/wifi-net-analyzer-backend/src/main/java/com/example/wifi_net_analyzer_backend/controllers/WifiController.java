package com.example.wifi_net_analyzer_backend.controllers;

import com.example.wifi_net_analyzer_backend.models.ChannelsResponse;
import com.example.wifi_net_analyzer_backend.models.WifiInterfaceResponse;
import com.example.wifi_net_analyzer_backend.models.WifiNetworkResponse;
import com.example.wifi_net_analyzer_backend.services.WifiScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wifi")
public class WifiController {
    @Autowired
    private WifiScanService wifiScanService;

    @GetMapping("/interfaces")
    public List<WifiInterfaceResponse> getInterfaces() {
        return wifiScanService.getInterfaces();
    }

    @GetMapping("/networks")
    public List<WifiNetworkResponse> getNetworks() {
        return wifiScanService.getNetworks();
    }

    @GetMapping("/channels")
    public List<ChannelsResponse> getChannels() {
        return wifiScanService.getChannels();
    }
}
