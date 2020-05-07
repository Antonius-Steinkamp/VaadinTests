package de.anst;

import com.vaadin.flow.router.Route;

import de.anst.data.ValueLists;

@Route("props")
public class PropertyView extends BaseView {

	public PropertyView() {
		super();
		
		setItems(ValueLists.getProperties());
	}
	

}
