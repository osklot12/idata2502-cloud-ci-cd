import {ApiInterface} from "./services/api/apiInterface.js";
import {token, userId, username, setToken, setUserId, setUsername, clearToken, clearUserId, clearUsername} from "./stores/authStore.js";
import Task from "./classes/Task.js"
import User from "./classes/User.js"

// generates a twt token for testing purposes
function generateMockToken(expiryInSeconds = 24 * 60 * 60) {
    const header = btoa(JSON.stringify({ alg: "HS256", typ: "JWT" }));
    const payload = btoa(
        JSON.stringify({ exp: Math.floor(Date.now() / 1000) + expiryInSeconds })
    );
    const signature = "dummySignature";
    return `${header}.${payload}.${signature}`;
}

export class TestApi extends ApiInterface {
    constructor() {
        super();
        // generating a new mock token
        this.mockToken = generateMockToken();

        // generating a new expired mock token
        this.mockTokenExpired = generateMockToken(-60);

        // setting a mock id
        this.mockId = 123;

        // setting a mock username
        this.mockUsername = "testUsername";

        // setting a mock password
        this.mockPassword = "testPassword";

        // mock tasks
        this.mockTasks = [
            new Task({
                id: 1,
                header: "Mock Task 1",
                description: "This is a mock task",
                status: "pending",
                deadline: new Date().toISOString(),
                creator: { id: 1, username: "mockCreator", email: "creator@example.com" },
                assignees: [
                    { id: 2, username: "mockAssignee1", email: "assignee1@example.com" },
                ],
            }),
            new Task({
                id: 2,
                header: "Mock Task 2",
                description: "This is another mock task",
                status: "completed",
                deadline: new Date().toISOString(),
                creator: { id: 1, username: "mockCreator", email: "creator@example.com" },
                assignees: [
                    { id: 3, username: "mockAssignee2", email: "assignee2@example.com" },
                ],
            }),
        ];

        // mock user
        this.mockUser = new User({
            id: 1,
            username: "mockUser",
            email: "mock@example.com",
        });
    }

    async login(username, password) {
        if (username === this.mockUsername && password === this.mockPassword) {
            setToken(this.mockToken);
            setUserId(this.mockId);
            setUsername(this.mockUsername);

            return {
                success: true,
                message: "Login successful",
                token: "mock-token",
                userId: this.mockUser.id,
                username: this.mockUser.username,
            };
        } else {
            throw new Error("Invalid username or password");
        }
    }

    async register(username, email, password) {
        if (username && email && password) {
            return {
                success: true,
                message: "Registration successful",
                token: "mock-token",
                userId: this.mockUser.id,
                username: username,
            };
        } else {
            throw new Error("Invalid registration details");
        }
    }

    logout() {

        console.log("Mock logout called");
    }

    async getTasks() {
        return this.mockTasks;
    }

    async createTask(taskData) {
        const newTask = new Task({
            ...taskData,
            id: this.mockTasks.length + 1,
            creator: { id: this.mockUser.id, username: this.mockUser.username },
            createdAt: new Date().toISOString(),
        });
        this.mockTasks.push(newTask);
        return newTask;
    }

    async updateTask(taskData) {
        const taskIndex = this.mockTasks.findIndex(task => task.id === taskData.id);
        if (taskIndex === -1) throw new Error("Task not found");

        const updatedTask = new Task({ ...this.mockTasks[taskIndex], ...taskData });
        this.mockTasks[taskIndex] = updatedTask;
        return updatedTask;
    }

    async deleteTask(taskId) {
        const taskIndex = this.mockTasks.findIndex(task => task.id === taskId);
        if (taskIndex === -1) throw new Error("Task not found");

        const [removedTask] = this.mockTasks.splice(taskIndex, 1);
        return removedTask;
    }

    async getUserByUsername(username) {
        if (username === this.mockUser.username) {
            return this.mockUser;
        } else {
            throw new Error(`Cannot find user with username: ${username}`);
        }
    }
}
