package tn.esprit.gestionfoyer.service;

import tn.esprit.gestionfoyer.entity.Etudiant;

import java.util.List;

public interface IEtudiantService {

    List<Etudiant> retrieveAllEtudiants();

    Etudiant retrieveEtudiant(Long id);

    Etudiant addEtudiant(Etudiant etudiant);

    void removeEtudiant(Long id);

    Etudiant modifyEtudiant(Etudiant etudiant);
}