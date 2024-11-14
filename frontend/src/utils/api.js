// src/utils/api.js

const API_BASE_URL = 'http://localhost:8080/api'; // adjust path as needed

export async function login(credentials) {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    });

    if (!response.ok) {
        throw new Error('Login failed');
    }

    return await response.json(); // Expecting a response containing the JWT token
}

export async function getTasks() {
    const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
            // Include authorization headers here if needed
        }
    });
    return await response.json();
}

export async function createTask(task) {
    const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
            // Include authorization headers here if needed
        },
        body: JSON.stringify(task)
    });
    return await response.json();
}
