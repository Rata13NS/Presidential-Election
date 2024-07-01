package ro.presidential.election;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/persons")
public class PersonsController {
    private final PersonsService personsService;

    public PersonsController(PersonsService personsService) {
        this.personsService = personsService;
    }

    @GetMapping
    @ResponseBody
    public List<Person> getPersons() {
        return personsService.getPersons();
    }

    @PostMapping
    @ResponseBody
    public void createPerson(@RequestBody Person person) {
        personsService.createPerson(person);
    }

    @PutMapping(path = "{id}")
    @ResponseBody
    public void updatePerson(@PathVariable Long id, @RequestBody Person person) {
        personsService.updatePerson(id, person);
    }

    @DeleteMapping(path = "{id}")
    @ResponseBody
    public void deletePerson(@PathVariable Long id) {
        personsService.deletePerson(id);
    }

    @RequestMapping("/log-in")
    public ModelAndView showLogInPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logInPage");
        return modelAndView;
    }

    @PostMapping("/log-in")
    public String logIn(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        try {
            personsService.validateExistingUsername(username);
            personsService.validatePassword(username, password);
            session.setAttribute("username", username);
            return "redirect:/persons/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "logInPage";
        }
    }

    @RequestMapping("/create-account")
    public String showCreateAccountPage(Model model) {
        model.addAttribute("person", new Person());
        return "createAccountPage";
    }

    @PostMapping("/create-account")
    public String createAccount(@ModelAttribute Person person, @RequestParam String confirmPassword, Model model, HttpSession session) {
        try {
            if (!person.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match!");
                return "createAccountPage";
            }

            person.setStatus("voter");
            person.setBibliography("");
            person.setMotto("");
            person.setNrOfVotes(0);

            personsService.createPerson(person);

            session.setAttribute("username", person.getUsername());

            return "redirect:/persons/home";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "createAccountPage";
        }
    }

    @RequestMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            Optional<Person> optionalPerson = personsService.getPersonByUsername(currentUsername);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                model.addAttribute("person", person);

                List<Person> topCandidates = personsService.getTopCandidates();
                model.addAttribute("topCandidates", topCandidates);

                return "homePage";
            }
        }
        return "errorPage";
    }

    @RequestMapping("/my-profile")
    public String showMyProfilePage(Model model, HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            Optional<Person> optionalPerson = personsService.getPersonByUsername(currentUsername);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                model.addAttribute("person", person);
                return "myProfilePage";
            }
        }
        return "errorPage";
    }

    @GetMapping("/my-profile/{id}")
    public String showMyProfilePage(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Person> optionalPerson = Optional.ofNullable(personsService.getPersonById(id));
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            model.addAttribute("person", person);
            return "myProfilePage";
        }
        return "errorPage";
    }

    @RequestMapping("/list-of-candidates")
    public ModelAndView showAllCandidatesPage(Model model, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            Optional<Person> optionalPerson = personsService.getPersonByUsername(currentUsername);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                modelAndView.addObject("person", person);
            }
        }

        List<Person> candidates = personsService.getAllCandidatesOrderedByVotes();
        modelAndView.addObject("candidates", candidates);

        modelAndView.setViewName("allCandidatesPage");
        return modelAndView;
    }

    @PostMapping("/increment-votes/{id}")
    public String incrementVotes(@PathVariable Long id, HttpSession session, Model model) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            Optional<Person> optionalPerson = personsService.getPersonByUsername(currentUsername);
            if (optionalPerson.isPresent()) {
                Person voter = optionalPerson.get();
                if (!voter.isHasVoted()) {
                    personsService.incrementVotes(id);
                    personsService.setHasVoted(voter.getId());
                } else {
                    model.addAttribute("error", "You already voted for a candidate");
                }
                return "redirect:/persons/list-of-candidates";
            }
        }
        return "errorPage";
    }

    @PostMapping("/candidate-request")
    public String submitCandidateRequest(@RequestParam String motto,
                                         @RequestParam String bibliography,
                                         HttpSession session, Model model) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            try {
                personsService.submitCandidateRequest(currentUsername, motto, bibliography);
                return "redirect:/persons/my-profile";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "homePage";
            }
        } else {
            model.addAttribute("error", "User not logged in");
            return "logInPage";
        }
    }

    @PostMapping("/send-request/{id}")
    public String sendRequest(@PathVariable Long id, HttpSession session, Model model) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            try {
                personsService.sendCandidateRequest(currentUsername, id);
                return "redirect:/persons/home";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "homePage";
            }
        } else {
            model.addAttribute("error", "User not logged in");
            return "logInPage";
        }
    }

    @GetMapping("/candidate-profile/{id}")
    public String showCandidateProfilePage(@PathVariable Long id, Model model) {
        Optional<Person> optionalPerson = Optional.ofNullable(personsService.getPersonById(id));
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            model.addAttribute("person", person);
            return "candidateProfilePage";
        }
        return "errorPage";
    }
}
