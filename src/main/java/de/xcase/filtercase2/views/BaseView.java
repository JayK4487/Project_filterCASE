package de.xcase.filtercase2.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import de.xcase.filtercase2.app.security.SecurityConfig;
import de.xcase.filtercase2.backend.enums.EError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class BaseView extends Div implements BeforeEnterObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseView.class);

    @Autowired
    private HttpServletRequest request;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (SecurityConfig.isAuthenticationRequired(event.getNavigationTarget())) {
            //Check if user is authenticated
            if (!auth.isAuthenticated() || auth.getPrincipal() == null || auth.getPrincipal().equals("anonymousUser")) {
                event.rerouteTo(ErrorView.VIEW_NAME, EError.AUTHENTICATION.getName());
                return;
            }
            //Check if user is authorized
            if (auth.getAuthorities().stream().noneMatch(ga -> ga.getAuthority().equals("X-CASE-Benutzer"))) {
                event.rerouteTo(ErrorView.VIEW_NAME, EError.AUTHORIZATION.getName());
            }
        }
    }
}
