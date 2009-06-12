package com.infusion.util.domain.event.hibernate;

import org.hibernate.Criteria;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Jun 1, 2009
 * Time: 11:46:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaContext {
    public final Criteria criteria;
    public final Class entityClass;

    public CriteriaContext(Criteria criteria, Class entityClass) {
        this.criteria = criteria;
        this.entityClass = entityClass;
    }
}
