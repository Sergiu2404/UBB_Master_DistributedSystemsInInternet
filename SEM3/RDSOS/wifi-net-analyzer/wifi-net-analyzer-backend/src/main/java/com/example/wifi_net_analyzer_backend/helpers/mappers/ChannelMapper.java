package com.example.wifi_net_analyzer_backend.helpers.mappers;

import com.example.wifi_net_analyzer_backend.domain.Band;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {
    public Band band(int mhz) {
        if (mhz >= 2400 && mhz < 2500) {
            return Band.GHz_2_4;
        }
        if (mhz >= 5150 && mhz < 5925) {
            return Band.GHz_5;
        }
        if (mhz >= 5925 && mhz <= 7125) {
            return Band.GHz_6;
        }

        return null;
    }
    public int channel(int mhz) {
        if (mhz == 2484) {
            return 14;
        }
        if (mhz >= 2412 && mhz <= 2472) {
            return (mhz - 2407) / 5;
        }
        if (mhz >= 5150 && mhz < 5925) {
            return (mhz - 5000) / 5;
        }
        if (mhz >= 5925 && mhz <= 7125) {
            return (mhz - 5950) / 5;
        }

        return 0;
    }
}
