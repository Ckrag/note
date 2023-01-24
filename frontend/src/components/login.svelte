<script>
      import {Modal, Button, PasswordInput, TextInput } from "carbon-components-svelte";

let email;
let password;
let open = false;

const handleSubmit=(e)=>{
       let loginFields={email,password};
	      console.log(loginFields);
    }
const navigateToSignup=()=>{};

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
  on:submit
>
<form on:submit|preventDefault="{handleSubmit}">
  <!--
  <input
    class="form-field"
    bind:value="{email}"
    type="email"
    placeholder="Email"
  />
  <input
    class="form-field"
    bind:value="{password}"
    type="password"
    placeholder="Password"
  />
  -->
  <TextInput type="email" bind:value="{email}" labelText="Email " placeholder="your@mail.com" />
  <PasswordInput bind:value="{password}" labelText="Password" placeholder="Enter password..." />

</form>
<p>
  Don't have an account?
  <strong class="link" on:click="{navigateToSignup}">Sign up</strong>
</p>
</Modal>