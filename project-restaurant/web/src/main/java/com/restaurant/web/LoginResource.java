package com.restaurant.web;

public class LoginResource {

//    private static final XLogger LOGGER = XLoggerFactory.getXLogger(LoginResource.class);
//
//    private final JpaHelper jpaHelper;
//
//    @Inject
//    public LoginResource(JpaHelper jpaHelper) {
//        this.jpaHelper = jpaHelper;
//    }
//
//    @POST
//    public Response login(@QueryParam("email") final String email,
//                          @QueryParam("password") final String password) throws HttpException {
//        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
//            return buildResponse(BAD_REQUEST, new ErrorType("Email and password required."));
//        }
//
//        final Long accountId = jpaHelper.executeJpaTransaction(new ThrowingFunction1<Long, EntityManager, HttpException>() {
//            @Override
//            public Long apply(EntityManager em) throws HttpException {
//                final Account account = jpaHelper.getAccountByEmail(em, email);
//                if (null != account) {
//                    final HashKey hashKey = jpaHelper.getHashKey(em, email, password);
//                    if (null != hashKey) {
//                        em.refresh(account, LockModeType.PESSIMISTIC_WRITE);
//                        account.setLastLoginDateTime( new Timestamp(DateTime.now().getMillis()));
//                        return account.getId();
//                    }
//                }
//                return null;
//            }
//        });
//
//        if (null == accountId) {
//            return buildResponse(BAD_REQUEST, new ErrorType("Email or password was incorrect."));
//        }
//
//        LOGGER.info("Logged in");
//        return buildResponse(OK, new Account().withId(accountId));
//    }
//
//    @POST
//    @Path("/{account_id}")
//    public Response updateEmailAndPassword(@PathParam("account_id") final Long accountId,
//                          @QueryParam("email") final String email,
//                          @QueryParam("password") final String password) throws HttpException {
//        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
//            return buildResponse(BAD_REQUEST, new ErrorType("Email and password required."));
//        }
//
//        try {
//            jpaHelper.executeJpaTransaction(new ThrowingFunction1<Integer, EntityManager, HttpException>() {
//                @Override
//                public Integer apply(EntityManager em) throws HttpException {
//                    final Account forUpdate = em.find(Account.class, accountId);
//                    final HashKey hashKey = jpaHelper.getHashKey(em, forUpdate.getHashKey());
//
//                    em.refresh(hashKey, PESSIMISTIC_WRITE);
//                    hashKey.setHashCode(HashKey.generateHashCode(email, password));
//
//                    em.refresh(forUpdate, PESSIMISTIC_WRITE);
//                    forUpdate.withEmail(email).withHashKey(hashKey.getHashCode());
//                    return hashKey.getHashCode();
//                }
//            });
//        } catch (Exception e) {
//            return buildResponse(BAD_REQUEST, new ErrorType("Email already in use."));
//        }
//        return buildResponse(OK, new ErrorType("Email and password updated."));
//    }
}
