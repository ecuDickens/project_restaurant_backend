package com.restaurant.web;

import com.google.inject.Inject;
import com.restaurant.base.ThrowingFunction1;
import com.restaurant.entity.Role;
import com.restaurant.exception.HttpException;
import com.restaurant.helper.JpaHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.restaurant.helper.JpaHelper.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/roles")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class RoleResource {

    private final JpaHelper jpaHelper;

    @Inject
    public RoleResource(final JpaHelper jpaHelper) {
        this.jpaHelper = jpaHelper;
    }

    @GET
    public Response getRoles() throws HttpException {
        return buildResponse(OK, loadRoles());
    }

    public List<Role> loadRoles() throws HttpException {
        return jpaHelper.executeJpa(new ThrowingFunction1<List<Role>, EntityManager, HttpException>() {
            @Override
            public List<Role> apply(EntityManager em) throws HttpException {
                Query query = em.createQuery("SELECT e FROM Role e");
                return (List<Role>) query.getResultList();
            }
        });
    }
}
