import './App.css';
import {BrowserRouter, Routes, Route, Link, Navigate} from "react-router-dom";
import CountriesPage from './pages/country/CountriesPage';
import LocationsPage from './pages/location/LocationsPage';
import PreferencesPage from './pages/preferences/PreferencesPage';

export default function App() {

  return (
    <BrowserRouter>
      <nav>
        <Link to="/countries">Countries</Link> |{" "}
        <Link to="/locations">Locations</Link> |{" "}
        <Link to="/preferences">Preferences</Link>
      </nav>

      <Routes>
        <Route path="/" element={<Navigate to="/countries" />} /> {/* default */}
        <Route path="/countries" element={<CountriesPage />} />
        <Route path="/locations" element={<LocationsPage />} />
        <Route path="/preferences" element={<PreferencesPage />} />
      </Routes>
    </BrowserRouter>
  );
}
