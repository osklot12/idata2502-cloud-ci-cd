import { mount } from 'svelte';
import './app.css';
import App from './App.svelte';
import { CallHandler } from './services/callHandler.js';
import { SpringApi } from './services/api/springApi.js';

// Declare `api` globally and initialize it later
export let api = null;

let app; // Declare `app` globally

async function main() {
  try {
    // Initialize SpringApi
    const springApi = new SpringApi();
    await springApi.init();
    console.log("SpringApi initialized successfully");

    // Initialize CallHandler and update `api`
    api = new CallHandler(springApi);

    // Mount the app
    app = mount(App, {
      target: document.getElementById('app'),
    });
  } catch (error) {
    console.error("Failed to initialize the application:", error);
  }
}

main();

export default app;
