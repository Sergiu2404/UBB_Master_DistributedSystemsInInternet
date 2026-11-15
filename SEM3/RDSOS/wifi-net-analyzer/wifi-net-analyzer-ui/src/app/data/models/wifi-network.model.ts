import { Band } from "./band.model";
import { SignalLevel } from "./signal-level.model";

export interface WifiNetwork {
    ssid: string;
    bssid: string;
    manufacturer: string | null;
    phyType: string;
    rssi: number;
    linkQuality: number;
    signalLevel: SignalLevel;
    frequencyKhz: number;
    band: Band | null;
    channel: number;
}