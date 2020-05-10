package de.anst.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.treegrid.ExpandEvent;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import lombok.extern.java.Log;

@Log
@Route("filesystem")
public class FilesystemView extends VerticalLayout //
		implements //
		ComponentEventListener<ExpandEvent<File, TreeGrid<File>>>, //
		ValueProvider<File, String>, //
		HasUrlParameter<String>, //
		SelectionListener<Grid<File>, File> {

	private File rootFile = new File(System.getProperty("user.home"));
	private TreeGrid<File> grid = new TreeGrid<>();
	private TextArea fileContentsView = new TextArea();

	public FilesystemView() {
		add(new H1("Filesystem"));

	}

	private void initData() {
		log.info("Init for " + rootFile.getAbsolutePath());

		grid.addHierarchyColumn(File::getAbsolutePath).setHeader("name").setResizable(true);
		grid.addColumn(this, "last Modified").setHeader("last Modified").setResizable(true);
		grid.addColumn(new AccessProvider(), "rwx").setHeader("rwx").setResizable(true);
		grid.addColumn(new SizeProvider(), "size").setHeader("size").setResizable(true);

		grid.addExpandListener(this);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(this);

		grid.getTreeData().addItem(null, rootFile);
		addChildren(grid.getTreeData(), rootFile);

		add(grid);
		add(fileContentsView);
		fileContentsView.setSizeFull();
	}

	private TreeData<File> addChildren(final TreeData<File> treeData, final File directory) {
		int childrenAdded = 0;
		// set children
		for (File file : directory.listFiles()) {
			treeData.addItem(directory, file);
			childrenAdded++;
		}
		log.info("Added " + childrenAdded + " children to " + directory.getAbsolutePath());

		return treeData;
	}

	@Override // ComponentEventListener
	public void onComponentEvent(final ExpandEvent<File, TreeGrid<File>> event) {
		TreeData<File> treeData = event.getSource().getTreeData();
		for (final File expandedItem : event.getItems()) {
			log.info("Expaned " + expandedItem.getAbsolutePath());
			if (expandedItem.isDirectory()) {
				for (File file : expandedItem.listFiles()) {
					if (file.isDirectory()) {
						File[] listedFiles = file.listFiles();
						if (listedFiles != null) {
							for (File l : listedFiles) {
								if (!treeData.contains(l)) {
									treeData.addItem(file, l);
								}
							}
						}
					}
				}
			}
		}

	}

	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			.withZone(ZoneId.systemDefault());

	@Override // ValueProvider<File, String>
	public String apply(File source) {
		return DATE_TIME_FORMATTER.format(Instant.ofEpochMilli(source.lastModified()));
	}

	static class AccessProvider implements ValueProvider<File, String> {

		@Override
		public String apply(File source) {
			return (source.canRead() ? "r" : "-") + (source.canWrite() ? "w" : "-") + (source.canExecute() ? "x" : "-");
		}

	}

	static class SizeProvider implements ValueProvider<File, Long> {

		@Override
		public Long apply(File source) {
			if (source.isDirectory()) {
				File[] listFiles = source.listFiles();
				if (listFiles != null) {
					return Long.valueOf(listFiles.length);
				}
			} else {
				return Long.valueOf(source.length());
			}

			return Long.valueOf(0);
		}

	}

	@Override // HasUrlParameter<String>
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		Location location = event.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();

		Map<String, List<String>> parametersMap = queryParameters.getParameters();

		List<String> list = parametersMap.get("root");
		if (list != null && list.size() > 0) {
			log.info("root is " + list);
			File file = new File(list.get(0));
			if (file.exists()) {
				rootFile = file;
			}
		}
		initData();
	}

	@Override // SelectionListener<Grid<File>, File>
	public void selectionChange(SelectionEvent<Grid<File>, File> event) {
		Optional<File> firstSelectedItem = event.getFirstSelectedItem();
		if (firstSelectedItem.isPresent()) {
			fileContentsView.setValue(readFile(firstSelectedItem.get()));
		}
	}

	static String readFile(File file) {
		return readFile(file, StandardCharsets.UTF_8);
	}
	
	static String readFile(File file, Charset encoding) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			return e.getLocalizedMessage();
		}
		return new String(encoded, encoding);
	}
}
