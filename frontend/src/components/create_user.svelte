<script>
    import {Button, Modal, PasswordInput, TextInput} from "carbon-components-svelte";
    import {Auth} from "../modules/auth.js";
    import {EventBus} from "../modules/eventBus.js";
    import { onMount } from 'svelte';
    import {Api} from "../modules/api.js";
    import {SessionProperties} from "../modules/sessionProperties.js";

    let auth
    let sessionProps
    const api = new Api()
	onMount(async () => {
		auth = new Auth(localStorage)
        sessionProps = new SessionProperties(localStorage)
	});

    let email;
    let password;
    let open = false;

    const handleSubmit = async (e) => {
        console.log(email, password);
        let createData = await api.createUser(email, password)
        if (createData == null) {
            console.log("Error during creation")
            return
        }
        let loginData = await api.login(email, password)
        if (loginData == null) {
            console.log("Error during login")
        } else {
            let orgData = await api.getUserOrgs(loginData.access_token)
            if (orgData == null) {
                console.log("Error loading orgs")
                sessionProps.setSelectedOrgId(null)
            } else {
                if (orgData.organizations.filter(org => org.orgId === sessionProps.getSelectedOrgId()).length === 0) {
                    sessionProps.setSelectedOrgId(orgData.organizations[0].orgId)
                }
            }
            auth.updateAccessToken(loginData.access_token, loginData.expires_in, email)
            EventBus.get().onLogin(loginData.access_token)
        }
        open = false
    }


</script>

<Button on:click={() => open = true}>Create User</Button>

<Modal
        bind:open
        modalHeading="Create User"
        primaryButtonText="Create"
        secondaryButtonText="Cancel"
        on:click:button--secondary={() => (open = false)}
        on:open
        on:close
        on:submit={handleSubmit}
>
    <form on:submit|preventDefault="{handleSubmit}">
        <TextInput type="email" bind:value="{email}" labelText="Email " placeholder="your@mail.com"/>
        <PasswordInput bind:value="{password}" labelText="Password" placeholder="Enter password..."/>
    </form>
</Modal>