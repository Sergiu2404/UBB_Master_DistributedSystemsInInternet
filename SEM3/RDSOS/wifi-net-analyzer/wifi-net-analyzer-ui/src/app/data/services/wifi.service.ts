import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ChannelOccupancy } from "../models/channel-occupancy.model";
import { WifiInterface } from "../models/wifi-interface.model";
import { WifiNetwork } from "../models/wifi-network.model";
import { WIFI_API_URL } from "../constants/wifi-analyzer.constants";

@Injectable({ providedIn: 'root' })
export class WifiService {
    private readonly httpClient = inject(HttpClient);
    private readonly wifiBaseUrl = WIFI_API_URL;

    getInterfaces(): Observable<WifiInterface[]> {
        return this.httpClient.get<WifiInterface[]>(`${this.wifiBaseUrl}/interfaces`);
    }

    getNetworks(): Observable<WifiNetwork[]> {
        return this.httpClient.get<WifiNetwork[]>(`${this.wifiBaseUrl}/networks`);
    }

    getChannelOccupancies(): Observable<ChannelOccupancy[]> {
        return this.httpClient.get<ChannelOccupancy[]>(`${this.wifiBaseUrl}/channels`);
    }
}