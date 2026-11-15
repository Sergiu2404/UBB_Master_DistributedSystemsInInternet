import { ChannelOccupancy } from './channel-occupancy.model';
import { WifiInterface } from './wifi-interface.model';
import { WifiNetwork } from './wifi-network.model';

export interface WifiSnapshot {
  interfaces: WifiInterface[];
  networks: WifiNetwork[];
  channelOccupancies: ChannelOccupancy[];
}