package de.person;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.vaadin.crudui.crud.CrudListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.java.Log;

@SuppressWarnings("serial")
@Log
public class PersonService implements CrudListener<Person> {

	private static List<Person> allPersons = new ArrayList<>();

	public PersonService() {
		if (allPersons.size() == 0) {
			allPersons.add(new Person(Long.valueOf(1), "Antonius", LocalDate.now(), "ansr@test.com", "pass"));
			allPersons.add(new Person(Long.valueOf(2), "Bntonius", LocalDate.now(), "ansr@test.com", "pass"));

			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			try {
				String resourceFilename = "/de/person/PersonData.json";
				URL resource = this.getClass().getResource(resourceFilename);
				if (resource != null) {
					allPersons = Arrays.asList(mapper.readValue(resource.openStream(), Person[].class));
				} else {
					log.info(String.format("Resource %s not found", resourceFilename));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			log.info("Found " + allPersons.size() + " Persons");
		}
	}

	@Override
	public Person add(final Person p) {
		allPersons.add(p);
		return p;
	}

	@Override
	public Collection<Person> findAll() {
		return allPersons;
	}

	@Override
	public Person update(Person p) {
		if (allPersons.contains(p)) {
			allPersons.remove(p);
			allPersons.add(p);
		}

		return p;
	}

	@Override
	public void delete(Person p) {
		allPersons.remove(p);
	}
}
