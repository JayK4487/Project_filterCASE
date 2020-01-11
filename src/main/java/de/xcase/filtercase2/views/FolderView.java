package de.xcase.filtercase2.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.entities.UsedFolders;
import de.xcase.filtercase2.backend.respositories.UsedFolderRepository;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = FolderView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Ordnerstruktur")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class FolderView extends BaseView {

    public static final String VIEW_NAME = "FolderView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final Card cPanel = new Card("Bearbeitung der Quell- und Zielordner");
    private final VerticalLayout vlCardMain = new VerticalLayout();

    private final Grid<UsedFolders> grUsedFolder = new Grid<>();
    private final Button btAdd = new Button("+");

    private List<UsedFolders> usedFolderList = new ArrayList<>();

    public FolderView(@Autowired UsedFolderRepository usedFolderRepository) {
        cPanel.getElement().getStyle().set("width", "100%");

        usedFolderList.addAll(usedFolderRepository.findAll());

        grUsedFolder.addColumn(UsedFolders::getSourceFolder).setHeader("Quellordner");
        grUsedFolder.addColumn(UsedFolders::getDestinationFolder).setHeader("Zielordner");
        grUsedFolder.addColumn(UsedFolders::getUser).setHeader("Gesetzt durch");
        grUsedFolder.addComponentColumn(keyword -> new Button("Bearbeiten"));
        grUsedFolder.addComponentColumn(keyword -> new Button(VaadinIcon.TRASH.create()));
        grUsedFolder.setItems(usedFolderList);
        grUsedFolder.getDataProvider().refreshAll();

        vlCardMain.getElement().getStyle().set("length", "100%");
        vlCardMain.add(btAdd, grUsedFolder);

        cPanel.add(vlCardMain);

        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);
    }
}
