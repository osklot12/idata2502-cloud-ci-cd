import User from "./User.js";

export default class Task {
    constructor({
                    id = null,
                    header = "",
                    description = "",
                    status = "pending",
                    deadline = null, // Allow null initially
                    createdAt = null, // Allow null initially
                    creator = {},
                    assignees = []
                } = {}) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.status = status;
        // Ensure deadline is converted to Date or set to null
        this.deadline = deadline ? new Date(deadline) : null;
        // Ensure createdAt is converted to Date or set to current date
        this.createdAt = createdAt ? new Date(createdAt) : new Date();
        this.creator = new User(creator);
        this.assignees = assignees.map(assignee => new User(assignee));
    }

    updateStatus(newStatus) {
        this.status = newStatus;
    }

    getFormattedDeadline() {
        // Format deadline as a readable date or provide a fallback
        return this.deadline ? this.deadline.toLocaleDateString() : "No deadline";
    }

    getFormattedCreatedAt() {
        // Format createdAt as a readable datetime
        return this.createdAt ? this.createdAt.toLocaleString() : "";
    }

    getCreatorName() {
        return this.creator.getDisplayName();
    }

    getAssigneesNames() {
        return this.assignees.map(assignee => assignee.username).join(", ");
    }
}
