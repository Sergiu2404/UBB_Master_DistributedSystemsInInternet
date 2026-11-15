import { Component, input } from '@angular/core';
import { WifiInterface } from '../../../data/models/wifi-interface.model';

@Component({
  imports: [],
  selector: 'app-interface-summary',
  styleUrl: './interface-summary.css',
  templateUrl: './interface-summary.html',
})
export class InterfaceSummary {
  readonly interfaces = input.required<WifiInterface[]>();
}
