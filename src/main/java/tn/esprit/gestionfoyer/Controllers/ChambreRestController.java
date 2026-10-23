package tn.esprit.gestionfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionfoyer.entity.Chambre;
import tn.esprit.gestionfoyer.service.IChambreService;

import java.util.List;

@RestController

@RequestMapping("/Chambre")
public class ChambreRestController {
    @Autowired

    private final IChambreService chambreService;

    public ChambreRestController(IChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @GetMapping("/retrieve-all-chambres")
    public List<Chambre> retrieveAllChambres() {
        return chambreService.retrieveAllChambres();
    }

    @GetMapping("/retrieve-chambre/{id}")
    public Chambre retrieveChambre(@PathVariable("id") Long id) {
        return chambreService.retrieveChambre(id);
    }

    @PostMapping("/add-chambre")
    @ResponseStatus(HttpStatus.CREATED)
    public Chambre addChambre(@RequestBody Chambre chambre) {
        return chambreService.addChambre(chambre);
    }

    @DeleteMapping("/remove-chambre/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeChambre(@PathVariable("id") Long id) {
        chambreService.removeChambre(id);
    }

    @PutMapping("/modify-chambre")
    public Chambre modifyChambre(@RequestBody Chambre chambre) {
        return chambreService.modifyChambre(chambre);
    }

    @PutMapping("/assign-chambre-to-bloc/{chambreId}/{blocId}")
    public Chambre assignChambreToBloc(
            @PathVariable("chambreId") Long chambreId,
            @PathVariable("blocId") Long blocId) {
        return chambreService.assignChambreToBloc(chambreId, blocId);
    }
}