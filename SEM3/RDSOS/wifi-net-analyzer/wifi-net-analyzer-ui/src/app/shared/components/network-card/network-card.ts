import { Component, computed, input } from '@angular/core';
import { UNKNOWN_MANUFACTURER_LABEL, BAND_LABELS, SIGNAL_LEVEL_TEXT_COLOR_CLASSES } from '../../../data/constants/wifi-analyzer.constants';
import { toDisplayedSsid } from '../../../data/helpers/wifi-display-utils';
import { WifiNetwork } from '../../../data/models/wifi-network.model';
import { SignalStrengthIcon } from '../signal-strength-icon/signal-strength-icon';

const KILOHERTZ_PER_MEGAHERTZ = 1000;

@Component({
  imports: [SignalStrengthIcon],
  selector: 'app-network-card',
  styleUrl: './network-card.css',
  templateUrl: './network-card.html',
})
export class NetworkCard {
  readonly network = input.required<WifiNetwork>();

  protected readonly displayedSsid = computed(() => toDisplayedSsid(this.network().ssid));

  protected readonly displayedManufacturer = computed(
    () => this.network().manufacturer ?? UNKNOWN_MANUFACTURER_LABEL,
  );

  protected readonly frequencyMegahertz = computed(
    () => this.network().frequencyKhz / KILOHERTZ_PER_MEGAHERTZ,
  );

  protected readonly bandLabel = computed(() => {
    const band = this.network().band;
    return band 
      ? BAND_LABELS[band] 
      : '-';
  });

  protected readonly rssiColorClass = computed(
    () => SIGNAL_LEVEL_TEXT_COLOR_CLASSES[this.network().signalLevel],
  );
}
