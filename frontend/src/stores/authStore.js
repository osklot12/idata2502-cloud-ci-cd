import { writable } from 'svelte/store'

// store to hold the jwt token
export const token = writable(localStorage.getItem('token') || null);

export const userId = writable(localStorage.getItem('userId') || null);

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

userId.subscribe((value) => {
    if (value) {
        localStorage.setItem('userId', value);
    } else {
        localStorage.removeItem('userId');
    }
});

// helper functions for setting and clearing the token
export function setToken(newToken) {
    token.set(newToken);
}

export function clearToken() {
    token.set(null);
}

export function setUserId(newUserId) {
    userId.set(newUserId);
}

export function clearUserId() {
    userId.set(null);
}