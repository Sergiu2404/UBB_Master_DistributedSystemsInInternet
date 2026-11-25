package com.example.wifi_net_analyzer_backend.models.packets_sniffer;

import com.example.wifi_net_analyzer_backend.native_api.PcapWrapper;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.util.Optional;

public class PcapSession implements AutoCloseable {
    private static final PcapWrapper PCAP = PcapWrapper.INSTANCE;

    private static final int ERROR_BUFFER_SIZE = 256;
    private static final int NON_PROMISCUOUS_MODE = 0;
    private static final int PACKET_READ_SUCCESSFULLY = 1;
    private static final int READ_TIMEOUT_EXPIRED = 0;

    private static final int HEADER_SECONDS_OFFSET = 0;
    private static final int HEADER_MICROSECONDS_OFFSET = 4;
    private static final int HEADER_CAPTURED_LENGTH_OFFSET = 8;
    private static final int HEADER_ORIGINAL_LENGTH_OFFSET = 12;

    private final Pointer pcapHandle;

    private PcapSession(Pointer pcapHandle) {
        this.pcapHandle = pcapHandle;
    }

    public static PcapSession open(String deviceName, int snapshotLength, int readTimeoutMilliseconds) {
        byte[] errorBuffer = new byte[ERROR_BUFFER_SIZE];
        Pointer pcapHandle = PCAP.pcap_open_live(deviceName, snapshotLength, NON_PROMISCUOUS_MODE, readTimeoutMilliseconds, errorBuffer);

        if (pcapHandle == null) {
            throw new IllegalStateException("Cannot open " + deviceName + ": " + Native.toString(errorBuffer));
        }

        return new PcapSession(pcapHandle);
    }

    public int getLinkType() {
        return PCAP.pcap_datalink(pcapHandle);
    }

    public Optional<RawPacket> readNextPacket() {
        PointerByReference packetHeaderReference = new PointerByReference();
        PointerByReference packetDataReference = new PointerByReference();

        int readResult = PCAP.pcap_next_ex(pcapHandle, packetHeaderReference, packetDataReference);

        if (readResult == READ_TIMEOUT_EXPIRED) {
            return Optional.empty();
        }
        if (readResult != PACKET_READ_SUCCESSFULLY) {
            throw new IllegalStateException("pcap_next_ex failed with code " + readResult);
        }

        Pointer packetHeader = packetHeaderReference.getValue();
        long seconds = Integer.toUnsignedLong(packetHeader.getInt(HEADER_SECONDS_OFFSET));
        long microseconds = Integer.toUnsignedLong(packetHeader.getInt(HEADER_MICROSECONDS_OFFSET));
        int capturedLength = packetHeader.getInt(HEADER_CAPTURED_LENGTH_OFFSET);
        int originalLength = packetHeader.getInt(HEADER_ORIGINAL_LENGTH_OFFSET);

        byte[] packetBytes = packetDataReference.getValue().getByteArray(0, capturedLength);
        long timestampMillis = seconds * 1000 + microseconds / 1000;

        return Optional.of(new RawPacket(timestampMillis, originalLength, packetBytes));
    }

    @Override
    public void close() {
        PCAP.pcap_close(pcapHandle);
    }
}
