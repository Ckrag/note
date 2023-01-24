<script>
    import Login from '../components/login.svelte';
    import { Grid, Row, Column, Modal, Button } from "carbon-components-svelte";


    let categoriesMapped = {
        "1": {
            id: "1",
            title: "holiday"
        },
        "2": {
            id: "2",
            title: "work"
        }
    }

    let notesMapped = {
        "1": {
            id: "1",
            title: "title 1",
            content: "information about note 1. woho",
            categoryId: "1"
        },
        "2": {
            id: "2",
            title: "title 2",
            content: "the 2. note is super important",
            categoryId: "2"
        },
        "3": {
            id: "3",
            title: "title 3",
            content: "more blahblah for 3",
            categoryId: "1"
        },
    }

    let noteSelectionId = null;
    let shownNotes = [];

    const showCategory=(categoryId)=>{
        if (!(categoryId in categoriesMapped)) {
            throw new Error(`Category ${categoryId} not loaded`);
        }

        shownNotes = Object.values(notesMapped)
            .filter((note) => note.categoryId == categoryId)
    };

    const showComment=(noteId)=>{
        if (!(noteId in notesMapped)) {
            throw new Error(`Note ${noteId} not loaded`);
        }
        noteSelectionId = noteId;
    };
    

    // https://carbon-components-svelte.onrender.com/components/
</script>

<div style="margin-bottom: 20px;">
    <Login />
</div>
<Grid fullWidth>
    <Row>
        {#each Object.entries(categoriesMapped) as [_, category]}
            <Button on:click={() => showCategory(category.id)} >{category.title}</Button>
        {/each}
    </Row>
    <Row>
        <Column sm={1} md={1} lg={2}>
            {#each shownNotes as note}
                <Button on:click={() => showComment(note.id)} >{note.title}</Button>
            {/each}
        </Column>
        <Column sm={1} md={1} lg={8}>
            {#if noteSelectionId !== null }
                <h1>{notesMapped[noteSelectionId].title}</h1>
                <p>{notesMapped[noteSelectionId].content}</p>
            {:else}
                <p>No notes created</p>
            {/if}
        </Column>
    </Row>
</Grid>
