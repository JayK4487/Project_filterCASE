package de.xcase.filtercase2.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * API implementation for the polymer paper card.
 */
@Tag("paper-card")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class Card extends Component implements HasComponents {

    /**
     * The {@link Div} containing the cards content.
     */
    private Div content = new Div();

    /**
     * The {@link Div} containing all the actions on the card.
     */
    private Div action = new Div();

    /**
     * {@link PropertyDescriptor} for the heading property.
     */
    private static final PropertyDescriptor<String, String> HEADING =
            PropertyDescriptors.propertyWithDefault("heading", "");

    /**
     * {@link PropertyDescriptor} for the elevation property.
     */
    private static final PropertyDescriptor<Integer, Integer> ELEVATION =
            PropertyDescriptors.propertyWithDefault("elevation", 0);

    private static final PropertyDescriptor<String, String> IMAGE =
            PropertyDescriptors.propertyWithDefault("image", "");

    /**
     * Basic constructor.
     */
    public Card() {
        content.setClassName("card-content");
        action.setClassName("card-actions");

        this.add(content, action);
    }

    /**
     * Constructor that allows setting the card title.
     * @param title text of the heading property
     */
    public Card(final String title) {
        this();
        set(HEADING, title);
    }

    /**
     * Set the heading property value.
     * @param heading the new property value
     */
    public void setHeading(@NotNull final String heading) {
        set(HEADING, heading);
    }

    /**
     * Get the heading property value.
     * @return the heading property value
     */
    public String getHeading() {
        return get(HEADING);
    }

    /**
     * Set the elevation property value.
     * @param elevation the elevation property value (between 0 - 5)
     */
    public void setElevation(@NotNull final Integer elevation) {
        if (elevation >= 0 && elevation <= 6) {
            set(ELEVATION, elevation);
        }
    }

    /**
     * Get the elevation property value.
     * @return the elevation property value.
     */
    public Integer getElevation(){
        return get(ELEVATION);
    }

    /**
     * Add components to the content area.
     * @param components one or more {@link Component}s
     */
    public void addContent(@NotNull final Component components) {
        action.add(components);
    }

    /**
     * Add components to the actions area.
     * @param components one or more {@link Component}s
     */
    public void addAction(@NotNull final Component... components) {
        action.add(components);
    }

    /**
     * Remove components from the content area.
     * @param components one or more {@link Component}s
     */
    public void removeContent(@NotNull final Component components) {
        action.remove(components);
    }

    /**
     * Remove components from the action area.
     * @param components one or more {@link Component}s
     */
    public void removeAction(@NotNull final Component... components) {
        action.remove(components);
    }

}
