package de.xcase.filtercase2.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import de.xcase.filtercase2.views.ExecutionTimesView;
import de.xcase.filtercase2.views.FolderView;
import de.xcase.filtercase2.views.KeywordView;
import de.xcase.filtercase2.views.NotificationView;
import de.xcase.filtercase2.views.StartsiteView;
import de.xcase.filtercase2.views.StatisticsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;


@BodySize
@Theme(Lumo.class)
@CssImport(value = "./styles/menu-layout-styles.css")
@CssImport(value = "./styles/navbar-styles.css", themeFor = "vaadin-app-layout")
public class AppRouterLayout extends AppLayout {

    @Autowired
    HttpServletRequest httpServletRequest;

    public AppRouterLayout() {
        Image iLogo = new Image("filtercaselogo.jpg", "filterCASE");
        iLogo.setHeight("44px");

        addToNavbar(new DrawerToggle(), iLogo);

        //final DrawerToggle drawerToggle = new DrawerToggle();
        final RouterLink startsite = new RouterLink("Startseite", StartsiteView.class);
        final RouterLink statistics = new RouterLink("Statistiken", StatisticsView.class);
        final RouterLink executiontimes = new RouterLink("Ausführungszeiten", ExecutionTimesView.class);
        final RouterLink keywords = new RouterLink("Suchbegriffe", KeywordView.class);
        final RouterLink notifications = new RouterLink("Benachrichtigungen", NotificationView.class);
        final RouterLink folders = new RouterLink("Ordnerstruktur", FolderView.class);
        //final RouterLink logs = new RouterLink("Logs", LogView.class);
        final Button logout = new Button("Logout");
        logout.addClickListener(clickEvent -> {
            SecurityContextHolder.clearContext();
            httpServletRequest.getSession(false).invalidate();

            UI.getCurrent().getSession().close();
            UI.getCurrent().getPage().reload();
        });

        Paragraph menu = new Paragraph("Menü");
        Paragraph administration = new Paragraph("Administration");

        Hr line = new Hr();
        Hr line2 = new Hr();

        final VerticalLayout vl = new VerticalLayout(menu, line, startsite, administration, line2, statistics, executiontimes, keywords, notifications, folders, logout); //, login);

        addToDrawer(vl);

    }
}


