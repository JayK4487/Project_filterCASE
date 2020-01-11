package de.xcase.filtercase2.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;

@Route(value = LogView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Ordnerstruktur")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class LogView extends BaseView {
    public static final String VIEW_NAME = "logs";

    public LogView() {

        HorizontalLayout hl = new HorizontalLayout();

        Card cardpanel = new Card("Reports");
        cardpanel.getElement().getStyle().set("width", "100%");
        cardpanel.add(startLogRequest());

        hl.add(cardpanel);
        hl.setSizeFull();
        add(hl);
    }

    private Component startLogRequest() {

        VerticalLayout content = new VerticalLayout();
        content.getElement().getStyle().set("length", "100%");

        Button startButton = new Button("Abrufen");
        content.add(startButton);

        return content;
    }
}
