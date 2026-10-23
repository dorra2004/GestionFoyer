package tn.esprit.gestionfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionfoyer.entity.Bloc;
import tn.esprit.gestionfoyer.service.IBlocService;

import java.util.List;

@RestController

@RequestMapping("/Bloc")
public class BlocRestController {
    @Autowired

    private final IBlocService blocService;

    public BlocRestController(IBlocService blocService) {
        this.blocService = blocService;
    }

    @GetMapping("/retrieve-all-blocs")
    public List<Bloc> retrieveAllBlocs() {
        return blocService.retrieveAllBlocs();
    }

    @GetMapping("/retrieve-bloc/{id}")
    public Bloc retrieveBloc(@PathVariable("id") Long id) {
        return blocService.retrieveBloc(id);
    }

    @PostMapping("/add-bloc")
    @ResponseStatus(HttpStatus.CREATED)
    public Bloc addBloc(@RequestBody Bloc bloc) {
        return blocService.addBloc(bloc);
    }

    @DeleteMapping("/remove-bloc/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBloc(@PathVariable("id") Long id) {
        blocService.removeBloc(id);
    }

    @PutMapping("/modify-bloc")
    public Bloc modifyBloc(@RequestBody Bloc bloc) {
        return blocService.modifyBloc(bloc);
    }
}