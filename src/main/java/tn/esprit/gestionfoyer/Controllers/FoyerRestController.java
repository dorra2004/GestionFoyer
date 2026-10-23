package tn.esprit.gestionfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionfoyer.entity.Foyer;
import tn.esprit.gestionfoyer.service.IFoyerService;

import java.util.List;

@RestController

@RequestMapping("/Foyer")
public class FoyerRestController {
    @Autowired

    private final IFoyerService foyerService;

    public FoyerRestController(IFoyerService foyerService) {
        this.foyerService = foyerService;
    }

    @GetMapping("/retrieve-all-foyers")
    public List<Foyer> retrieveAllFoyers() {
        return foyerService.retrieveAllFoyers();
    }

    @GetMapping("/retrieve-foyer/{id}")
    public Foyer retrieveFoyer(@PathVariable("id") Long id) {
        return foyerService.retrieveFoyer(id);
    }

    @PostMapping("/add-foyer")
    @ResponseStatus(HttpStatus.CREATED)
    public Foyer addFoyer(@RequestBody Foyer foyer) {
        return foyerService.addFoyer(foyer);
    }

    @DeleteMapping("/remove-foyer/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFoyer(@PathVariable("id") Long id) {
        foyerService.removeFoyer(id);
    }

    @PutMapping("/modify-foyer")
    public Foyer modifyFoyer(@RequestBody Foyer foyer) {
        return foyerService.modifyFoyer(foyer);
    }
}