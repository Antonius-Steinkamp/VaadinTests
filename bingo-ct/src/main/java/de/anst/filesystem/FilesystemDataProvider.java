package de.anst.filesystem;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.function.SerializablePredicate;

import lombok.extern.java.Log;


/**
 * FileSystemDataProvider is data provider that can map file system to hierarchical
 * data, so that it can be used e.g. with Tree and TreeGrid
 * 
 * @author Tatu Lind
 *
 */
@Log
public class FilesystemDataProvider extends TreeDataProvider<File> {

    private final boolean recursive;
    private final FilesystemData treeData;
    
    /**
     * Construct new FilesystemDataProvider with given FilesystemData 
     * 
     * @param treeData The data model
     */
    public FilesystemDataProvider(final FilesystemData treeData) {
    	super(treeData);
    	recursive = treeData.isRecursive();
    	this.treeData = treeData;
    }

    /**
     * Get the count of children based on query
     * 
     * @param query A query
     * 
     * @return The count of children
     */
    @Override
    public int getChildCount(final HierarchicalQuery<File, SerializablePredicate<File>> query) {
    	int result = 0;
    	
    	final File parent = query.getParentOptional().orElse(treeData.getRootItems().get(0));
    	if (!parent.isFile()) {
    		result = (int) fetchChildren(query).count();
    	}
    	log.info("getChildCount (" + query + ") is " + result);
    	
    	return result;
    }    
    
    /**
     * Check if the file has children or not
     * 
     * @param item The File 
     * 
     * @return True if the File has children (i.e. it is a non empty directory)
     */
    @Override
    public boolean hasChildren(File item) {
    	boolean result;
    	if (!isInMemory()) {
    		result = item.isDirectory() && !treeData.getChildrenFromFilesystem(item).isEmpty();
    	} else {
    		result = super.hasChildren(item);
    	}
    	
    	log.info("hasChildren (" + item.getAbsolutePath() + " is " + result);
    	
    	return result;
    }

    /**
     * Return the files in directory as a Stream based on query
     *  
     * @param query A query
     * @return A stream of Files
     */
    @Override
    public Stream<File> fetchChildren(final HierarchicalQuery<File, SerializablePredicate<File>> query) {
    	log.info("fetchChildren(" +  ") isInMemory: " + isInMemory());
    	if (!isInMemory()) {    		
        	File parent = query.getParentOptional().orElse(treeData.getRootItems().get(0));
			if (treeData.getChildren(parent).isEmpty()) {
	        	final List<File> files = treeData.getChildrenFromFilesystem(parent);
				treeData.addItems(parent, files);
	        	log.info("fetchChildren(" + parent.getAbsolutePath() + "): " + files.size());
				return files.stream();
			} else {
				return super.fetchChildren(query);
			}
    	} else {
    		return super.fetchChildren(query);
    	}
    }
    
    /**
     * FilesystemDataProvider is fully in-memory if it is constructed
     * recursively, otherwise it is progressively lazy
     * 
     * @return boolean value
     */
	@Override
	public boolean isInMemory() {
		return recursive;
	}

}

