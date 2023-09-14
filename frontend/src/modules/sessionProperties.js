export class SessionProperties {

    constructor(storage) {
        this.storage = storage
    }

    #props = {
        org_id: null
    }

    #save() {
        this.storage.setItem('session_properties', JSON.stringify(this.#props))
    }

    #load() {
        const data = this.storage.getItem('session_properties')
        if (data === null) {
            return {}
        }
        this.#props = JSON.parse(data)
    }

    clearLocalProps() {
        // TODO:
        this.storage.setItem('session_properties', "{}")
        //this.#props = {}
        this.#load()
    }

    #getPropSafely(propName) {
        if (propName in this.#props) {
            return this.#props[propName]
        }
        return null
    }

    setSelectedOrgId(id) {
        console.log("Org ID set", id)
        this.#props['org_id'] = id
        this.#save()
    }

    getSelectedOrgId() {
        this.#load()
        let id = this.#getPropSafely("org_id")
        console.log("getting selected id", id)
        return id
    }

}