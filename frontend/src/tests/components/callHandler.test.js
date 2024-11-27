import {describe, it, expect, beforeEach} from "vitest";
import {token, userId, username} from "../../stores/authStore.js";
import {get} from "svelte/store";
import {CallHandler} from "../../services/callHandler.js";
import {TestApi} from "../../services/api/testApi.js";

let testApi;
let callHandler;

describe("Tests logging in", () => {
    beforeEach(() => {
        testApi = new TestApi();
        callHandler = new CallHandler(testApi);
    });

    it("should set the token", async () => {
        await callHandler.login(testApi.mockUsername, testApi.mockPassword);
        expect(get(token)).toBe(testApi.mockToken);
    });

    it("should set the userid", async () => {
        await callHandler.login(testApi.mockUsername, testApi.mockPassword);
        expect(get(userId)).toBe(testApi.mockId);
    });

    it("should set the username", async () => {
        await callHandler.login(testApi.mockUsername, testApi.mockPassword);
        expect(get(username)).toBe(testApi.mockUsername);
    })
});

describe("Tests logging out", () => {
    beforeEach(() => {
        testApi = new TestApi();
        callHandler = new CallHandler(testApi);
    });

    it("should clear token", async () => {
        await callHandler.logout();
        expect(get(token)).toBe(null);
    });

    it("should clear userId", async () => {
        await callHandler.logout();
        expect(get(userId)).toBe(null);
    })

    it("should clear username", async () => {
        await callHandler.logout();
        expect(get(username)).toBe(null);
    })
});

describe("Tests user registration", () => {
    beforeEach(() => {
        testApi = new TestApi();
        callHandler = new CallHandler(testApi);
    });

    it("should register successfully with valid data", async () => {
        const username = "newUser";
        const email = "newuser@example.com";
        const password = "securePassword";

        const response = await callHandler.register(username, email, password);

        expect(response.success).toBe(true); 
        expect(response.message).toBe("Registration successful"); 
        expect(response.token).toBe("mock-token"); 
        expect(response.userId).toBe(testApi.mockUser.id); 
        expect(response.username).toBe(username); 
    });

    it("should throw an error for missing registration fields", async () => {
        await expect(callHandler.register("", "", ""))
            .rejects
            .toThrow("Invalid registration details");
    });

    it("should handle duplicate username", async () => {
        
        testApi.register = async () => {
            throw new Error("Username already exists");
        };

        await expect(callHandler.register("testUsername", "email@example.com", "password"))
            .rejects
            .toThrow("Username already exists");
    });
}); 

describe("Tests task", () => {
    beforeEach(() => {
        testApi = new TestApi();
        callHandler = new CallHandler(testApi);
    });

    it("should retrieve all tasks", async () => {
        const tasks = await callHandler.getTasks();
        expect(tasks).toEqual(testApi.mockTasks);
    });

    it("should create a new task", async () => {
        const newTaskData = {
            header: "New Task",
            description: "This is a new task.",
            status: "pending",
            deadline: new Date().toISOString(),
            assignees: [
                { id: 4, username: "newAssignee", email: "newassignee@example.com" },
            ],
        };

        const createdTask = await callHandler.createTask(newTaskData);

        expect(createdTask.id).toBe(testApi.mockTasks.length); 
        expect(createdTask.header).toBe(newTaskData.header);
        expect(createdTask.description).toBe(newTaskData.description);
        expect(testApi.mockTasks).toContainEqual(createdTask); 
    });

    it("should update an existing task", async () => {
        const updatedTaskData = {
            id: 1,
            header: "Updated Task Header",
            status: "completed",
        };

        const updatedTask = await callHandler.updateTask(updatedTaskData);

        expect(updatedTask.id).toBe(updatedTaskData.id);
        expect(updatedTask.header).toBe(updatedTaskData.header);
        expect(updatedTask.status).toBe(updatedTaskData.status);
        expect(testApi.mockTasks[0]).toEqual(updatedTask); 
    });

    it("should throw an error when updating a non-existent task", async () => {
        const nonExistentTask = { id: 99, header: "Non-existent Task" };

        await expect(callHandler.updateTask(nonExistentTask))
            .rejects
            .toThrow("Task not found");
    });

    it("should delete an existing task", async () => {
        const taskIdToDelete = 1;
        const deletedTask = await callHandler.deleteTask(taskIdToDelete);

        expect(deletedTask.id).toBe(taskIdToDelete);
        expect(testApi.mockTasks.find(task => task.id === taskIdToDelete)).toBeUndefined(); 
    });

    it("should throw an error when deleting a non-existent task", async () => {
        const nonExistentTaskId = 99;

        await expect(callHandler.deleteTask(nonExistentTaskId))
            .rejects
            .toThrow("Task not found");
    });
});

describe("Tests Get User by Username", () => {
    beforeEach(() => {
        testApi = new TestApi();
        callHandler = new CallHandler(testApi);
    });

    it("should return user details when a valid username is provided", async () => {
        const usernameToSearch = "mockUser"; 

        const user = await callHandler.getUserByUsername(usernameToSearch);

        expect(user.id).toBe(testApi.mockUser.id);
        expect(user.username).toBe(testApi.mockUser.username);
        expect(user.email).toBe(testApi.mockUser.email);
    });

    it("should throw an error if user is not found", async () => {
        const invalidUsername = "nonExistentUser";

        await expect(callHandler.getUserByUsername(invalidUsername))
            .rejects
            .toThrow(`Cannot find user with username: ${invalidUsername}`);
    });
});