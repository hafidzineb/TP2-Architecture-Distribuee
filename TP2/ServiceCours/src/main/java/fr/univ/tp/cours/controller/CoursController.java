package fr.univ.tp.cours.controller;

import fr.univ.tp.cours.model.Cours;
import fr.univ.tp.cours.model.EtudiantDto;
import fr.univ.tp.cours.repository.CoursRepository;
import fr.univ.tp.cours.service.EtudiantClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/cours")
public class CoursController {

    private final CoursRepository repo;
    private final EtudiantClient etudiantClient;

    public CoursController(CoursRepository repo, EtudiantClient etudiantClient) {
        this.repo = repo;
        this.etudiantClient = etudiantClient;
    }

    @GetMapping
    public List<Cours> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Cours get(@PathVariable int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours non trouvé"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cours create(@RequestBody Cours c) {
        c.setId(null);
        return repo.save(c);
    }

    @PutMapping("/{id}")
    public Cours update(@PathVariable int id, @RequestBody Cours c) {
        repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours non trouvé"));
        c.setId(id);
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        if (!repo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cours non trouvé");
        }
    }

    // Communication inter-services : chaque cours avec le nom de son étudiant
    @GetMapping("/enrichis")
    public List<Map<String, Object>> getCoursEnrichis() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Cours c : repo.findAll()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", c.getId());
            m.put("titre", c.getTitre());
            m.put("etudiantId", c.getEtudiantId());
            try {
                EtudiantDto et = etudiantClient.getEtudiantById(c.getEtudiantId());
                m.put("etudiantNom", et.getNom());
            } catch (Exception ex) {
                // étudiant introuvable ou service arrêté : on répond quand même
                m.put("etudiantNom", "INCONNU");
                m.put("erreur", "Service Etudiants indisponible ou étudiant introuvable");
            }
            result.add(m);
        }
        return result;
    }
}