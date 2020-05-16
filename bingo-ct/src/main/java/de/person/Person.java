package de.person;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
//@AllArgsConstructor
@NoArgsConstructor

public class Person {
    @NotNull // Validation API is required! Add it as a dependency on your project
    private Long id;

    @NotNull
    private String name;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @Past
    private LocalDate birthDate;

    @Email(message = "name@domain")
    private String email;

    @NotNull
    @Size(min=6, max=25, message="6 to 25 characters necessary")
    private String password;
    
    public Person(Long id, String name, LocalDate birthday, String email, String pass) {
    	this.id = id;
    	this.name = name;
    	this.birthDate = birthday;
    	this.email = email;
    	this.password = pass;
    	
    }
}
