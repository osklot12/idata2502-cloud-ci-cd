export default class User {
    constructor({ id = null, username = "", email = ""} = {}) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    getDisplayName() {
        return this.username || "Unknown user";
    }
}