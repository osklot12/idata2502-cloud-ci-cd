import User from "./User.js";

export default class Task {
    constructor({
        id = null,
        header = "",
        description = "",
        status = "",
        deadline = null,
        createdAt = null,
        creator = null,
        assignees = []
                } = {}) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.status = status;
        this.deadline = deadline ? new Date(deadline) : null;
        this.createdAt = createdAt ? new Date(createdAt) : null;
        this.creator = new User(creator);
        this.assignees = assignees.map(assignee => new User(assignee));
    }

    updateStatus(newStatus) {
        this.status = newStatus;
    }

    getFormattedDeadline() {
        return this.deadline ? this.deadline.toLocaleDateString() : 'No deadline';
    }

    getFormattedCreatedAt() {
        return this.createdAt ? this.createdAt.toLocaleString() : '';
    }

    getCreatorName() {
        return this.creator.getDisplayName();
    }

    getAssigneesNames() {
        return this.assignees.map(assignee => assignee.username).join(', ');
    }
}