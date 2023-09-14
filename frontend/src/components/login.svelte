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

    const handleSubmit = async (_) => {
        console.log(email, password);
        let logindata = await api.login(email, password)
        if (logindata == null) {
            console.log("Error during login")
        } else {
            let orgData = await api.getUserOrgs(logindata.access_token)
            if (orgData == null) {
                console.log("Error loading orgs")
                sessionProps.setSelectedOrgId(null)
            } else {
                if (orgData.organizations.filter(org => org.orgId === sessionProps.getSelectedOrgId()).length === 0) {
                    sessionProps.setSelectedOrgId(orgData.organizations[0].orgId)
                }
            }
            auth.updateAccessToken(logindata.access_token, logindata.expires_in, email)
            EventBus.get().onLogin(logindata.access_token)
        }
        open = false
    }
    const navigateToSignup = () => {
        console.log("Navigated to signup, not impl. yet!")
    };


</script>

<Button on:click={() => open = true}>Login</Button>

<Modal
        bind:open
        modalHeading="Login"
        primaryButtonText="Confirm"
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
    <p>
        Don't have an account?
        <strong class="link" on:click="{navigateToSignup}">Sign up</strong>
    </p>
</Modal>