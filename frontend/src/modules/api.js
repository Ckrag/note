export class Api {

    #selectedOrg = null

    #noteApiFetch(method, route, token = null, body = null) {
        const config = {
            method: method,
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                "Access-Control-Allow-Origin": "*"
            },
        }
        if (token !== null) {
            config.headers['Authorization']=`Bearer ${token}`
        }
        if (body !== null) {
            config.body = JSON.stringify(body)
        }
        return fetch(`http://0.0.0.0:8080${route}`, config)
    }

    async login(email, password) {
        const payload = {
            username: email,
            password: password
        }
        const promise = await this.#noteApiFetch('POST', "/login", null, payload)
        if (!promise.ok) {
            console.error("/login", promise)
            return null
        } else {
            return await promise.json()
        }
    }

    async createUser(email, password) {
        const body = {
            email: email,
            raw_password: password
        }
        const promise = await this.#noteApiFetch("POST", "/user/create", null, body)
        if (!promise.ok) {
            console.error("/user/create", promise)
            return null
        } else {
            return await promise.json()
        }
    }

    async getUserOrgs(token) {
        const promise = await this.#noteApiFetch("GET", "/user/organizations", token)
        if (!promise.ok) {
            console.error("/user/organizations", promise)
            return null
        } else {
            return await promise.json()
        }
    }

    async getCategories(token, orgId) {
        const promise = await this.#noteApiFetch("GET", `/org/${orgId}/category/all`, token)
        if (!promise.ok) {
            console.error(`/org/${orgId}/category/all`, promise)
            return null
        } else {
            return await promise.json()
        }
    }

    async getNotes(token, orgId, categoryId) {
        const promise = await this.#noteApiFetch("GET", `/org/${orgId}/note/category/${categoryId}`, token)
        if (!promise.ok) {
            console.error(`/org/${orgId}/note/category/${categoryId}`, promise)
            return null
        } else {
            return await promise.json()
        }
    }

    async createNote(token, orgId, categoryId, noteTitle, noteContent) {
        const body = {
            title: noteTitle,
            content: noteContent,
            category_id: categoryId
        }
        const promise = await this.#noteApiFetch("POST", `/org/${orgId}/note/create`, token, body)
        if (!promise.ok) {
            console.error(`/org/${orgId}/note/create`, promise)
            return null
        } else {
            return await promise.json()
        }
    }

    async createCategory(token, orgId, categoryTitle, categoryDescription) {
        const body = {
            title: categoryTitle,
            description: categoryDescription
        }
        const promise = await this.#noteApiFetch("POST", `/org/${orgId}/category/create`, token, body)
        if (!promise.ok) {
            console.error(`/org/${orgId}/category/create`, promise)
            return null
        } else {
            return await promise.json()
        }
    }
}