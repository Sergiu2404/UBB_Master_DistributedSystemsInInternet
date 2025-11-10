import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";


function AddPreferenceModal({ onClose, onSave }) {
    const [description, setDescription] = useState("");
    const [minTemp, setMinTemp] = useState("");
    const [maxTemp, setMaxTemp] = useState("");
    const [errorMsg, setErrorMsg] = useState("");

    const handleSave = async () => {
        try {
            await onSave(description, minTemp, maxTemp);
            setDescription("");
            setMinTemp("");
            setMaxTemp("");
            setErrorMsg("");
        } catch (err) {
            setErrorMsg(err.message || "Failed to add preference");
        }
    };

    return (
        <div style={{ border: "1px solid black", padding: "10px" }}>
            <h3>Add Preference</h3>
            <input
                value={description}
                onChange={e => setDescription(e.target.value)}
                placeholder="Description"
            />
            <input
                value={minTemp}
                type="number"
                step="0.01"
                min="-90"
                max="90"
                onChange={e => setMinTemp(e.target.value)}
                placeholder="Min Temperature"
            />
            <input
                value={maxTemp}
                type="number"
                step="0.01"
                min="-90"
                max="90"
                onChange={e => setMaxTemp(e.target.value)}
                placeholder="Max Temperature"
            />
            {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
            <button onClick={handleSave}>Save</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
}

function EditPreferenceModal({ preference, onClose, onSave }) {
    const [description, setDescription] = useState(preference.description);
    const [minTemp, setMinTemp] = useState(preference.minTemperature);
    const [maxTemp, setMaxTemp] = useState(preference.maxTemperature);
    const [errorMsg, setErrorMsg] = useState("");

    const handleSave = async () => {
        try {
            await onSave(preference.id, description, minTemp, maxTemp);
            setErrorMsg("");
        } catch (err) {
            setErrorMsg(err.message || "Failed to update preference");
        }
    };

    return (
        <div style={{ border: "1px solid black", padding: "10px" }}>
            <h3>Edit Preference</h3>
            <input
                value={description}
                onChange={e => setDescription(e.target.value)}
                placeholder="Description"
            />
            <input
                value={minTemp}
                type="number"
                step="0.01"
                min="-90"
                max="90"
                onChange={e => setMinTemp(e.target.value)}
                placeholder="Min Temperature"
            />
            <input
                value={maxTemp}
                type="number"
                step="0.01"
                min="-90"
                max="90"
                onChange={e => setMaxTemp(e.target.value)}
                placeholder="Max Temperature"
            />
            {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
            <button onClick={handleSave}>Save</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
}

function PreferencesPage() {
    const { locationId } = useParams();
    const [location, setLocation] = useState(null);
    const [preferences, setPreferences] = useState([]);
    const [forecastData, setForecastData] = useState([]);
    const [forecastVisible, setForecastVisible] = useState({}); // {[pref.id]: true/false}
    const [searchDescription, setSearchDescription] = useState("");

    const [addModalOpen, setAddModalOpen] = useState(false);
    const [editModalOpen, setEditModalOpen] = useState(null);
    const [deleteConfirm, setDeleteConfirm] = useState(null);

    const navigate = useNavigate();

    const fetchLocationById = () => {
        fetch(`/api/locations?locationId=${locationId}`)
            .then(res => res.json())
            .then(data => setLocation(data))
            .catch(err => console.error("Error fetching location:", err));
    };

    const fetchPreferencesForLocation = () => {
        fetch(`/api/preferences?locationId=${locationId}`)
            .then(res => res.json())
            .then(data => setPreferences(data))
            .catch(err => console.error("Error fetching preferences:", err));
    };

    useEffect(() => {
        fetchLocationById();
        fetchPreferencesForLocation();
    }, [locationId]);

    const filteredPreferences = preferences.filter(pref =>
        pref.description.toLowerCase().includes(searchDescription.toLowerCase())
    );

    const handleSeeForecasts = async (pref) => {
        if (!location) return;

        if (forecastVisible[pref.id]) {
            setForecastVisible(prev => ({ ...prev, [pref.id]: false }));
            return;
        }

        const { latitude, longitude } = location;
        try {
            const res = await fetch(
                `/api/forecasts?lat=${latitude}&lon=${longitude}&minTemp=${pref.minTemperature}&maxTemp=${pref.maxTemperature}`
            );
            const data = await res.json();

            setForecastData(prev => ({
                ...prev,
                [pref.id]: data
            }));
            setForecastVisible(prev => ({
                ...prev,
                [pref.id]: true
            }));
        } catch (err) {
            console.error("Error fetching forecasts:", err);
        }
    };

    const addPreference = (description, minTemp, maxTemp) => {
        return fetch("/api/preferences", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `description=${encodeURIComponent(description)}&minTemp=${Number(minTemp)}&maxTemp=${Number(maxTemp)}&locationId=${locationId}`
        })
            .then(async res => {
                const data = await res.json();
                if (!res.ok) throw new Error(data.error || "Failed to add preference");
                setAddModalOpen(false);
                fetchPreferencesForLocation();
            });
    };

    const editPreference = (id, description, minTemp, maxTemp) => {
        return fetch(`/api/preferences?preferenceId=${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `description=${encodeURIComponent(description)}&minTemp=${minTemp}&maxTemp=${maxTemp}`
        })
            .then(async res => {
                const data = await res.json();
                if (!res.ok) throw new Error(data.error || "Failed to update preference");
                setEditModalOpen(null);
                fetchPreferencesForLocation();
            });
    };

    const deletePreference = (id) => {
        fetch(`/api/preferences?preferenceId=${id}`, { method: "DELETE" })
            .then(() => {
                setDeleteConfirm(null);
                fetchPreferencesForLocation();
            });
    };

    if (!location) return <p>Loading location data...</p>;

    return (
        <div>
            <h3>Weather preferences for {location.name}</h3>
            <button onClick={() => setAddModalOpen(true)}>Add Preference</button>

            <div style={{ margin: "10px 0" }}>
                <input
                    type="text"
                    value={searchDescription}
                    onChange={e => setSearchDescription(e.target.value)}
                    placeholder="Search by description"
                />
            </div>

            <ul>
                {filteredPreferences.map(pref => (
                    <li
                        key={pref.id}
                        style={{
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "flex-start",
                            justifyContent: "space-between",
                            padding: "10px 0",
                            borderBottom: "1px solid #ccc"
                        }}
                    >
                        <span>
                            {pref.description} (minTemp: {pref.minTemperature}, maxTemp: {pref.maxTemperature})
                        </span>
                        <div style={{ display: "flex", gap: "5px" }}>
                            <button onClick={() => setEditModalOpen(pref)}>Edit</button>
                            <button
                                style={{ backgroundColor: "red", color: "white" }}
                                onClick={() => setDeleteConfirm(pref.id)}
                            >
                                Delete
                            </button>
                            <button onClick={() => handleSeeForecasts(pref)}>
                                See Forecasts
                            </button>
                        </div>

                        {forecastVisible[pref.id] && forecastData[pref.id] && (
                            <div
                                style={{
                                    marginTop: "8px",
                                    border: "1px solid #eee",
                                    borderRadius: "5px",
                                    backgroundColor: "#f9f9f9",
                                    padding: "10px 15px",
                                }}
                            >
                                <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "5px" }}>
                                    <button
                                        onClick={() => setForecastVisible(prev => ({ ...prev, [pref.id]: false }))}
                                        style={{
                                            background: "transparent",
                                            border: "none",
                                            cursor: "pointer",
                                            fontWeight: "bold",
                                            fontSize: "18px",
                                            color: "#888"
                                        }}
                                    >
                                        ×
                                    </button>
                                </div>

                                {forecastData[pref.id].length === 0 ? (
                                    <p style={{ fontStyle: "italic", color: "#666", margin: "5px 0" }}>
                                        No forecasts available within this temperature range.
                                    </p>
                                ) : (
                                    <ul style={{ margin: 0, paddingLeft: "15px" }}>
                                        {forecastData[pref.id].map(f => (
                                            <li key={f.date}>
                                                {f.date}: min {f.minTemp}°C, max {f.maxTemp}°C
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        )}
                    </li>
                ))}
            </ul>

            {addModalOpen && (
                <AddPreferenceModal
                    onClose={() => setAddModalOpen(false)}
                    onSave={addPreference}
                />
            )}

            {editModalOpen && (
                <EditPreferenceModal
                    preference={editModalOpen}
                    onClose={() => setEditModalOpen(null)}
                    onSave={editPreference}
                />
            )}

            {deleteConfirm !== null && (
                <div style={{ border: "1px solid red", padding: "10px", marginTop: "10px" }}>
                    <p>Are you sure you want to delete this preference?</p>
                    <button onClick={() => deletePreference(deleteConfirm)}>Yes</button>
                    <button onClick={() => setDeleteConfirm(null)}>No</button>
                </div>
            )}
        </div>
    );
}

export default PreferencesPage;
