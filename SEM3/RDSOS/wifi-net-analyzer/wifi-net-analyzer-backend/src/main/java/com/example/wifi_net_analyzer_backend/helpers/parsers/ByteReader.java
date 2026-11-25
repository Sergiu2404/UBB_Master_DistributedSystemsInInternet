package com.example.wifi_net_analyzer_backend.helpers.parsers;

public class ByteReader {
    private static final int MAC_ADDRESS_LENGTH = 6;
    private static final int IPV4_ADDRESS_LENGTH = 4;

    private ByteReader() {}

    public static int readUnsignedByte(byte[] bytes, int offset) {
        return bytes[offset] & 0xFF;
    }

    public static int readUnsignedShort(byte[] bytes, int offset) {
        return (readUnsignedByte(bytes, offset) << 8) | readUnsignedByte(bytes, offset + 1);
    }

    public static long readUnsignedInt(byte[] bytes, int offset) {
        return ((long) readUnsignedShort(bytes, offset) << 16) | readUnsignedShort(bytes, offset + 2);
    }

    public static String readMacAddress(byte[] bytes, int offset) {
        StringBuilder macAddress = new StringBuilder();

        for (int byteIndex = 0; byteIndex < MAC_ADDRESS_LENGTH; byteIndex++) {
            if (byteIndex > 0) {
                macAddress.append(':');
            }
            macAddress.append(String.format("%02x", readUnsignedByte(bytes, offset + byteIndex)));
        }

        return macAddress.toString();
    }

    public static String readIpv4Address(byte[] bytes, int offset) {
        StringBuilder ipv4Address = new StringBuilder();

        for (int byteIndex = 0; byteIndex < IPV4_ADDRESS_LENGTH; byteIndex++) {
            if (byteIndex > 0) {
                ipv4Address.append('.');
            }
            ipv4Address.append(readUnsignedByte(bytes, offset + byteIndex));
        }

        return ipv4Address.toString();
    }
}
