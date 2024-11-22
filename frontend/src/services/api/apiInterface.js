import {clearToken, clearUserId, clearUsername} from "../../stores/authStore.js";

export class ApiInterface {
    async init() {
        throw new Error("init() must be implemented");
    }

    async login(username, password) {
        throw new Error("login() must be implemented.");
    }

    async register(username, email, password) {
        throw new Error("register() must be implemented.")
    }

    logout() {
        clearToken();
        clearUserId();
        clearUsername();
    }

    async getTasks() {
        throw new Error("getTasks() must be implemented.")
    }

    async createTask(task) {
        throw new Error("createTask() must be implemented.")
    }

    async updateTask(task) {
        throw new Error("updateTask() must be implemented.")
    }

    async deleteTask(taskId) {
        throw new Error("deleteTask() must be implemented.")
    }

    async getUserByUsername(username) {
        throw new Error("getUserByUsername() must be implemented.")
    }
}