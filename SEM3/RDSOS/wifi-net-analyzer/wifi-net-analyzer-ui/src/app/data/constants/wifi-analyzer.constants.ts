import { Band } from "../models/band.model";
import { SignalLevel } from "../models/signal-level.model";

export const API_BASE_URL = 'http://localhost:8080';
export const WIFI_API_URL = `${API_BASE_URL}/wifi`;

export const WIFI_POLLING_INTERVAL_MILLISECONDS = 3000;
export const SIGNAL_FLOOR_DBM = -100;
export const SIGNAL_CEILING_DBM = -20;
export const FIRST_DISPLAYED_CHANNEL = 1;
export const LAST_DISPLAYED_CHANNEL = 13;
export const HIDDEN_NETWORK_LABEL = 'Hidden network';
export const UNKNOWN_MANUFACTURER_LABEL = 'Unknown manufacturer';

export const BAND_LABELS: Record<Band, string> = {
  GHz_2_4: '2.4 GHz',
  GHz_5: '5 GHz',
  GHz_6: '6 GHz',
};

export const SIGNAL_LEVEL_TEXT_COLOR_CLASSES: Record<SignalLevel, string> = {
  GREEN: 'text-green-500',
  YELLOW: 'text-yellow-400',
  RED: 'text-red-500',
};

export const SIGNAL_LEVEL_ACTIVE_ARC_COUNTS: Record<SignalLevel, number> = {
  GREEN: 3,
  YELLOW: 2,
  RED: 1,
};