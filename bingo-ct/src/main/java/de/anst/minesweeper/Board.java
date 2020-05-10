package de.anst.minesweeper;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Board extends VerticalLayout {
	
	public Board(final int numberOfRows, final int numberOfColumns ) {
		
		for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			for (int columnNumber = 0; columnNumber < numberOfColumns; columnNumber++) {
				horizontalLayout.add(new Button("x"));
			}
			add(horizontalLayout);
		}
	}

}
