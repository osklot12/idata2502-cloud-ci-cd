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
            const jwtToken = data.token; // Adjust based on actual response structure

            // Save the token in the store
            token.set(jwtToken);
            return true;
        } else {
            console.error('Login failed');
            return false;
        }
    } catch (error) {
        console.error('Error during login:', error);
        return false;
    }
}

export function logout() {
    token.set(null); // This will clear local storage and set isAuthenticated to false
}
