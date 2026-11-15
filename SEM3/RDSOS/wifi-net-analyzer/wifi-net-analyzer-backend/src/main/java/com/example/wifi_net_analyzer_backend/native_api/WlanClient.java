package com.example.wifi_net_analyzer_backend.native_api;

import com.example.wifi_net_analyzer_backend.constants.NetworkConfigConstants;
import com.example.wifi_net_analyzer_backend.models.WifiInterfaceResponse;
import com.example.wifi_net_analyzer_backend.native_api.models.RawBssEntry;
import com.example.wifi_net_analyzer_backend.native_api.models.WlanInterfaceInfo;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WlanClient {
    private static final int INTERFACE_INFO_SIZE = 16 + 256 * 2 + 4; //532
    private static final int BSS_ENTRY_SIZE = 360;
    private static final int LIST_HEADER_SIZE = 8; //2 dwords before the array
    private static final int DOT11_BSS_TYPE_ANY = 3;

    private final WlanWrapper wlan = WlanWrapper.INSTANCE;
    private Pointer clientHandle;

    @PostConstruct
    private void postConstruct() {
        IntByReference negotiatedVersion = new IntByReference();
        PointerByReference handleRef = new PointerByReference();

        this.wlan.WlanOpenHandle(NetworkConfigConstants.WLAN_CLIENT_VERSION, null, negotiatedVersion, handleRef);
        this.clientHandle = handleRef.getValue();
    }

    @PreDestroy
    private void preDestroy() {
        if (clientHandle != null) {
            this.wlan.WlanCloseHandle(clientHandle, null);
            this.clientHandle = null;
        }
    }

    public synchronized List<WlanInterfaceInfo> listInterfaces() {
        PointerByReference listRef = new PointerByReference();
        wlan.WlanEnumInterfaces(clientHandle, null, listRef);
        Pointer list = listRef.getValue();

        try {
            int count = list.getInt(0);
            List<WlanInterfaceInfo> result = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                long off = LIST_HEADER_SIZE + (long) i * INTERFACE_INFO_SIZE;
                result.add(new WlanInterfaceInfo(
                        list.getByteArray(off, 16),
                        list.getWideString(off + 16),
                        list.getInt(off + 16 + 512)));
            }

            return result;
        } finally {
            wlan.WlanFreeMemory(list);
        }
    }

    public synchronized void scan(byte[] interfaceGuid) {
        wlan.WlanScan(clientHandle, toNative(interfaceGuid), null, null, null);
    }

    public synchronized List<RawBssEntry> getBssList(byte[] interfaceGuid) {
        PointerByReference listRef = new PointerByReference();
        wlan.WlanGetNetworkBssList(clientHandle, toNative(interfaceGuid), null, DOT11_BSS_TYPE_ANY, false, null, listRef);
        Pointer list = listRef.getValue();

        try {
            int count = list.getInt(4);
            List<RawBssEntry> result = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                result.add(readBssEntry(list.share(LIST_HEADER_SIZE + (long) i * BSS_ENTRY_SIZE)));
            }

            return result;
        } finally {
            wlan.WlanFreeMemory(list);
        }
    }

    private RawBssEntry readBssEntry(Pointer e) {
        int ssidLength = Math.min(e.getInt(0), 32);
        int ieOffset = e.getInt(352);
        int ieSize = e.getInt(356);
        return new RawBssEntry(
                e.getByteArray(4, ssidLength), //ssid
                e.getByteArray(40, 6), //bssid
                e.getInt(52), //phytype
                e.getInt(56), //rssi
                e.getInt(60), //link quality
                e.getShort(88) & 0xFFFF, //capability
                e.getInt(92), //center frequency khz
                e.getByteArray(ieOffset, ieSize)); //raw Information Elements
    }

    private static Pointer toNative(byte[] guid) {
        Memory memory = new Memory(16);
        memory.write(0, guid, 0, 16);
        return memory;
    }
}
