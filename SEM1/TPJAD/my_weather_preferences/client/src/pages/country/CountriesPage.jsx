import { useState, useEffect } from "react";

function CountriesPage() {
    const [countries, setCountries] = useState([]);
    const [name, setName] = useState("");
    const [region, setRegion] = useState("");
    const [modalOpen, setModalOpen] = useState(false);

    const fetchCountries = () => {
        fetch("/countries")
            .then(res => res.json())
            .then(data => setCountries(data));
    };

    useEffect(() => {
        fetchCountries();
    }, []);


    const addCountry = () => {
        fetch("/countries", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `name=${encodeURIComponent(name)}&region=${encodeURIComponent(region)}`
        }).then(() => {
            setModalOpen(false); // close modal
            setName("");
            setRegion("");
            fetchCountries(); // refresh list
        });
    };

    const deleteCountry = (id) => {
        fetch(`/countries?id=${id}`, { method: "DELETE" })
            .then(() => fetchCountries());
    };

    return (
        <div>
            <h1>Countries</h1>
            <button onClick={() => setModalOpen(true)}>Add Country</button>

            <ul>
                {countries.map(c => (
                    <li key={c.id}>
                        {c.name} ({c.region}) {" "}
                        <button onClick={() => { setName(c.name); setRegion(c.region); setModalOpen(true); }}>Edit</button>
                        <button onClick={() => deleteCountry(c.id)}>Delete</button>
                    </li>
                ))}
            </ul>

            {modalOpen && (
                <div style={{ border: "1px solid black", padding: "10px" }}>
                    <h3>{name ? "Edit Country" : "Add Country"}</h3>
                    <input value={name} onChange={e => setName(e.target.value)} placeholder="Name" />
                    <input value={region} onChange={e => setRegion(e.target.value)} placeholder="Region" />
                    <button onClick={addCountry}>Save</button>
                    <button onClick={() => setModalOpen(false)}>Cancel</button>
                </div>
            )}
        </div>
    );
}

export default CountriesPage;
