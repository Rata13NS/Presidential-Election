package ro.presidential.election;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonsService implements IPersonsService {
    private final PersonsRepository personsRepository;

    public PersonsService(PersonsRepository personsRepository) {
        this.personsRepository = personsRepository;
    }

    @Override
    public List<Person> getPersons() {
        return personsRepository.findAll();
    }

    @Override
    public void createPerson(Person person) {
        validateUsername(person.getUsername());
        validateEmailAdress(person.getEmailAdress());
        person.setHasVoted(false);
        personsRepository.save(person);
    }

    public Person getPersonById(Long id) {
        return personsRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Person with id %d not found", id)));
    }

    @Override
    public void updatePerson(Long id, Person person) {
        Person personToUpdate = personsRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Person with id %d not found", id)));

        personToUpdate.setUsername(person.getUsername());
        personToUpdate.setPassword(person.getPassword());
        personToUpdate.setPhoneNumber(person.getPhoneNumber());
        personToUpdate.setEmailAdress(person.getEmailAdress());
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setAge(person.getAge());
        personToUpdate.setProfession(person.getProfession());
        personToUpdate.setStatus("Candidate");
        personToUpdate.setBibliography(person.getBibliography());
        personToUpdate.setMotto(person.getMotto());

        personsRepository.save(personToUpdate);
    }

    private void validateUsername(String username) {
        Optional<Person> personOptional = personsRepository.getPersonByUsername(username);
        if(personOptional.isPresent()) {
            throw new IllegalStateException(String.format("Person with username %s already exists", username));
        }
    }

    private void validateEmailAdress(String emailAdress) {
        Optional<Person> personOptional = personsRepository.getPersonByEmailAdress(emailAdress);
        if(personOptional.isPresent()) {
            throw new IllegalStateException(String.format("Person with email adress %s already exists", emailAdress));
        }
    }

    public void deletePerson(Long id) {
        boolean personExists = personsRepository.existsById(id);
        if(!personExists) {
            throw new IllegalStateException(String.format("Person with id %d not found", id));
        }
        personsRepository.deleteById(id);
    }

    public void validateExistingUsername(String username) {
        Optional<Person> personOptional = personsRepository.getPersonByUsername(username);
        if (!personOptional.isPresent()) {
            throw new IllegalStateException(String.format("Username %s does not exist", username));
        }
    }

    public void validatePassword(String username, String password) {
        Optional<Person> personOptional = personsRepository.getPersonByUsername(username);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            if (!person.getPassword().equals(password)) {
                throw new IllegalStateException("Incorrect password");
            }
        }
    }

    public Optional<Person> getPersonByUsername(String username) {
        return personsRepository.getPersonByUsername(username);
    }

    public void submitCandidateRequest(String username, String motto, String bibliography) {
        Optional<Person> optionalPerson = personsRepository.getPersonByUsername(username);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setStatus("Candidate");
            person.setMotto(motto);
            person.setBibliography(bibliography);
            personsRepository.save(person);
        } else {
            throw new IllegalStateException(String.format("User with username %s not found", username));
        }
    }

    public List<Person> getAllCandidatesOrderedByVotes() {
        return personsRepository.findAllByStatusOrderByNrOfVotesDesc("Candidate");
    }

    public void incrementVotes(Long id) {
        Person person = getPersonById(id);
        person.incrementVotes();
        personsRepository.save(person);
    }

    public List<Person> getTopCandidates() {
        return personsRepository.findAllByStatusOrderByNrOfVotesDesc("Candidate").stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public void sendCandidateRequest(String username, Long candidateId) {
        Optional<Person> optionalPerson = personsRepository.getPersonByUsername(username);
        Optional<Person> optionalCandidate = personsRepository.findById(candidateId);
        if (optionalPerson.isPresent() && optionalCandidate.isPresent()) {
            Person person = optionalPerson.get();
            Person candidate = optionalCandidate.get();
            person.setMotto(candidate.getMotto());
            person.setBibliography(candidate.getBibliography());
            person.setStatus("Candidate");
            personsRepository.save(person);
        } else {
            throw new IllegalStateException("User or candidate not found");
        }
    }

    public void setHasVoted(Long userId) {
        Person person = getPersonById(userId);
        person.setHasVoted(true);
        personsRepository.save(person);
    }
}
