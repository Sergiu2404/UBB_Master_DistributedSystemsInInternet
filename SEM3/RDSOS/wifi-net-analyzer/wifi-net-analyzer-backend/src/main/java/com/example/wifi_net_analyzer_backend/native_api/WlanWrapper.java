package com.example.wifi_net_analyzer_backend.native_api;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.awt.*;

public interface WlanWrapper extends StdCallLibrary {
    WlanWrapper INSTANCE = Native.load("wlanapi", WlanWrapper.class);

    int WlanOpenHandle(int clientVersion, Pointer reserved, IntByReference negotiatedVersion, PointerByReference clientHandle);

    int WlanCloseHandle(Pointer clientHandle, Pointer reserved);

    int WlanGetNetworkBssList(Pointer clientHandle, Pointer interfaceGuid, Pointer ssid, int bssType, boolean securityEnabled, Pointer reserved, PointerByReference bssList);

    int WlanScan(Pointer clientHandle, Pointer interfaceGuid, Pointer ssid, Pointer ieData, Pointer reserved);

    int WlanEnumInterfaces(Pointer clientHandle, Pointer reserved, PointerByReference interfaceList);

    void WlanFreeMemory(Pointer memory);
}
