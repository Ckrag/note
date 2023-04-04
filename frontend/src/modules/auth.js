export class Auth {
    constructor(storage) {
        this.storage = storage
    }

    updateAccessToken(token, expireInSec) {
        console.log("STORING TOKEN", token)
        this.storage.setItem('access_token', token)
        this.storage.setItem('access_token_expire_at', this.#getNowEpochSec() + expireInSec)
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

    #getNowEpochSec() {
        return (new Date().getTime() / 1000)
    }
}