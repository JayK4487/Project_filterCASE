package de.xcase.filtercase2.views;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.components.AppRouterLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;


@Route(value = LoginView.VIEW_NAME)
@PageTitle(value = "Login filterCASE")
public class LoginView extends BaseView {

    public static final String VIEW_NAME = "login";
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginView.class);

    private final VerticalLayout vlLogin = new VerticalLayout();
    private final TextField tfUsername = new TextField();
    private final PasswordField pfLoginPassword = new PasswordField();
    private final Button btnLogin = new Button("Login", new Icon(VaadinIcon.SIGN_IN));
    private final FormLayout flLogin = new FormLayout();

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private HttpServletRequest req;

    public LoginView() {
        tfUsername.setPlaceholder("Nutzername");
        pfLoginPassword.setPlaceholder("Passwort");

        flLogin.setResponsiveSteps(new FormLayout.ResponsiveStep(null, 1));
        flLogin.addFormItem(tfUsername, "Nutzername:");
        flLogin.addFormItem(pfLoginPassword, "Passwort:");
        flLogin.addFormItem(btnLogin, "");

        btnLogin.addClickListener(clickEvent -> {
            try {
                UsernamePasswordAuthenticationToken authReq
                        = new UsernamePasswordAuthenticationToken(tfUsername.getValue(), pfLoginPassword.getValue());

                Authentication auth = authManager.authenticate(authReq);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);

                //TODO Bestätiger-Rechte laden

                this.getUI().ifPresent(ui -> ui.navigate(StartsiteView.class));
            } catch (Exception e) {
                Notification.show("Fehler: " + e.getMessage());
            }

        });

        btnLogin.addClickShortcut(Key.ENTER);

        vlLogin.add(flLogin);
        this.add(vlLogin);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent enterEvent) {
        super.beforeEnter(enterEvent);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            enterEvent.rerouteTo(StartsiteView.VIEW_NAME);
        }
    }
}
