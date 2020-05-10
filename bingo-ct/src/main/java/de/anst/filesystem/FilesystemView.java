package de.anst.filesystem;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.ExpandEvent;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
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
		HasUrlParameter<String> {

	private File rootFile = new File(System.getProperty("user.home"));
	private TreeGrid<File> grid = new TreeGrid<>();

	public FilesystemView() {
		add(new H1("Filesystem"));

	}

	private void initData() {
		log.info("Init for " + rootFile.getAbsolutePath());
		
		grid.addHierarchyColumn(File::getAbsolutePath).setHeader("name");
		grid.addColumn(this, "last Modified").setHeader("last Modified");
		grid.addColumn(new AccessProvider(), "rwx").setHeader("rwx");
		grid.addColumn(new SizeProvider(), "size").setHeader("size");
		
		grid.addExpandListener(this);

		grid.getTreeData().addItem(null, rootFile);
		addChildren(grid.getTreeData(), rootFile);

		add(grid);
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
			if ( source.isDirectory()) {
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
}
