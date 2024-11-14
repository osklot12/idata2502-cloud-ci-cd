// src/stores/auth.js
import { writable } from 'svelte/store';

export const jwtToken = writable(localStorage.getItem('jwt') || '');

jwtToken.subscribe((value) => {
    localStorage.setItem('jwt', value);
});
