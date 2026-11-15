import { Routes } from '@angular/router';

export const routes: Routes = [

    { path: '', redirectTo: 'wifi', pathMatch: 'full' },
    {
        path: 'wifi',
        loadComponent: () =>
        import('./pages/wifi-analyzer-page/wifi-analyzer-page').then(
            (module) => module.WifiAnalyzerPage,
        ),
    }
];
