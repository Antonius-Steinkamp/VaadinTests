package de.anst.all;

import java.util.List;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;

@Route("all")
public class KnownRoutes  extends VerticalLayout {

	public KnownRoutes() {
		final List<RouteData> availableRoutes = RouteConfiguration.forApplicationScope().getAvailableRoutes();
		for (RouteData routeData: availableRoutes) {
			add(new Anchor(routeData.getUrl(), routeData.getUrl()));
		}
	}
}
