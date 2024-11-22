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