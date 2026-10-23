package tn.esprit.gestionfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionfoyer.entity.Etudiant;
import tn.esprit.gestionfoyer.service.IEtudiantService;

import java.util.List;

@RestController

@RequestMapping("/Etudiant")
public class EtudiantRestController {
    @Autowired

    private final IEtudiantService etudiantService;

    public EtudiantRestController(IEtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping("/retrieve-all-etudiants")
    public List<Etudiant> retrieveAllEtudiants() {
        return etudiantService.retrieveAllEtudiants();
    }

    @GetMapping("/retrieve-etudiant/{id}")
    public Etudiant retrieveEtudiant(@PathVariable("id") Long id) {
        return etudiantService.retrieveEtudiant(id);
    }

    @PostMapping("/add-etudiant")
    @ResponseStatus(HttpStatus.CREATED)
    public Etudiant addEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.addEtudiant(etudiant);
    }

    @DeleteMapping("/remove-etudiant/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEtudiant(@PathVariable("id") Long id) {
        etudiantService.removeEtudiant(id);
    }

    @PutMapping("/modify-etudiant")
    public Etudiant modifyEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.modifyEtudiant(etudiant);
    }
}