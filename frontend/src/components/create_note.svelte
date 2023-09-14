<script>

    import {Button, Dropdown, TextArea, TextInput} from "carbon-components-svelte";
    import {Api} from "../modules/api.js";
    import {onMount} from "svelte";

    export let orgId;
    export let accessToken;
    export let onNewCreated = () => {
    }
    let selectableCategories = []

    let selectedCategoryId
    let newNoteText
    let newNoteTitle
    const loadCategories = (accessToken, orgId) => {
        const api = new Api()
        let categories = api.getCategories(accessToken, orgId)
        categories.then((data) => {
            if (data.categories.length === 0) {
                return
            }
            selectableCategories = data.categories.map((category) => {
                return {
                    id: category.id,
                    text: category.title
                }
            })
            selectedCategoryId = selectableCategories[0].id
        })
    }

    const createNote = () => {
        console.log("Creating note for category", selectedCategoryId)
        const api = new Api()
        if (api.createNote(accessToken, orgId, selectedCategoryId, newNoteTitle, newNoteText) === null) {
            console.log("Failed to create note...")
        } else {
            onNewCreated()
        }
    }

    onMount(() => {
        loadCategories(accessToken, orgId)
    })

</script>
<div style="width: 400px">
    <h3>Create new note</h3>
    <Dropdown
            titleText="Select a category"
            on:select={(item) => {selectedCategoryId = item.detail.selectedId}}
            items={selectableCategories}
    />
    <TextInput bind:value={newNoteTitle} placeholder="Write a title"/>
    <TextArea bind:value={newNoteText} placeholder="Write a note"/>
    <Button on:click={() => createNote()}>Create Note</Button>
</div>