import { app, BrowserWindow } from 'electron';
import * as path from 'path';

const isDev = !app.isPackaged && process.env['ELECTRON_PROD'] !== '1';

function createWindow(): void {
  const win = new BrowserWindow({
    width: 1400,
    height: 900,
    title: 'WiFi Net Analyzer',
    webPreferences: { contextIsolation: true, nodeIntegration: false },
  });

  if (isDev) {
    win.loadURL('http://localhost:4200');
    win.webContents.openDevTools();
  } else {
    win.loadFile(path.join(__dirname, '../../dist/wifi-net-analyzer-ui/browser/index.html'));
  }
}

app.whenReady().then(createWindow);
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});