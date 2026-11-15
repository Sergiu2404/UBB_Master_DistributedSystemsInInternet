import { HIDDEN_NETWORK_LABEL } from "../constants/wifi-analyzer.constants";

export function toDisplayedSsid(ssid: string): string {
    const cleanedSsid = ssid.replace(/\u0000/g, '').trim();
    return cleanedSsid.length > 0 ? cleanedSsid : HIDDEN_NETWORK_LABEL;
}

export function toHueFromBssid(bssid: string): number {
    let hue = 0;

    for (const character of bssid) {
        hue = (hue * 31 + character.charCodeAt(0)) % 360;
    }

    return hue;
}