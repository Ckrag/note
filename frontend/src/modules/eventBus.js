export class EventBus {

    static #BUS = null;

    static get() {
        if (this.#BUS == null) {
            this.#BUS = new EventBus()
        }
        return this.#BUS;
    }

    #loginListeners = new Map()

    listenForAuthEvents(id, onLogin, onLogout) {
        this.#loginListeners.set(id, new AuthListener(onLogin, onLogout))
        console.log(`${id} started listening`)
    }

    stopListeningForAuthEvents(id) {
        this.#loginListeners.delete(id)
        console.log(`${id} stopped listening`)
    }

    onLogin(accessToken) {
        console.log(`User logged in`)
        this.#loginListeners.forEach((listener, listenerId) => {
            listener.onLogin(accessToken)
        })
    }

    onLogout() {
        console.log(`User logged out`)
        this.#loginListeners.forEach((listener, listenerId) => {
            listener.onLogout()
        })
    }
}

class AuthListener {

    onLogin = () => {
    }
    onLogout = () => {
    }

    constructor(onLogin, onLogout) {
        this.onLogin = onLogin
        this.onLogout = onLogout
    }
}