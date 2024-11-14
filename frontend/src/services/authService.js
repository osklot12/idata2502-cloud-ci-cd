// src/utils/auth.js
import { token } from '../stores/authStore';

export async function login(username, password) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            const jwtToken = data.token; // Adjust if necessary

            // Save the token in the store
            token.set(jwtToken);
            return { success: true };
        } else {
            const errorData = await response.json(); // Capture the error message
            return { success: false, message: errorData.message || 'Login failed' };
        }
    } catch (error) {
        console.error('Error during login:', error);
        return { success: false, message: 'An error occurred during login' };
    }
}

export function logout() {
    token.set(null); // This will clear local storage and set isAuthenticated to false
}

export async function register(username, email, password) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        const data = await response.json(); // Parse the response JSON

        if (!response.ok) {
            // If the response is not ok (e.g., 400), throw an error with the backend message
            throw new Error(data.error || 'Registration failed');
        }

        // Set the token if registration is successful
        token.set(data.token); // Adjust if your backend doesn't return a token on registration
        return { success: true, message: data.message }; // Return success and any backend message

    } catch (error) {
        console.error('Error during registration:', error);
        return { success: false, message: error.message }; // Return error message from backend
    }
}