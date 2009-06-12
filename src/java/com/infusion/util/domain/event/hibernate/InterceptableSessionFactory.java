package com.infusion.util.domain.event.hibernate;

import org.hibernate.*;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.hibernate.cfg.Settings;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.cache.QueryCache;
import org.hibernate.cache.UpdateTimestampsCache;
import org.hibernate.cache.Region;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.type.Type;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunctionRegistry;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.event.EventListeners;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.engine.*;
import org.hibernate.engine.query.QueryPlanCache;
import org.hibernate.stat.Statistics;
import org.hibernate.stat.StatisticsImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.classic.Session;

import javax.naming.Reference;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

import com.infusion.util.event.EventBroker;

/**
 * Wrapped "SessionFactory" class that returns custom TenantSession instances.
 */
public class InterceptableSessionFactory implements SessionFactory , SessionFactoryImplementor{
// ========================================================================================================================
//    Instance Fields
// ========================================================================================================================

    private SessionFactory wrapped;
    private SessionFactoryImplementor implementor;

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public InterceptableSessionFactory() {
    }

    public InterceptableSessionFactory(SessionFactory wrapped, EventBroker eventBroker) {
        this.setWrapped(wrapped);
    }

// ========================================================================================================================
//    Public Instance Methods
// ========================================================================================================================

    public void close() throws HibernateException {
        wrapped.close();
    }

    public void evict(Class aClass) throws HibernateException {
        wrapped.evict(aClass);
    }

    public void evict(Class aClass, Serializable serializable) throws HibernateException {
        wrapped.evict(aClass, serializable);
    }

    public void evictCollection(String s) throws HibernateException {
        wrapped.evictCollection(s);
    }

    public void evictCollection(String s, Serializable serializable) throws HibernateException {
        wrapped.evictCollection(s, serializable);
    }

    public void evictEntity(String s) throws HibernateException {
        wrapped.evictEntity(s);
    }

    public void evictEntity(String s, Serializable serializable) throws HibernateException {
        wrapped.evictEntity(s, serializable);
    }

    public void evictQueries() throws HibernateException {
        wrapped.evictQueries();
    }

    public void evictQueries(String s) throws HibernateException {
        wrapped.evictQueries(s);
    }

    public Map getAllClassMetadata() throws HibernateException {
        return wrapped.getAllClassMetadata();
    }

    public Map getAllCollectionMetadata() throws HibernateException {
        return wrapped.getAllCollectionMetadata();
    }

    public ClassMetadata getClassMetadata(Class aClass) throws HibernateException {
        return wrapped.getClassMetadata(aClass);
    }

    public ClassMetadata getClassMetadata(String s) throws HibernateException {
        return wrapped.getClassMetadata(s);
    }

    public CollectionMetadata getCollectionMetadata(String s) throws HibernateException {
        return wrapped.getCollectionMetadata(s);
    }

    public Session getCurrentSession() throws HibernateException {
        return wrapped.getCurrentSession();
    }

    public Set getDefinedFilterNames() {
        return wrapped.getDefinedFilterNames();
    }

    /**
     * This isn't an actual method from the SessionFactory interface, but we duck-type implemented it here
     * because most often the wrapped SessionFactory will actually be a SessionFactoryImpl (and places this
     * class is wired to will expect the method to exist)
     *
     */
    public EventListeners getEventListeners() {
        EventListeners rtn = null;
        if (wrapped instanceof SessionFactoryImpl) {
            rtn = ((SessionFactoryImpl) wrapped).getEventListeners();
        }
        return rtn;
    }

    public FilterDefinition getFilterDefinition(String s) throws HibernateException {
        return wrapped.getFilterDefinition(s);
    }

    public Reference getReference() throws NamingException {
        return wrapped.getReference();
    }

    public Statistics getStatistics() {
        return wrapped.getStatistics();
    }

    public SessionFactory getWrapped() {
        return wrapped;
    }

    public boolean isClosed() {
        return wrapped.isClosed();
    }

    public Session openSession() throws HibernateException {
        return new InterceptableSession(wrapped.openSession());
    }

    public Session openSession(Connection connection) {
        final Session session = wrapped.openSession(connection);
        return new InterceptableSession(session);
    }

    public Session openSession(Interceptor interceptor) throws HibernateException {
        final Session session = wrapped.openSession(interceptor);
        return new InterceptableSession(session);
    }

    public Session openSession(Connection connection, Interceptor interceptor) {
        final Session session = wrapped.openSession(connection, interceptor);
        return new InterceptableSession(session);
    }

    public StatelessSession openStatelessSession() {
        return wrapped.openStatelessSession();
    }

    public StatelessSession openStatelessSession(Connection connection) {
        return wrapped.openStatelessSession(connection);
    }


    public EntityPersister getEntityPersister(String entityName) throws MappingException {
        return implementor.getEntityPersister(entityName);
    }

    public CollectionPersister getCollectionPersister(String role) throws MappingException {
        return implementor.getCollectionPersister(role);
    }

    public Dialect getDialect() {
        return implementor.getDialect();
    }

    public Interceptor getInterceptor() {
        return implementor.getInterceptor();
    }

    public QueryPlanCache getQueryPlanCache() {
        return implementor.getQueryPlanCache();
    }

    public Type[] getReturnTypes(String queryString) throws HibernateException {
        return implementor.getReturnTypes(queryString);
    }

    public String[] getReturnAliases(String queryString) throws HibernateException {
        return implementor.getReturnAliases(queryString);
    }

    public ConnectionProvider getConnectionProvider() {
        return implementor.getConnectionProvider();
    }

    public String[] getImplementors(String className) throws MappingException {
        return implementor.getImplementors(className);
    }

    public String getImportedClassName(String name) {
        return implementor.getImportedClassName(name);
    }

    public TransactionManager getTransactionManager() {
        return implementor.getTransactionManager();
    }

    public QueryCache getQueryCache() {
        return implementor.getQueryCache();
    }

    public QueryCache getQueryCache(String regionName) throws HibernateException {
        return implementor.getQueryCache(regionName);
    }

    public UpdateTimestampsCache getUpdateTimestampsCache() {
        return implementor.getUpdateTimestampsCache();
    }

    public StatisticsImplementor getStatisticsImplementor() {
        return implementor.getStatisticsImplementor();
    }

    public NamedQueryDefinition getNamedQuery(String queryName) {
        return implementor.getNamedQuery(queryName);
    }

    public NamedSQLQueryDefinition getNamedSQLQuery(String queryName) {
        return implementor.getNamedSQLQuery(queryName);
    }

    public ResultSetMappingDefinition getResultSetMapping(String name) {
        return implementor.getResultSetMapping(name);
    }

    public IdentifierGenerator getIdentifierGenerator(String rootEntityName) {
        return implementor.getIdentifierGenerator(rootEntityName);
    }

    public Region getSecondLevelCacheRegion(String regionName) {
        return implementor.getSecondLevelCacheRegion(regionName);
    }

    public Map getAllSecondLevelCacheRegions() {
        return implementor.getAllSecondLevelCacheRegions();
    }

    public SQLExceptionConverter getSQLExceptionConverter() {
        return implementor.getSQLExceptionConverter();
    }

    public Settings getSettings() {
        return implementor.getSettings();
    }

    public Session openTemporarySession() throws HibernateException {
        return implementor.openTemporarySession();
    }

    public Session openSession(Connection connection, boolean flushBeforeCompletionEnabled, boolean autoCloseSessionEnabled, ConnectionReleaseMode connectionReleaseMode) throws HibernateException {
        return implementor.openSession(connection, flushBeforeCompletionEnabled, autoCloseSessionEnabled, connectionReleaseMode);
    }

    public Set getCollectionRolesByEntityParticipant(String entityName) {
        return implementor.getCollectionRolesByEntityParticipant(entityName);
    }

    public EntityNotFoundDelegate getEntityNotFoundDelegate() {
        return implementor.getEntityNotFoundDelegate();
    }

    public SQLFunctionRegistry getSqlFunctionRegistry() {
        return implementor.getSqlFunctionRegistry();
    }

    public Type getIdentifierType(String className) throws MappingException {
        return implementor.getIdentifierType(className);
    }

    public String getIdentifierPropertyName(String className) throws MappingException {
        return implementor.getIdentifierPropertyName(className);
    }

    public Type getReferencedPropertyType(String className, String propertyName) throws MappingException {
        return implementor.getReferencedPropertyType(className, propertyName);
    }

    public void setWrapped(SessionFactory wrapped) {
        this.wrapped = wrapped;
        this.implementor = (SessionFactoryImplementor) wrapped;
    }

    public SessionFactoryImplementor getImplementor() {
        return implementor;
    }

}
