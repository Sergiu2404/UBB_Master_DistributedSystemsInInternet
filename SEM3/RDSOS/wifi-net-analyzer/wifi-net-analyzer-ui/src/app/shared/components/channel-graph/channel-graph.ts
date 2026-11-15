import { Component, computed, input } from '@angular/core';
import { ChartData, Point, ChartOptions, ChartDataset } from 'chart.js';
import { FIRST_DISPLAYED_CHANNEL, LAST_DISPLAYED_CHANNEL, SIGNAL_FLOOR_DBM, SIGNAL_CEILING_DBM } from '../../../data/constants/wifi-analyzer.constants';
import { toHueFromBssid, toDisplayedSsid } from '../../../data/helpers/wifi-display-utils';
import { ChannelOccupancy } from '../../../data/models/channel-occupancy.model';
import { BaseChartDirective } from 'ng2-charts';

const AXIS_PADDING_CHANNELS = 2;
const PEAK_HALF_WIDTH_CHANNELS = 1;
const AXIS_TEXT_COLOR = '#94a3b8';
const GRID_LINE_COLOR = 'rgba(148, 163, 184, 0.15)';

@Component({
  imports: [BaseChartDirective],
  selector: 'app-channel-graph',
  styleUrl: './channel-graph.css',
  templateUrl: './channel-graph.html',
})
export class ChannelGraph {
  readonly channelOccupancies = input.required<ChannelOccupancy[]>();

  protected readonly chartData = computed<ChartData<'line', Point[]>>(() => ({
    datasets: this.channelOccupancies().map((channelOccupancy) => this.toDataset(channelOccupancy)),
  }));

  protected readonly chartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: false,
    animation: false,
    scales: {
      x: {
        type: 'linear',
        min: FIRST_DISPLAYED_CHANNEL - AXIS_PADDING_CHANNELS,
        max: LAST_DISPLAYED_CHANNEL + AXIS_PADDING_CHANNELS,
        ticks: {
          stepSize: 1,
          color: AXIS_TEXT_COLOR,
          callback: (tickValue) => this.toChannelTickLabel(Number(tickValue)),
        },
        grid: { color: GRID_LINE_COLOR },
        title: { display: true, text: 'Channel', color: AXIS_TEXT_COLOR },
      },
      y: {
        min: SIGNAL_FLOOR_DBM,
        max: SIGNAL_CEILING_DBM,
        ticks: { color: AXIS_TEXT_COLOR },
        grid: { color: GRID_LINE_COLOR },
        title: { display: true, text: 'Signal strength (dBm)', color: AXIS_TEXT_COLOR },
      },
    },
    plugins: {
      legend: { position: 'bottom', labels: { color: AXIS_TEXT_COLOR } },
    },
  };

  private toDataset(channelOccupancy: ChannelOccupancy): ChartDataset<'line', Point[]> {
    const networkHue = toHueFromBssid(channelOccupancy.bssid);
    return {
      label: `${toDisplayedSsid(channelOccupancy.ssid)} (ch ${channelOccupancy.channel})`,
      data: [
        { x: channelOccupancy.from, y: SIGNAL_FLOOR_DBM },
        { x: channelOccupancy.channel - PEAK_HALF_WIDTH_CHANNELS, y: channelOccupancy.rssi },
        { x: channelOccupancy.channel + PEAK_HALF_WIDTH_CHANNELS, y: channelOccupancy.rssi },
        { x: channelOccupancy.to, y: SIGNAL_FLOOR_DBM },
      ],
      borderColor: `hsl(${networkHue}, 75%, 55%)`,
      backgroundColor: `hsla(${networkHue}, 75%, 55%, 0.15)`,
      fill: 'start',
      tension: 0.4,
      pointRadius: 0,
      borderWidth: 2,
    };
  }

  private toChannelTickLabel(channelNumber: number): string {
    const isDisplayedChannel =
      channelNumber >= FIRST_DISPLAYED_CHANNEL && channelNumber <= LAST_DISPLAYED_CHANNEL;
    return isDisplayedChannel ? String(channelNumber) : '';
  }
}
