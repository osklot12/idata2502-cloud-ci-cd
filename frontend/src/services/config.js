let API_BASE_URL;

const FALLBACK_URL = "http://localhost:8080/api";

async function fetchConfig() {
    try {
        const response = await fetch('/config.json');
        const config = await response.json();
        API_BASE_URL = config.VITE_API_BASE_URL || FALLBACK_URL;
    } catch (error) {
        console.warn("Failed to fetch API configuration: ", error);
        API_BASE_URL = FALLBACK_URL;
    } finally {
        console.log("Backend API set to URL: ", API_BASE_URL);
    }
}

export { API_BASE_URL, fetchConfig };
