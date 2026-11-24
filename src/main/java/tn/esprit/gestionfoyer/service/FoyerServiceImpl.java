package tn.esprit.gestionfoyer.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.gestionfoyer.entity.Foyer;
import tn.esprit.gestionfoyer.entity.Universite;
import tn.esprit.gestionfoyer.repository.FoyerRepository;
import tn.esprit.gestionfoyer.repository.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerServiceImpl implements IFoyerService {

    private final FoyerRepository foyerRepository;
    private final UniversiteRepository universiteRepository;

    @Override
    public List<Foyer> retrieveAllFoyers() {
        return foyerRepository.findAll();
    }

    @Override
    public Foyer retrieveFoyer(Long id) {
        return foyerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Foyer introuvable avec l'id : " + id
                ));
    }

    @Override
    public Foyer addFoyer(Foyer foyer) {
        return foyerRepository.save(foyer);
    }

    @Override
    public void removeFoyer(Long id) {
        foyerRepository.deleteById(id);
    }

    @Override
    public Foyer modifyFoyer(Foyer foyer) {
        return foyerRepository.save(foyer);
    }

    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Université introuvable avec l'id : " + idUniversite
                ));

        if (universite.getFoyer() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "L'université " + idUniversite + " est déjà associée à un foyer"
            );
        }

        foyer.setUniversite(universite);
        if (foyer.getBlocs() != null) {
            foyer.getBlocs().forEach(bloc -> bloc.setFoyer(foyer));
        }
        universite.setFoyer(foyer);

        return foyerRepository.save(foyer);
    }
}