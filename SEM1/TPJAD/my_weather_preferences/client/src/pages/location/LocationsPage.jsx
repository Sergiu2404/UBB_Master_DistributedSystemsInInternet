import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";


function AddLocationModal({ onClose, onSave }) {
    const [name, setName] = useState("");
    const [errorMsg, setErrorMsg] = useState("");

    const handleSave = async () => {
        try {
            await onSave(name);
            setName("");
            setErrorMsg("");
        } catch (err) {
            setErrorMsg(err.message || "Failed to add location");
        }
    };

    return (
        <div style={{ border: "1px solid black", padding: "10px" }}>
            <h3>Add Location</h3>
            <input
                value={name}
                onChange={e => setName(e.target.value)}
                placeholder="Name"
            />
            {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
            <button onClick={handleSave}>Save</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
}

function EditLocationModal({ location, onClose, onSave }) {
    const [name, setName] = useState(location.name);
    const [errorMsg, setErrorMsg] = useState("");

    const handleSave = async () => {
        try {
            await onSave(location.id, name);
            setErrorMsg("");
        } catch (err) {
            setErrorMsg(err.message || "Failed to update location");
        }
    };

    return (
        <div style={{ border: "1px solid black", padding: "10px" }}>
            <h3>Edit Location</h3>
            <input
                value={name}
                onChange={e => setName(e.target.value)}
                placeholder="Name"
            />
            {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
            <button onClick={handleSave}>Save</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
}



function LocationsPage() {
    const { countryId } = useParams();
    const [country, setCountry] = useState(null);
    const [locations, setLocations] = useState([]);
    const [addModalOpen, setAddModalOpen] = useState(false);
    const [editModalOpen, setEditModalOpen] = useState(null);
    const [deleteConfirm, setDeleteConfirm] = useState(null);

    const navigate = useNavigate();


    const fetchCountryById = () => {
        fetch(`/api/countries?countryId=${countryId}`)
            .then(res => res.json())
            .then(data => setCountry(data))
            .catch(err => console.error("Error fetching country:", err));
    }
    const fetchLocationsForCountry = () => {
        fetch(`/api/locations?countryId=${countryId}`)
            .then(res => res.json())
            .then(data => setLocations(data))
            .catch(err => console.error("Error fetching locations:", err));
    };

    useEffect(() => {
        fetchCountryById();
        fetchLocationsForCountry();
    }, [countryId]);

    const addLocation = (name) => {
        return fetch("/api/locations", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${encodeURIComponent(name)}&countryId=${countryId}`
        })
            .then(async res => {
                const data = await res.json();
                if (!res.ok) throw new Error(data.error || "Failed to add location");
                setAddModalOpen(false);
                fetchLocationsForCountry();
            });
    };

    const editLocation = (id, name) => {
        return fetch(`/api/locations?locationId=${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${encodeURIComponent(name)}`
        })
            .then(async res => {
                const data = await res.json();
                if (!res.ok) throw new Error(data.error || "Failed to update location");
                setEditModalOpen(null);
                fetchLocationsForCountry();
            });
    };


    const deleteLocation = (id) => {
        fetch(`/api/locations?locationId=${id}`, { method: "DELETE" })
            .then(() => {
                setDeleteConfirm(null);
                fetchLocationsForCountry();
            });
    };

    if (!country) return <p>Loading country data...</p>;

    return (
        <div>
            <h3>Locations for {country.name} ({country.region})</h3>
            <button onClick={() => setAddModalOpen(true)}>Add Location</button>

            <ul>
                {locations.map(loc => (
                    <li
                        key={loc.id}
                        style={{
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "space-between",
                            padding: "5px 0",
                            borderBottom: "1px solid #ccc"
                        }}
                    >
                        <span>
                            {loc.name} (Lat: {loc.latitude}, Lon: {loc.longitude})
                        </span>
                        <div style={{ display: "flex", gap: "5px" }}>
                            <button onClick={() => setEditModalOpen(loc)}>Edit</button>
                            <button
                                style={{ backgroundColor: "red", color: "white" }}
                                onClick={() => setDeleteConfirm(loc.id)}
                            >
                                Delete
                            </button>
                            <button onClick={() => navigate(`/preferences/${loc.id}`)}>
                                See Preferences
                            </button>
                        </div>
                    </li>
                ))}
            </ul>


            {addModalOpen && (
                <AddLocationModal
                    onClose={() => setAddModalOpen(false)}
                    onSave={addLocation}
                />
            )}

            {editModalOpen && (
                <EditLocationModal
                    location={editModalOpen}
                    onClose={() => setEditModalOpen(null)}
                    onSave={editLocation}
                />
            )}

            {deleteConfirm !== null && (
                <div style={{ border: "1px solid red", padding: "10px", marginTop: "10px" }}>
                    <p>Are you sure you want to delete this location?</p>
                    <button onClick={() => deleteLocation(deleteConfirm)}>Yes</button>
                    <button onClick={() => setDeleteConfirm(null)}>No</button>
                </div>
            )}
        </div>
    );
}

export default LocationsPage;
