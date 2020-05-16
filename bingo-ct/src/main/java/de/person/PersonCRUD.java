package de.person;

import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import lombok.extern.java.Log;

@Route("persons")
@Log
public class PersonCRUD extends VerticalLayout {

	private PersonService personService = new PersonService();
	
	public PersonCRUD() {
		GridCrud<Person> crud = new GridCrud<>(Person.class);
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.setCrudListener(personService);
		
		
		crud.getGrid().getColumns().stream().forEach(column -> column.setResizable(true));
		
        Button extraButton = new Button(VaadinIcon.ANCHOR.create(), e -> log.info("Extra"));
        extraButton.getElement().setAttribute("title", "Extra");
        crud.getCrudLayout().addToolbarComponent(extraButton);

		add(crud);	
		setSizeFull();
	}
}
