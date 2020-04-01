package com.portalsoup.mrbutlertron.data.dao

import com.portalsoup.mrbutlertron.data.util.Generics
import com.portalsoup.mrbutlertron.data.util.HibernateUtil
import org.hibernate.Session
import org.hibernate.Transaction
import org.hibernate.query.Query
import java.lang.RuntimeException
import javax.persistence.RollbackException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class AbstractDao<E> {
    private val entityClass: Class<E>

    protected fun <T> withTransaction(function: (Session) -> T): T {
        var transaction: Transaction? = null
        return try {
            HibernateUtil.sessionFactory.openSession().use { session ->
                try {
                    transaction = session.beginTransaction()
                    function(session)
                } finally {
                    transaction?.commit()
                }
            }
        } catch (e: Throwable) {
            if (transaction != null) {
                transaction?.rollback()
            }
            throw RollbackException()
        }
    }

    protected fun withTransaction(supplier: (Session) -> Unit) {
        var transaction: Transaction? = null
        try {
            HibernateUtil.sessionFactory.openSession().use { session ->
                transaction = session.beginTransaction()
                supplier(session)
                transaction?.commit()
            }
        } catch (e: Throwable) {
            if (transaction != null) {
                transaction?.rollback()
            }
            throw RollbackException(e)
        }
    }

    protected fun <T> withSession(function: (Session) -> T): T {
        HibernateUtil.sessionFactory.openSession().use({ session -> return function(session) })
    }

    fun <T> asCriteria(function: (CriteriaBuilder, CriteriaQuery<E>, Root<E>) -> T): List<E> {
        return withSession { session: Session ->
                val criteriaBuilder: CriteriaBuilder = session.getCriteriaBuilder()
                val query: CriteriaQuery<E> = criteriaBuilder.createQuery(entityClass)
                val root: Root<E> = query.from(entityClass)
                query.select(root)
                // Do the work
                function(criteriaBuilder, query, root)
                val q: Query<E> = session.createQuery(query)
                q.getResultList()
            }
    }

    init {
        entityClass = Generics.getTypeParameter(javaClass) as Class<E>
    }
}