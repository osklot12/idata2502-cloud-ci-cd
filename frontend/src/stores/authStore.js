import { writable } from 'svelte/store'

// store to hold the jwt token
export const token = writable(localStorage.getItem('token') || null);

// store to track if the user is authenticated
export const isAuthenticated = writable(!!localStorage.getItem('token'));

// keep localStorage and isAuthenticated in sync with the token store
token.subscribe((value) => {
    if (value) {
        localStorage.setItem('token', value);
        isAuthenticated.set(true);
    } else {
        localStorage.removeItem('token');
        isAuthenticated.set(false);
    }
})

// helper functions for setting and clearing the token
export function setToken(newToken) {
    token.set(newToken);
}

export function clearToken() {
    token.set(null);
}