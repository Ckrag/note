/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import java.util.Arrays;
import java.util.List;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.AuthRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Auth extends TableImpl<AuthRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.auth</code>
     */
    public static final Auth AUTH = new Auth();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AuthRecord> getRecordType() {
        return AuthRecord.class;
    }

    /**
     * The column <code>public.auth.id</code>.
     */
    public final TableField<AuthRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.auth.encrypted_password</code>.
     */
    public final TableField<AuthRecord, String> ENCRYPTED_PASSWORD = createField(DSL.name("encrypted_password"), SQLDataType.VARCHAR.nullable(false), this, "");

    private Auth(Name alias, Table<AuthRecord> aliased) {
        this(alias, aliased, null);
    }

    private Auth(Name alias, Table<AuthRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.auth</code> table reference
     */
    public Auth(String alias) {
        this(DSL.name(alias), AUTH);
    }

    /**
     * Create an aliased <code>public.auth</code> table reference
     */
    public Auth(Name alias) {
        this(alias, AUTH);
    }

    /**
     * Create a <code>public.auth</code> table reference
     */
    public Auth() {
        this(DSL.name("auth"), null);
    }

    public <O extends Record> Auth(Table<O> child, ForeignKey<O, AuthRecord> key) {
        super(child, key, AUTH);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<AuthRecord, Integer> getIdentity() {
        return (Identity<AuthRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<AuthRecord> getPrimaryKey() {
        return Keys.AUTH_PKEY;
    }

    @Override
    public List<ForeignKey<AuthRecord, ?>> getReferences() {
        return Arrays.asList(Keys.AUTH__AUTH_ID_FKEY);
    }

    private transient Users _users;

    /**
     * Get the implicit join path to the <code>public.users</code> table.
     */
    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.AUTH__AUTH_ID_FKEY);

        return _users;
    }

    @Override
    public Auth as(String alias) {
        return new Auth(DSL.name(alias), this);
    }

    @Override
    public Auth as(Name alias) {
        return new Auth(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Auth rename(String name) {
        return new Auth(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Auth rename(Name name) {
        return new Auth(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
