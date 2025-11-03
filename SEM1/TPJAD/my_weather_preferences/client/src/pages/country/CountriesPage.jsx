import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function AddCountryModal({ onClose, onSave }) {
    const [name, setName] = useState("");
    const [region, setRegion] = useState("");

    const handleSave = () => {
        onSave(name, region);
        setName("");
        setRegion("");
    };

    return (
        <div style={{ border: "1px solid black", padding: "10px" }}>
            <h3>Add Country</h3>
            <input value={name} onChange={e => setName(e.target.value)} placeholder="Name" />
            <input value={region} onChange={e => setRegion(e.target.value)} placeholder="Region" />
            <button onClick={handleSave}>Save</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
}
function EditCountryModal({ country, onClose, onSave }) {
    const [name, setName] = useState(country.name);
    const [region, setRegion] = useState(country.region);

    const handleSave = () => {
        onSave(country.id, name, region); // send id for PUT
    };

    return (
        <div style={{ border: "1px solid black", padding: "10px" }}>
            <h3>Edit Country</h3>
            <input value={name} onChange={e => setName(e.target.value)} placeholder="Name" />
            <input value={region} onChange={e => setRegion(e.target.value)} placeholder="Region" />
            <button onClick={handleSave}>Save</button>
            <button onClick={onClose}>Cancel</button>
        </div>
    );
}

function CountriesPage() {
    const [countries, setCountries] = useState([]);
    const [addModalOpen, setAddModalOpen] = useState(false);
    const [editModalOpen, setEditModalOpen] = useState(null); // country object
    const [deleteConfirm, setDeleteConfirm] = useState(null);

    const navigate = useNavigate();

    const fetchCountries = () => {
        fetch("/countries")
            .then(res => res.json())
            .then(data => setCountries(data));
    };

    useEffect(() => {
        fetchCountries();
    }, []);

    const addCountry = (name, region) => {
        fetch("/countries", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${encodeURIComponent(name)}&region=${encodeURIComponent(region)}`
        }).then(() => {
            setAddModalOpen(false);
            fetchCountries();
        });
    };

    const editCountry = (id, name, region) => {
        fetch(`/countries?countryId=${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${encodeURIComponent(name)}&region=${encodeURIComponent(region)}`
        }).then(() => {
            setEditModalOpen(null);
            fetchCountries();
        });
    };

    const deleteCountry = (id) => {
        fetch(`/countries?countryId=${id}`, { method: "DELETE" })
            .then(() => {
                setDeleteConfirm(null);
                fetchCountries();
            });
    };

    return (
        <div>
            <h1>Countries</h1>
            <button onClick={() => setAddModalOpen(true)}>Add Country</button>

            <ul>
                {countries.map(c => (
                    <li key={c.id} style={{display: "flex", alignItems: "center", justifyContent: "space-between", padding: "5px 0", borderBottom: "1px solid #ccc"}}>
                        <span>{c.name} ({c.region})</span>
                        <div style={{ display: "flex", gap: "5px" }}>
                            <button onClick={() => setEditModalOpen(c)}>Edit</button>
                            <button style={{ backgroundColor: "red", color: "white"}} onClick={() => setDeleteConfirm(c.id)}>Delete</button>
                            <button onClick={() => navigate(`/locations/${c.id}`)}>See locations</button>
                        </div>
                    </li>
                ))}
            </ul>

            {/* Modals */}
            {addModalOpen && <AddCountryModal onClose={() => setAddModalOpen(false)} onSave={addCountry} />}
            {editModalOpen && <EditCountryModal country={editModalOpen} onClose={() => setEditModalOpen(null)} onSave={editCountry} />}

            {/* Delete confirmation */}
            {deleteConfirm !== null && (
                <div style={{ border: "1px solid red", padding: "10px", marginTop: "10px" }}>
                    <p>Are you sure you want to delete this country?</p>
                    <button onClick={() => deleteCountry(deleteConfirm)}>Yes</button>
                    <button onClick={() => setDeleteConfirm(null)}>No</button>
                </div>
            )}
        </div>
    );
}

export default CountriesPage;
