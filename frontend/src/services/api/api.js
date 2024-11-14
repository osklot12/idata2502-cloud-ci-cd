import {get} from 'svelte/store'
import {token, setToken, clearToken} from '../../stores/authStore.js'

const API_BASE_URL = "http://localhost:8080/api"

// function to log in a user
export async function login(username, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({username, password})
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Login failed");
        }

        const data = await response.json();
        setToken(data.token);
        return {success: true};
    } catch (error) {
        console.error("Error during login:", error)
        return {success: false, message: error.message}
    }
}

// function to register a new user
export async function register(username, email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ username, email, password })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Registration failed");
        }

        const data = await response.json();
        setToken(data.token);
        return { success: true, message: data.message };
    } catch (error) {
        console.error("Error during registration:", error);
        return { success: false, message: error.message };
    }
}

// function to log out the user
export function logout() {
    clearToken();
}

// function to fetch tasks
export async function getTasks() {
    const jwtToken = get(token);

    const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwtToken}`
        }
    });

    if (!response.ok) {
        throw new Error("Failed to fetch tasks");
    }

    return await response.json();
}

// function to create a new task
export async function createTask(task) {
    const jwtToken = get(token);

    const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify(task)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to create task");
    }

    return await response.json();
}