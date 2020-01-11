package de.xcase.filtercase2.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.entities.Keyword;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = KeywordView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class KeywordView extends BaseView {

    public static final String VIEW_NAME = "KeywordView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final VerticalLayout vlSite = new VerticalLayout();
    private final Card cPanel = new Card("Bearbeiten der Schlüsselbegriffe");
    private final Button btAdd = new Button("+");
    private final Grid<Keyword> grKeywords = new Grid<>();

    private List<Keyword> keywordList = new ArrayList<>();

    private KeywordRepository keywordRepository;

    public KeywordView(@Autowired KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;

        cPanel.getElement().getStyle().set("width", "100%");
        cPanel.add(setupSite());

        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);
    }

    private Component setupSite() {
        vlSite.getElement().getStyle().set("length", "100%");

        grKeywords.addColumn(Keyword::getKeyword).setHeader("Schlüsselbegriff");
        grKeywords.addColumn(Keyword::getUserKeyword).setHeader("gesetzt durch");
        grKeywords.addComponentColumn(keyword -> {
            Checkbox active = new Checkbox();
            active.setValue(keyword.getStatus());
            active.setEnabled(false);
            return active;
        }).setHeader("Status");
        grKeywords.addComponentColumn(keyword -> new Button("Bearbeiten"));
        grKeywords.addComponentColumn(keyword -> {
            Button delete = new Button(VaadinIcon.TRASH.create());
            delete.addClickListener(clickEvent -> {
                keywordRepository.delete(keyword);
                keywordList.remove(keyword);
                grKeywords.getDataProvider().refreshAll();
            });
            return delete;
        });

        keywordList.addAll(keywordRepository.findAll());
        grKeywords.setItems(keywordList);

        vlSite.add(btAdd, grKeywords);
        return vlSite;
    }
}
