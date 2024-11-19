<script>
    import TaskScroll from './TaskScroll.svelte'
    import Dashboard from "./Dashboard.svelte";
    import {api} from "../../main.js";

    function handleLogout() {
        api.logout();
    }
</script>

<div class="workspace">
    <div class="dashboard-container">
        <Dashboard/>
    </div>
    <div class="tools-and-tasks">
        <div class="toolbar">
            <button class="tool-btn" on:click={handleLogout}>
                <span class="material-symbols-outlined">logout</span>
            </button>
        </div>
        <div class="task-scroll-container">

            <TaskScroll/>
        </div>
    </div>

</div>

<style>
    .workspace {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 100vh;
    }

    .dashboard-container {
        background-color: var(--main-grey);
    }

    .tools-and-tasks {
        display: flex;
        flex-direction: column;
        flex: 1;
        background-color: var(--secondary-grey);
    }

    .toolbar {
        display: flex;
        flex-direction: row;
        justify-content: right;
        padding: 20px;
    }

    .tool-btn {
        background-color: transparent;
        border: none;
        outline: none;
        color: var(--secondary-text-color);
    }

    .task-scroll-container {
        display: flex;
        flex-direction: column;
        padding: 100px 0;
        flex: 1;
        overflow-y: scroll;
    }
</style>