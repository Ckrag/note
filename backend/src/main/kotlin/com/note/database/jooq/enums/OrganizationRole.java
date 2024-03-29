/*
 * This file is generated by jOOQ.
 */
package jooq.enums;


import jooq.Public;

import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum OrganizationRole implements EnumType {

    USER("USER"),

    ADMIN("ADMIN");

    private final String literal;

    private OrganizationRole(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return getSchema().getCatalog();
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public String getName() {
        return "organization_role";
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Lookup a value of this EnumType by its literal
     */
    public static OrganizationRole lookupLiteral(String literal) {
        return EnumType.lookupLiteral(OrganizationRole.class, literal);
    }
}
