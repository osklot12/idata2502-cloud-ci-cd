import { get } from 'svelte/store';
import {
    token,
    setToken,
    clearToken,
    setUserId,
    clearUserId,
    setUsername,
    clearUsername
} from './stores/authStore.js';
import {ApiInterface} from "./services/api/apiInterface.js";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

const DEFAULT_HEADERS = { "Content-Type": "application/json" };

function createHeaders(additionalHeaders = {}) {
    return { ...DEFAULT_HEADERS, ...additionalHeaders };
}

async function handleResponse(response, defaultErrMsg = "Request failed") {
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || defaultErrMsg);
    }
    return await response.json();
}

function setUserAuthInfo(token, userId, username) {
    setToken(token);
    setUserId(userId);
    setUsername(username);
}

function responseContainsAuthInfo(data) {
    return data.token && data.userId && data.username;
}

export class SpringApi extends ApiInterface {
    async login(username, password) {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: createHeaders(),
            body: JSON.stringify({ username, password }),
        });

        const data = await handleResponse(response, "Cannot log in");

        if (!responseContainsAuthInfo(data)) {
            throw new Error("The response does not contain authorization information");
        }

        setUserAuthInfo(data.token, data.userId, data.username);
        return { success: true, message: data.message };
    }

    async register(username, email, password) {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: createHeaders(),
            body: JSON.stringify({ username, email, password }),
        });

        const data = await handleResponse(response, "Cannot register user");

        if (!responseContainsAuthInfo(data)) {
            throw new Error("The response does not contain authorization information");
        }

        setUserAuthInfo(data.token, data.userId, data.username);
        return { success: true, message: data.message };
    }

    logout() {
        clearToken();
        clearUserId();
        clearUsername();
    }

    async getTasks() {
        const jwtToken = get(token);

        const response = await fetch(`${API_BASE_URL}/tasks`, {
            method: "GET",
            headers: createHeaders({ "Authorization": `Bearer ${jwtToken}` }),
        });

        return await handleResponse(response, "Cannot fetch tasks");
    }

    async createTask(task) {
        const jwtToken = get(token);

        const response = await fetch(`${API_BASE_URL}/tasks`, {
            method: "POST",
            headers: createHeaders({ "Authorization": `Bearer ${jwtToken}` }),
            body: JSON.stringify(task),
        });

        return await handleResponse(response, "Cannot create task");
    }

    async updateTask(task) {
        const jwtToken = get(token);

        const response = await fetch(`${API_BASE_URL}/tasks/${task.id}`, {
            method: "PUT",
            headers: createHeaders({ "Authorization": `Bearer ${jwtToken}` }),
            body: JSON.stringify(task),
        });

        return await handleResponse(response, `Cannot update task with id ${task.id}`);
    }

    async deleteTask(taskId) {
        const jwtToken = get(token);

        const response = await fetch(`${API_BASE_URL}/tasks/${taskId}`, {
            method: "DELETE",
            headers: createHeaders({ "Authorization": `Bearer ${jwtToken}` }),
        });

        return await handleResponse(response, `Cannot delete task with id ${taskId}`);
    }

    async getUserByUsername(username) {
        const jwtToken = get(token);

        const response = await fetch(`${API_BASE_URL}/users/${username}`, {
            method: "GET",
            headers: createHeaders({ "Authorization": `Bearer ${jwtToken}` }),
        });

        return await handleResponse(response, `Cannot find user with username: ${username}`);
    }
}
