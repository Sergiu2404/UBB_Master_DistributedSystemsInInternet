import { inject, Injectable, signal } from "@angular/core";
import { WifiService } from "./wifi.service";
import { catchError, EMPTY, forkJoin, Observable, Subscription, switchMap, timer } from "rxjs";
import { ChannelOccupancy } from "../models/channel-occupancy.model";
import { WifiInterface } from "../models/wifi-interface.model";
import { WifiNetwork } from "../models/wifi-network.model";
import { WIFI_POLLING_INTERVAL_MILLISECONDS } from "../constants/wifi-analyzer.constants";
import { WifiSnapshot } from "../models/wifi-snapshot.model";

@Injectable({ providedIn: 'root' })
export class WifiStateService {
    private readonly wifiService = inject(WifiService);
    private pollingSubscription: Subscription | null = null;

    readonly interfaces = signal<WifiInterface[]>([]);
    readonly networks = signal<WifiNetwork[]>([]);
    readonly channelOccupancies = signal<ChannelOccupancy[]>([]);
    
    readonly errorMessage = signal<string | null>(null);
    readonly lastUpdatedAt = signal<Date | null>(null);

    startPolling(): void {
        if (this.pollingSubscription) {
            return;
        }

        this.pollingSubscription = timer(0, WIFI_POLLING_INTERVAL_MILLISECONDS)
            .pipe(switchMap(() => this.fetchSnapshot()))
            .subscribe((wifiSnapshot) => this.applySnapshot(wifiSnapshot));
    }

    stopPolling(): void {
        this.pollingSubscription?.unsubscribe();
        this.pollingSubscription = null;
    }

    private fetchSnapshot(): Observable<WifiSnapshot> {
        return forkJoin({
            interfaces: this.wifiService.getInterfaces(),
            networks: this.wifiService.getNetworks(),
            channelOccupancies: this.wifiService.getChannelOccupancies(),
        }).pipe(
            catchError(() => {
                this.errorMessage.set('Cannot reach the backend right now');
                return EMPTY;
            }),
        );
    }

    private applySnapshot(wifiSnapshot: WifiSnapshot): void {
        this.interfaces.set(wifiSnapshot.interfaces);
        this.networks.set(wifiSnapshot.networks);
        this.channelOccupancies.set(wifiSnapshot.channelOccupancies);

        this.errorMessage.set(null);
        this.lastUpdatedAt.set(new Date());
    }
}