package ro.presidential.election;

import java.util.List;

public interface IPersonsService {
    List<Person> getPersons();
    void createPerson(Person person);
    void updatePerson(Long id, Person person);
    void deletePerson(Long id);
}
