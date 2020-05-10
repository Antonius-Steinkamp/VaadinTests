package de.anst.filesystem;

public class Person {
    private String name;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Person(String name, Person parent) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public String toTree() {
    	return "t-" + name;
    }

}