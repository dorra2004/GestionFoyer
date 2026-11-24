package tn.esprit.gestionfoyer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.gestionfoyer.entity.Bloc;
import tn.esprit.gestionfoyer.entity.Foyer;
import tn.esprit.gestionfoyer.entity.Universite;
import tn.esprit.gestionfoyer.repository.BlocRepository;
import tn.esprit.gestionfoyer.repository.UniversiteRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:gestionfoyer;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class FoyerServiceImplTest {

    @Autowired
    private IFoyerService foyerService;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Test
    void createsFoyerWithBlocksAndAssignsItToUniversity() {
        Universite universite = new Universite();
        universite.setNomUniversite("Université de test");
        universite.setAdresse("Tunis");
        universite = universiteRepository.save(universite);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100L);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer de test");
        foyer.setCapaciteFoyer(100L);
        foyer.setBlocs(Set.of(bloc));

        Foyer savedFoyer = foyerService.ajouterFoyerEtAffecterAUniversite(
                foyer, universite.getIdUniversite());

        assertNotNull(savedFoyer.getIdFoyer());
        assertEquals(universite.getIdUniversite(), savedFoyer.getUniversite().getIdUniversite());
        assertEquals(1, blocRepository.count());
        Bloc savedBloc = blocRepository.findAll().get(0);
        assertEquals(savedFoyer.getIdFoyer(), savedBloc.getFoyer().getIdFoyer());
        assertEquals(savedFoyer.getIdFoyer(), universite.getFoyer().getIdFoyer());
    }

    @Test
    void reportsNotFoundWhenUniversityDoesNotExist() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> foyerService.ajouterFoyerEtAffecterAUniversite(new Foyer(), 1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
