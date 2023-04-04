<script>
    import Login from '../components/login.svelte';
    import CreateNote from '../components/create_note.svelte'
    import CreateCategory from '../components/create_category.svelte'
    import {Button, Column, Grid, Row} from "carbon-components-svelte";

    import {EventBus} from "../modules/eventBus.js";
    import {onDestroy, onMount} from "svelte";
    import {Api} from "../modules/api.js";
    import {SessionProperties} from "../modules/sessionProperties.js";
    import {Auth} from "../modules/auth.js";

    let sessionProps
    let auth
    const onLogin = (accessToken) => {
        console.log("Svelte page heard about login")
        loadCategories(accessToken)
    }
    const onLogout = () => {
        console.log("Svelte page heard about logout")
    }

    const onNewNoteCreated = () => {
        console.log("We were just told a new note was created")
    }

    const onNewCategoryCreated = () => {
        console.log("We were just told a new category was created")
    }

    const loadCategories = (accessToken) => {
        const api = new Api()
        let categories = api.getCategories(accessToken, sessionProps.getSelectedOrgId())
        categories.then((data) => {
            let m = {}
            data.categories.forEach((category) => {
                m[category.id] = category
            })
            categoriesMapped = m
        })
    }

    onMount(() => {
        sessionProps = new SessionProperties(localStorage)
        auth = new Auth(localStorage)
        EventBus.get().listenForAuthEvents("my-id", onLogin, onLogout)
        if (auth.isLoggedIn()) {
            console.log("Already logged in!", auth.getValidCachedAccessToken())
            loadCategories(auth.getValidCachedAccessToken())
        }
    });

    onDestroy(() => {
        EventBus.get().stopListeningForAuthEvents("my-id")
    })

    let categoriesMapped = {}
    let notesMapped = {}

    let noteSelectionId = null;
    let shownNotes = []

    const showCategory = (categoryId) => {
        noteSelectionId = null
        if (!(categoryId in categoriesMapped)) {
            throw new Error(`Category ${categoryId} not loaded`);
        }

        const api = new Api()
        let noteData = api.getNotes(auth.getValidCachedAccessToken(), sessionProps.getSelectedOrgId(), categoryId)
        let n = {}
        noteData.then((data) => {
            data.notes.forEach((note) => {
                n[note.id] = note
            })
            notesMapped = n
            shownNotes = data.notes
        })
    };

    const showComment = (noteId) => {
        if (!(noteId in notesMapped)) {
            throw new Error(`Note ${noteId} not loaded`);
        }
        noteSelectionId = noteId;
    };


    // https://carbon-components-svelte.onrender.com/components/
</script>

<div style="margin-bottom: 20px;">
    {#if auth && auth.isLoggedIn()}
        <Button>Logout</Button>
    {:else}
        <Login/>
    {/if}
</div>

{#if auth && auth.isLoggedIn()}
    <div>
        <Grid fullWidth>
            <Row>
                {#each Object.entries(categoriesMapped) as [_, category]}
                    <Button on:click={() => showCategory(category.id)}>{category.title}</Button>
                {/each}
            </Row>
            <Row>
                <Column sm={1} md={1} lg={2}>
                    {#each shownNotes as note}
                        <Button on:click={() => showComment(note.id)}>{note.title}</Button>
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

        <CreateNote
                orgId={sessionProps.getSelectedOrgId()}
                accessToken={auth.getValidCachedAccessToken()}
                onNewCreated={onNewNoteCreated}
        />
        <CreateCategory
                orgId={sessionProps.getSelectedOrgId()}
                accessToken={auth.getValidCachedAccessToken()}
                onNewCreated={onNewCategoryCreated}
        />
    </div>
{/if}