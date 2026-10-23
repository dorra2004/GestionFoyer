package tn.esprit.gestionfoyer.service;

import tn.esprit.gestionfoyer.entity.Foyer;

import java.util.List;

public interface IFoyerService {

    List<Foyer> retrieveAllFoyers();

    Foyer retrieveFoyer(Long id);

    Foyer addFoyer(Foyer foyer);

    void removeFoyer(Long id);

    Foyer modifyFoyer(Foyer foyer);
}