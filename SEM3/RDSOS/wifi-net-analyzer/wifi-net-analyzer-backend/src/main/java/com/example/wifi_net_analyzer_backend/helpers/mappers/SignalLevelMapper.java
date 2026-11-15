package com.example.wifi_net_analyzer_backend.helpers.mappers;

import com.example.wifi_net_analyzer_backend.domain.SignalLevel;
import org.springframework.stereotype.Component;

@Component
public class SignalLevelMapper {
    public SignalLevel fromRssi(int rssi) {
        if (rssi >= -60) {
            return SignalLevel.GREEN;
        }
        if (rssi >= -75) {
            return SignalLevel.YELLOW;
        }

        return SignalLevel.RED;
    }
}
