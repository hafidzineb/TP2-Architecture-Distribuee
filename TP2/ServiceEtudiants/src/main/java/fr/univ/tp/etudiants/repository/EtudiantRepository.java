package fr.univ.tp.etudiants.repository;

import fr.univ.tp.etudiants.model.Etudiant;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class EtudiantRepository {

    private final Map<Integer, Etudiant> store = new ConcurrentHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(1);

    public List<Etudiant> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Etudiant> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public Etudiant save(Etudiant e) {
        if (e.getId() == null) {
            e.setId(seq.getAndIncrement());
        }
        store.put(e.getId(), e);
        return e;
    }

    public boolean delete(int id) {
        return store.remove(id) != null;
    }
}