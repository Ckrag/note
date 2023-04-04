<script>

    import {Button, Dropdown, TextArea, TextInput} from "carbon-components-svelte";
    import {Api} from "../modules/api.js";
    import {onMount} from "svelte";

    export let orgId;
    export let accessToken;
    export let onNewCreated = () => {
    }

    let newCategoryTitle
    let newCategoryDescription

    const createNote = () => {
        const api = new Api()
        if (api.createCategory(accessToken, orgId, newCategoryTitle, newCategoryDescription) === null) {
            console.log("Failed to create note...")
        } else {
            onNewCreated()
        }
    }

</script>
<div style="width: 400px">
    <h3>Create new category</h3>
    <TextInput bind:value={newCategoryTitle} placeholder="Write a title"/>
    <TextArea bind:value={newCategoryDescription} placeholder="Write a description"/>
    <Button on:click={() => createNote()}>Create Category</Button>
</div>