package tn.esprit.gestionfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionfoyer.entity.Universite;
import tn.esprit.gestionfoyer.service.IUniversiteService;

import java.util.List;

@RestController

@RequestMapping("/Universite")
public class UniversiteRestController {
    @Autowired

    private final IUniversiteService universiteService;

    public UniversiteRestController(IUniversiteService universiteService) {
        this.universiteService = universiteService;
    }

    @GetMapping("/retrieve-all-universites")
    public List<Universite> retrieveAllUniversites() {
        return universiteService.retrieveAllUniversites();
    }

    @GetMapping("/retrieve-universite/{id}")
    public Universite retrieveUniversite(@PathVariable("id") Long id) {
        return universiteService.retrieveUniversite(id);
    }

    @PostMapping("/add-universite")
    @ResponseStatus(HttpStatus.CREATED)
    public Universite addUniversite(@RequestBody Universite universite) {
        return universiteService.addUniversite(universite);
    }

    @DeleteMapping("/remove-universite/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUniversite(@PathVariable("id") Long id) {
        universiteService.removeUniversite(id);
    }

    @PutMapping("/modify-universite")
    public Universite modifyUniversite(@RequestBody Universite universite) {
        return universiteService.modifyUniversite(universite);
    }
}