package com.example.wifi_net_analyzer_backend.native_api;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public interface PcapWrapper extends Library {
    PcapWrapper INSTANCE = loadFromNpcapFolder();

    static PcapWrapper loadFromNpcapFolder() {
        NativeLibrary.addSearchPath("wpcap", System.getenv("SystemRoot") + "\\System32\\Npcap");
        return Native.load("wpcap", PcapWrapper.class);
    }

    Pointer pcap_open_live(String deviceName, int snapshotLength, int promiscuousMode, int readTimeoutMilliseconds, byte[] errorBuffer);

    int pcap_datalink(Pointer pcapHandle);

    int pcap_next_ex(Pointer pcapHandle, PointerByReference packetHeader, PointerByReference packetData);

    void pcap_close(Pointer pcapHandle);
}
