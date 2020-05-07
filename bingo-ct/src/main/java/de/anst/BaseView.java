package de.anst;

import java.util.List;
import java.util.logging.Logger;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import de.anst.data.KeyValue;

/**
 * {@link VerticalLayout} mit {@link KeyValue}-{@link Grid}
 * @author Antonius
 *
 */
public class BaseView extends VerticalLayout {
	private static final Logger LOG = Logger.getLogger(BaseView.class.getName());

	protected Grid<KeyValue<String, String>> grid = new Grid<>();

	public BaseView() {
		
        grid.addColumn(KeyValue::getKey).setHeader("Key");
        grid.addColumn(KeyValue::getValue).setHeader("Value");
        add(grid);
        setSizeFull();

	}
	
	/**
	 * Set items for {@link Grid}.
	 * 
	 * @param items List<KeyValue<String, String>> the izems
	 */
	public void setItems(List<KeyValue<String, String>> items) {
		final String msg = "Set " + items.size() + " items in grid"; 
		LOG.info(msg);
        grid.setItems(items);
	}
}
