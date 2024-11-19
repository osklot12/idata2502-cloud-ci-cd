export class ApiInterface {
    async login(username, password) {
        throw new Error("login() must be implemented.");
    }

    async register(username, email, password) {
        throw new Error("register() must be implemented.")
    }

    logout() {
        throw new Error("logout() must be implemented.")
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