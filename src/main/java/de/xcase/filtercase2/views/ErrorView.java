package de.xcase.filtercase2.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.enums.EError;

@Route(value = ErrorView.VIEW_NAME)
public class ErrorView extends BaseView implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "error";

    private Label lbCause = new Label("Ein unbekannter Fehler ist aufgetreten!");

    public ErrorView() {
        add(lbCause);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String error) {
        EError cause = EError.getByName(error);
        if (cause != null) {
            lbCause.setText(cause.getText());
        }
    }
}