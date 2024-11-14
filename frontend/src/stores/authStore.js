// src/stores/authStore.js
import { writable } from 'svelte/store';

// Store for tracking if the user is authenticated
export const isAuthenticated = writable(false);

// Store for holding the JWT token
export const token = writable(localStorage.getItem('token') || null);

// Sync token changes to localStorage and update isAuthenticated status
token.subscribe((value) => {
    if (value) {
        localStorage.setItem('token', value);
        isAuthenticated.set(true);
    } else {
        localStorage.removeItem('token');
        isAuthenticated.set(false);
    }
});
