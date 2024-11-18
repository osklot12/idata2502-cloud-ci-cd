import {get} from 'svelte/store'
import {token, setToken, clearToken, setUserId, clearUserId, setUsername, clearUsername} from '../../stores/authStore.js'

const API_BASE_URL = "http://localhost:8080/api"

const DEFAULT_HEADERS = {"Content-Type": "application/json"}

// function to log in a user
export async function login(username, password) {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: createHeaders(),
        body: JSON.stringify({username, password})
    });

    const data = await handleResponse(response, "Cannot log in");

    handleUserAuthorization(data);
    return {success: true, message: data.message};
}

function createHeaders(additionalHeaders = {}) {
    return { ...DEFAULT_HEADERS, ...additionalHeaders };
}

// function to register a new user
export async function register(username, email, password) {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: createHeaders(),
        body: JSON.stringify({username, email, password})
    });

    const data = await handleResponse(response, "Cannot register user");

    handleUserAuthorization(data);
    return {success: true, message: data.message};
}

// handles the response from authorization functions
function handleUserAuthorization(data) {
    if (!responseContainsAuthInfo(data)) {
        throw new Error("The response does not contain authorization information");
    }

    setUserAuthInfo(data.token, data.userId, data.username);
}

function setUserAuthInfo(token, userId, username) {
    setToken(token);
    setUserId(userId);
    setUsername(username)
}

function responseContainsAuthInfo(data) {
    return data.token && data.userId && data.username;
}

// function to log out the user
export function logout() {
    clearToken();
    clearUserId();
    clearUsername();
}

// function to fetch tasks
export async function getTasks() {
    const jwtToken = get(token);

    const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: "GET",
        headers: createHeaders({"Authorization": `Bearer ${jwtToken}`})
    });

    return await handleResponse(response, "Cannot fetch tasks")
}

// function to create a new task
export async function createTask(task) {
    const jwtToken = get(token);

    const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: "POST",
        headers: createHeaders({"Authorization": `Bearer ${jwtToken}`}),
        body: JSON.stringify(task)
    });

    return await handleResponse(response, "Cannot create task");
}

// function to update an existing task
export async function updateTask(task) {
    const jwtToken = get(token);

    const response = await fetch(`${API_BASE_URL}/tasks/${task.id}`, {
        method: "PUT",
        headers: createHeaders({"Authorization": `Bearer ${jwtToken}`}),
        body: JSON.stringify(task)
    });

    return await handleResponse(response, "Cannot update task " + task.id);
}

// handles the response of a request
async function handleResponse(response, defaultErrMsg = "Request failed") {
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || defaultErrMsg);
    }

    return await response.json();
}

// function to get user by username
export async function getUserByUsername(username) {
    const jwtToken = get(token);

    const response = await fetch(`${API_BASE_URL}/users/${username}`, {
        method: "GET",
        headers: createHeaders({"Authorization": `Bearer ${jwtToken}`})
    });

    return await handleResponse(response, "Cannot find user with username: " + username);
}