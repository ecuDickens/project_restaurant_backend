package com.restaurant.helper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.exception.HttpException;
import com.restaurant.jpa.session.EntitySession;
import com.restaurant.jpa.spi.JpaEntityManagerService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

@Singleton
public class JpaHelper {

    private JpaEntityManagerService jpaManagerService;

    public final static String SUBMITTER_HEADER = "Submitter-Id";

    @Inject
    public JpaHelper(JpaEntityManagerService jpaManagerService) {
        this.jpaManagerService = jpaManagerService;
    }

    public <T> T executeJpa(final ThrowingFunction1<T, EntityManager, HttpException> jpaFunction) throws HttpException {
        try {
            return jpaManagerService.invoke(new EntitySession<T>() {
                @Override
                public T execute(EntityManager entityManager) throws HttpException {
                    return jpaFunction.apply(entityManager);
                }
            });
        } catch (NoResultException ex) {
            return null;
        }
    }

    public <T> T executeJpaTransaction(final ThrowingFunction1<T, EntityManager, HttpException> jpaFunction) throws HttpException {
        return jpaManagerService.invoke(new EntitySession<T>() {
            @Override
            public T execute(EntityManager entityManager) throws HttpException {
                boolean rollback = false;
                T result = null;
                try {
                    entityManager.getTransaction().begin();
                    result = jpaFunction.apply(entityManager);
                    entityManager.getTransaction().commit();
                }  catch (EntityNotFoundException e) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Record was not found. %s", e.getMessage()), e);
                } catch (NoResultException e) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Record was not found. %s", e.getMessage()), e);
                } catch (EntityExistsException t) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Record already created. %s", t.getMessage()), t);
                } catch (Throwable t) {
                    rollback = true;
                    throw new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR, String.format("Transaction error. %s", t.getMessage()), t);
                } finally {
                    if (rollback && entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                }
                return result;
            }
        });
    }

    public static Response buildResponse(Response.Status status) {
        return buildResponse(status, null);
    }
    public static Response buildResponse(Response.Status status, Object entity) {
        return Response
                .status(status)
                .entity(entity)
                .build();
    }
}