import { Component, computed, input } from '@angular/core';
import { SIGNAL_LEVEL_TEXT_COLOR_CLASSES, SIGNAL_LEVEL_ACTIVE_ARC_COUNTS } from '../../../data/constants/wifi-analyzer.constants';
import { SignalLevel } from '../../../data/models/signal-level.model';

const ACTIVE_ARC_OPACITY = 1;
const INACTIVE_ARC_OPACITY = 0.2;

@Component({
  imports: [],
  selector: 'app-signal-strength-icon',
  styleUrl: './signal-strength-icon.css',
  templateUrl: './signal-strength-icon.html',
})
export class SignalStrengthIcon {
  readonly signalLevel = input.required<SignalLevel>();

  protected readonly colorClass = computed(() => SIGNAL_LEVEL_TEXT_COLOR_CLASSES[this.signalLevel()]);
  protected readonly activeArcCount = computed(() => SIGNAL_LEVEL_ACTIVE_ARC_COUNTS[this.signalLevel()]);

  protected arcOpacity(arcIndex: number): number {
    return arcIndex <= this.activeArcCount() 
      ? ACTIVE_ARC_OPACITY
      : INACTIVE_ARC_OPACITY;
  }
}
