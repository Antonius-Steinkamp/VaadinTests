package de.anst;

import com.vaadin.flow.router.Route;

import de.anst.data.ValueLists;

@Route("env")
public class EnvironmentView extends BaseView {

	public EnvironmentView() {
		super();
		setItems(ValueLists.getEnvironmen());
	}
	
}
