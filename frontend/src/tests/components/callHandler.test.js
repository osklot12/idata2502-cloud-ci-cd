import {describe, it, expect} from "vitest";
import {token, userId} from "../../stores/authStore.js";
import {get} from "svelte/store";
import {CallHandler} from "../../services/callHandler.js";
import {TestApi} from "../../services/api/testApi.js";

describe("Tests logging in", () => {
    it("should set the token", async () => {
        const testApi = new TestApi();
        const callHandler = new CallHandler(testApi);

        await callHandler.login(testApi.mockUsername, testApi.mockPassword);

        expect(get(token)).toBe(testApi.mockToken);
    });

    it("should set the userid", async () => {
        const testApi = new TestApi();
        const callHandler = new CallHandler(testApi);

        await callHandler.login(testApi.mockUsername, testApi.mockPassword);

        expect(get(userId)).toBe(testApi.mockId);
    });
});
