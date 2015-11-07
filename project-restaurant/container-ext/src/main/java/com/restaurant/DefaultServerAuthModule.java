package com.restaurant;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ServerAuthModule;
import java.util.Map;

public class DefaultServerAuthModule implements ServerAuthModule {
    private static final Class[] SUPPORTED_MESSAGE_TYPES = new Class[]{
            javax.servlet.http.HttpServletRequest.class,
            javax.servlet.http.HttpServletResponse.class};

    private static final String REALM_NAME_OPTION = "realm.name";

    protected MessagePolicy requestPolicy;
    protected MessagePolicy responsePolicy;
    protected CallbackHandler handler;
    protected Map options;

    protected String realm;

    @Override
    public void initialize(final MessagePolicy requestPolicy,
                           final MessagePolicy responsePolicy,
                           final CallbackHandler handler,
                           final Map options) throws AuthException {
        this.requestPolicy = requestPolicy;
        this.responsePolicy = responsePolicy;
        this.handler = handler;
        this.options = options;

        if (options != null) {
            this.realm = (String) options.get(REALM_NAME_OPTION);
        }
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        return SUPPORTED_MESSAGE_TYPES;
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        return AuthStatus.SUCCESS;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject subject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
    }
}
