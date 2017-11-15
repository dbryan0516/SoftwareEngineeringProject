package edu.ncsu.csc.itrust2.config;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class LoginAuditingListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent ( final ApplicationEvent event ) {
        if ( event instanceof InteractiveAuthenticationSuccessEvent ) {
            final InteractiveAuthenticationSuccessEvent authEvent = (InteractiveAuthenticationSuccessEvent) event;
            final Authentication authentication = authEvent.getAuthentication();
            LoggerUtil.log( TransactionType.LOGIN_SUCCESS, authentication.getName() );
        }

        if ( event instanceof AbstractAuthenticationFailureEvent ) {
            final AbstractAuthenticationFailureEvent authEvent = (AbstractAuthenticationFailureEvent) event;
            final Authentication authentication = authEvent.getAuthentication();
            LoggerUtil.log( TransactionType.LOGIN_FAILURE, authentication.getPrincipal().toString() );
        }
    }
}
