const API_URL = "http://localhost:5071/api/Transactions";

const handleResponse = async (response) => {
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage || `Error: ${response.status}`);
    }

    if (response.status !== 204) {
        return await response.json();
    }

    return null;
};

export const transactionsService = {
    async getAll() {
        const response = await fetch(API_URL);
        return handleResponse(response);
    },

    async getById(id) {
        const response = await fetch(`${API_URL}/${id}`);
        return handleResponse(response);
    },

    async getBalance() {
        const response = await fetch(`${API_URL}/balance`);
        return handleResponse(response);
    },

    async getByType(type) {
        const response = await fetch(`${API_URL}/type/${type}`);
        return handleResponse(response);
    },

    async getGrouped() {
        const response = await fetch(`${API_URL}/grouped`);
        return handleResponse(response);
    },

    async create(dto) {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });
        return handleResponse(response);
    },

    async update(id, dto) {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });

        await handleResponse(response);
        return { id, ...dto };
    },

    async remove(id) {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        await handleResponse(response);
        return id;
    }
};