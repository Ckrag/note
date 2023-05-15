export class Auth {
    constructor(storage) {
        this.storage = storage
    }

    updateAccessToken(token, expireInSec, email) {
        console.log("STORING TOKEN", token)
        this.storage.setItem('access_token', token)
        this.storage.setItem('access_token_username', email)
        this.storage.setItem('access_token_expire_at', this.#getNowEpochSec() + expireInSec)
    }

    clearAccessToken() {
        console.log("CLEARING TOKEN")
        this.storage.removeItem('access_token')
        this.storage.removeItem('access_token_username')
        this.storage.removeItem('access_token_expire_at')
    }

    getValidCachedAccessToken() {
        const expireInSec = this.storage.getItem('access_token_expire_at')
        const deltaSec = 60
        if (expireInSec == null || expireInSec + deltaSec < this.#getNowEpochSec()) {
            console.log("FAILED RETRIEVING TOKEN", expireInSec, expireInSec + deltaSec > this.#getNowEpochSec())
            return null
        }
        let token = this.storage.getItem("access_token")
        console.log("RETRIEVING TOKEN", token)
        return token
    }

    isLoggedIn() {
        return this.getValidCachedAccessToken() !== null
    }

    getEmail() {
        return this.storage.getItem("access_token_username")
    }

    #getNowEpochSec() {
        return (new Date().getTime() / 1000)
    }
}