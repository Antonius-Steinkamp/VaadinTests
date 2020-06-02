package de.person.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Simples Backup von Daten.
 * 
 * Die Daten werden im XML-Format abgelegt. Es stört auch nicht, wenn Zyklen in
 * den Daten sind.
 * 
 * @author Antonius
 *
 * @param <T> Typ der abgelegten Daten
 */
public class BackupXStream<T> implements Db<T> {
	private static XStream x = new XStream();
	static {
		x.addPermission(AnyTypePermission.ANY);
	}

	private String filename = DB_FILENAME + ".xstream.zip";

	@Override
	public boolean backup(T object) {
		try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
				Writer writer = new OutputStreamWriter(zip);) {
			zip.putNextEntry(new ZipEntry(DB_FILENAME));
			x.toXML(object, writer);
			x.toXML(object, System.out);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		return true;

	}

//	@SuppressWarnings("unchecked")
	@Override
	public T restore(Class<T> classOfT) {
		T result = null;

		File f = new File(filename);
		if (f.exists()) {

			try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)));
					Reader reader = new InputStreamReader(zip);) {
				zip.getNextEntry();
				result = (T) x.fromXML(reader);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (result == null) {
			result = createOne(classOfT);
		}

		return result;
	}

}
