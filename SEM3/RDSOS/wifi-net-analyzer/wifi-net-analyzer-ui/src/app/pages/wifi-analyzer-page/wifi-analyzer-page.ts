import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { WifiStateService } from '../../data/services/wifi-state.service';
import { NetworkCard } from '../../shared/components/network-card/network-card';
import { ChannelGraph } from '../../shared/components/channel-graph/channel-graph';
import { InterfaceSummary } from '../../shared/components/interface-summary/interface-summary';

@Component({
  imports: [NetworkCard, ChannelGraph, InterfaceSummary, DatePipe],
  selector: 'app-wifi-analyzer-page',
  styleUrl: './wifi-analyzer-page.css',
  templateUrl: './wifi-analyzer-page.html',
})
export class WifiAnalyzerPage implements OnInit, OnDestroy {
  protected readonly wifiAnalyzerState = inject(WifiStateService);

  ngOnInit(): void {
    this.wifiAnalyzerState.startPolling();
  }

  ngOnDestroy(): void {
    this.wifiAnalyzerState.stopPolling();
  }
}
