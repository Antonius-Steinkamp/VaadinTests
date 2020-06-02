package de.person;

import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.Route;

import lombok.extern.java.Log;

@SuppressWarnings("serial")
@Route("persons")
@Log
public class PersonCRUD extends VerticalLayout {

	private PersonService personService = new PersonService();
	
	public PersonCRUD() {
		GridCrud<Person> crud = new GridCrud<>(Person.class);
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.setCrudListener(personService);
		
		
		crud.getGrid().getColumns().stream().forEach(column -> {
			column.setResizable(true);
			log.info(column.getKey());
		});
		
		crud.getGrid().setColumnReorderingAllowed(true);
		crud.getGrid().removeColumnByKey("id");
		
		// Neuen Button, der bei selected aktiviert wird.
        Button extraButton = new Button(VaadinIcon.ANCHOR.create(), e -> Notification.show("Clicked Extra"));
        extraButton.getElement().setAttribute("title", "Extra");
        crud.getCrudLayout().addToolbarComponent(extraButton);
        extraButton.setEnabled(false);
        
        // setEnabled(isSelected)
        crud.getGrid().addSelectionListener(new SelectionListener<Grid<Person>, Person>() {
			
			@Override
			public void selectionChange(SelectionEvent<Grid<Person>, Person> event) {
				extraButton.setEnabled(event.getAllSelectedItems().size() > 0);				
			}
		});

		add(crud);	
		setSizeFull();
	}
}
