/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.NoteRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
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
public class Note extends TableImpl<NoteRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.note</code>
     */
    public static final Note NOTE = new Note();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NoteRecord> getRecordType() {
        return NoteRecord.class;
    }

    /**
     * The column <code>public.note.id</code>.
     */
    public final TableField<NoteRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.note.title</code>.
     */
    public final TableField<NoteRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.note.content</code>.
     */
    public final TableField<NoteRecord, String> CONTENT = createField(DSL.name("content"), SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>public.note.verified</code>.
     */
    public final TableField<NoteRecord, Boolean> VERIFIED = createField(DSL.name("verified"), SQLDataType.BOOLEAN.defaultValue(DSL.field("false", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.note.created</code>.
     */
    public final TableField<NoteRecord, LocalDateTime> CREATED = createField(DSL.name("created"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.note.author_id</code>.
     */
    public final TableField<NoteRecord, Integer> AUTHOR_ID = createField(DSL.name("author_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    private Note(Name alias, Table<NoteRecord> aliased) {
        this(alias, aliased, null);
    }

    private Note(Name alias, Table<NoteRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.note</code> table reference
     */
    public Note(String alias) {
        this(DSL.name(alias), NOTE);
    }

    /**
     * Create an aliased <code>public.note</code> table reference
     */
    public Note(Name alias) {
        this(alias, NOTE);
    }

    /**
     * Create a <code>public.note</code> table reference
     */
    public Note() {
        this(DSL.name("note"), null);
    }

    public <O extends Record> Note(Table<O> child, ForeignKey<O, NoteRecord> key) {
        super(child, key, NOTE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<NoteRecord, Integer> getIdentity() {
        return (Identity<NoteRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<NoteRecord> getPrimaryKey() {
        return Keys.NOTE_PKEY;
    }

    @Override
    public List<ForeignKey<NoteRecord, ?>> getReferences() {
        return Arrays.asList(Keys.NOTE__NOTE_AUTHOR_ID_FKEY);
    }

    private transient Users _users;

    /**
     * Get the implicit join path to the <code>public.users</code> table.
     */
    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.NOTE__NOTE_AUTHOR_ID_FKEY);

        return _users;
    }

    @Override
    public Note as(String alias) {
        return new Note(DSL.name(alias), this);
    }

    @Override
    public Note as(Name alias) {
        return new Note(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Note rename(String name) {
        return new Note(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Note rename(Name name) {
        return new Note(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, String, String, Boolean, LocalDateTime, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
