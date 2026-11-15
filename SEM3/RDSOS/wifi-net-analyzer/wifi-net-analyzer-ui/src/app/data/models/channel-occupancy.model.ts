export interface ChannelOccupancy {
  ssid: string;
  bssid: string;
  channel: number;
  from: number;
  to: number;
  rssi: number;
}