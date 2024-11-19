export class CallHandler {
    constructor(apiImplementation) {
        this.api = apiImplementation;
    }

    async login(username, password) {
        return this.api.login(username, password);
    }

    async register(username, email, password) {
        return this.api.register(username, email, password);
    }

    logout() {
        this.api.logout();
    }

    async getTasks() {
        return this.api.getTasks();
    }

    async createTask(task) {
        return this.api.createTask(task);
    }

    async updateTask(task) {
        return this.api.updateTask(task);
    }

    async deleteTask(taskId) {
        return this.api.deleteTask(taskId);
    }

    async getUserByUsername(username) {
        return this.api.getUserByUsername(username);
    }
}