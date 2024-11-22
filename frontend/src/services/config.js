let API_BASE_URL;

async function fetchConfig() {
    try {
        const response = await fetch('/config.json');
        const config = await response.json();
        API_BASE_URL = config.VITE_API_BASE_URL || "http://localhost:8080/api";
        console.log("API_BASE_URL:", API_BASE_URL);
    } catch (error) {
        console.error("Failed to fetch configuration:", error);
        API_BASE_URL = "http://localhost:8080/api"; // Fallback
    }
}

export { API_BASE_URL, fetchConfig };
