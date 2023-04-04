CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    verified BOOL DEFAULT FALSE,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS auth (
    id SERIAL PRIMARY KEY REFERENCES users (id),
    encrypted_password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS note_category (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR
);

CREATE TYPE note_category_access AS ENUM ('WRITE', 'READ');

CREATE TABLE IF NOT EXISTS note (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content VARCHAR NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_id INTEGER REFERENCES users (id) NOT NULL,
    category_id INTEGER REFERENCES note_category (id) NOT NULL
);
CREATE TYPE note_access AS ENUM ('WRITE', 'READ');

CREATE TABLE IF NOT EXISTS organization (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    name VARCHAR(200)
);
CREATE INDEX IF NOT EXISTS organization_name ON organization(name);

CREATE TABLE IF NOT EXISTS user_notecategory_ownership (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id INTEGER REFERENCES users (id),
    category_id INTEGER REFERENCES note_category (id),
    organization_id INTEGER REFERENCES organization (id),
    note_category_access note_category_access,
    UNIQUE (user_id, category_id)
);

CREATE TYPE organization_role AS ENUM ('USER', 'ADMIN');
CREATE TABLE IF NOT EXISTS user_organization_membership (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id INTEGER REFERENCES users (id),
    organization_id INTEGER REFERENCES organization (id),
    role organization_role,
    UNIQUE (user_id, organization_id, role)
);
CREATE INDEX IF NOT EXISTS user_organization_membership_user_id ON user_organization_membership(user_id);
CREATE INDEX IF NOT EXISTS user_organization_membership_role ON user_organization_membership(role);
CREATE INDEX IF NOT EXISTS user_organization_membership_organization_id ON user_organization_membership(organization_id);

CREATE TABLE IF NOT EXISTS user_note_ownership (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id INTEGER REFERENCES users (id),
    note_id INTEGER REFERENCES note (id),
    organization_id INTEGER REFERENCES organization (id),
    note_access note_access,
    UNIQUE (user_id, note_id)
);

CREATE INDEX IF NOT EXISTS user_note_ownership_user_id ON user_note_ownership(user_id);
CREATE INDEX IF NOT EXISTS user_note_ownership_note_id ON user_note_ownership(note_id);
CREATE INDEX IF NOT EXISTS user_note_ownership_access ON user_note_ownership(note_access);