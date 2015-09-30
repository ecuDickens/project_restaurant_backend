package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.collect.MoreCollections;
import com.restaurant.entity.Account;
import com.restaurant.entity.Recipe;
import com.restaurant.entity.SleepRecord;
import com.restaurant.exception.HttpException;
import com.restaurant.helper.JpaHelper;
import com.restaurant.types.ErrorType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

import static com.restaurant.helper.JpaHelper.buildResponse;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import static javax.ws.rs.core.Response.Status.*;

public class SleepRecordResource {

    private final JpaHelper jpaHelper;

    @Inject
    public SleepRecordResource(JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    public Response createSleepRecord(final Long accountId, final SleepRecord record) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        final Long recordId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                record.setAccount(em.getReference(Account.class, accountId));
                em.persist(record);
                em.flush();
                return record.getId();
            }
        });

        if (null == recordId) {
            return buildResponse(BAD_REQUEST, new ErrorType("Unable to create record."));
        }

        return buildResponse(OK, new Recipe().withId(recordId));
    }

    public Response loadSleepRecords(final Long accountId, final Date startDate, final Date endDate) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        final List<SleepRecord> records = jpaHelper.executeJpa(new ThrowingFunction1<List<SleepRecord>, EntityManager, HttpException>() {
            @Override
            public List<SleepRecord> apply(EntityManager em) throws HttpException {
                final String queryStr = String.format("select a from SleepRecord a where a.accountId = :accountId%s%s",
                        null != startDate ? " and a.recordDate >= :startDate" : "",
                        null != endDate ? " and a.recordDate <= :endDate" : "");
                final TypedQuery<SleepRecord> query = em.createQuery(queryStr, SleepRecord.class);
                query.setParameter("accountId", accountId);
                if (null != startDate) {
                    query.setParameter("startDate", startDate);
                }
                if (null != endDate) {
                    query.setParameter("endDate", endDate);
                }
                return query.getResultList();
            }
        });
        if (MoreCollections.isNullOrEmpty(records)) {
            return buildResponse(NO_CONTENT, new ErrorType("Records not found"));
        }
        for (SleepRecord record : records) {
            record.clean();
        }
        return buildResponse(OK, records);
    }

    public Response loadSleepRecord(final Long accountId, final Long recordId) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        final SleepRecord record = jpaHelper.executeJpa(new ThrowingFunction1<SleepRecord, EntityManager, HttpException>() {
            @Override
            public SleepRecord apply(EntityManager em) throws HttpException {
                return em.find(SleepRecord.class, recordId);
            }
        });
        if (null == record) {
            return buildResponse(NO_CONTENT, new ErrorType("Record not found"));
        }
        record.clean();
        return buildResponse(OK, record);
    }

    public Response updateSleepRecord(final Long accountId, final Long recordId, final SleepRecord record) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                final SleepRecord forUpdate = em.find(SleepRecord.class, recordId);
                em.refresh(forUpdate, PESSIMISTIC_WRITE);
                forUpdate
                        .withHoursSlept(null == record.getHoursSlept() ? forUpdate.getHoursSlept() : record.getHoursSlept())
                        .withRecordDate(null == record.getRecordDate() ? forUpdate.getRecordDate() : record.getRecordDate());
                return forUpdate.getId();
            }
        });
        return buildResponse(OK);
    }

    public Response deleteSleepRecord(final Long accountId, final Long recordId) throws HttpException {
        if (!jpaHelper.isLoggedIn(accountId)) {
            return buildResponse(UNAUTHORIZED, new ErrorType("Login expired, please login again."));
        }

        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
            @Override
            public Long apply(EntityManager em) throws HttpException {
                em.remove(em.find(SleepRecord.class, recordId));
                return null;
            }
        });
        return buildResponse(NO_CONTENT);
    }
}