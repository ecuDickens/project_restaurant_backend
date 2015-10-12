package com.restaurant.web;

//@Path("/logout")
//@Consumes({ MediaType.APPLICATION_JSON })
//@Produces({ MediaType.APPLICATION_JSON })
public class LogoutResource {

//    private final JpaHelper jpaHelper;
//
//    @Inject
//    public LogoutResource(JpaHelper jpaHelper) {
//        this.jpaHelper = jpaHelper;
//    }
//
//    @POST
//    @Path("/{account_id}")
//    public Response logout(@PathParam("account_id") final Long accountId) throws HttpException {
//        jpaHelper.executeJpaTransaction(new ThrowingFunction1<Integer, EntityManager, HttpException>() {
//            @Override
//            public Integer apply(EntityManager em) throws HttpException {
//                final Account forUpdate = em.find(Account.class, accountId);
//                if (null != forUpdate) {
//                    em.refresh(forUpdate, PESSIMISTIC_WRITE);
//                    forUpdate.setLastLoginDateTime(null);
//                }
//
//                return null;
//            }
//        });
//        return buildResponse(OK);
//    }
}
