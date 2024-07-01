package ro.presidential.election;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonsRepository extends JpaRepository<Person, Long> {
    Optional<Person> getPersonByEmailAdress(String emailAdress);
    Optional<Person> getPersonByUsername(String username);
    List<Person> findAllByStatusOrderByNrOfVotesDesc(String status);
}
