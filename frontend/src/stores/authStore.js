import { writable } from 'svelte/store'

// store to hold the jwt token
export const token = writable(localStorage.getItem('token') || null);

export const userId = writable(localStorage.getItem('userId') || null);

// store to track if the user is authenticated
export const isAuthenticated = writable(!!localStorage.getItem('token'));

// helper function to decode a JWT token
function decodeToken(token) {
    try {
        return JSON.parse(atob(token.split(".")[1]));
    } catch (error) {
        console.error("Failed to decode token:", error);
        return null;
    }
}

// function to check if the token is expired
function isTokenExpired(token) {
    const decoded = decodeToken(token);
    if (!decoded || !decoded.exp) return true;

    const currentTime = Math.floor(Date.now() / 1000);
    return decoded.exp < currentTime;
}

// keep localStorage and isAuthenticated in sync with the token store
token.subscribe((value) => {
    if (value) {
        if (isTokenExpired(value)) {
            console.warn("Token is expired. Clearing token.")
            clearToken();
        } else {
            localStorage.setItem('token', value);
            isAuthenticated.set(true);
        }
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