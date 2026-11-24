package tn.esprit.gestionfoyer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.gestionfoyer.entity.Bloc;
import tn.esprit.gestionfoyer.entity.Chambre;
import tn.esprit.gestionfoyer.entity.Etudiant;
import tn.esprit.gestionfoyer.entity.Reservation;
import tn.esprit.gestionfoyer.entity.TypeChambre;
import tn.esprit.gestionfoyer.repository.BlocRepository;
import tn.esprit.gestionfoyer.repository.EtudiantRepository;
import tn.esprit.gestionfoyer.repository.ReservationRepository;
import tn.esprit.gestionfoyer.repository.ChambreRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:gestionfoyer;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class ReservationServiceImplTest {

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Test
    void createsReservationWithRequiredFormatAndEnforcesRoomCapacity() {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(12L);
        chambre.setTypeC(TypeChambre.DOUBLE);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");
        bloc.setCapaciteBloc(20L);
        chambre.setBloc(bloc);
        bloc.setChambres(Set.of(chambre));
        bloc = blocRepository.save(bloc);
        long blocId = bloc.getIdBloc();

        Etudiant firstStudent = new Etudiant();
        firstStudent.setCin(12345678L);
        firstStudent = etudiantRepository.save(firstStudent);
        Etudiant secondStudent = new Etudiant();
        secondStudent.setCin(87654321L);
        etudiantRepository.save(secondStudent);
        Etudiant thirdStudent = new Etudiant();
        thirdStudent.setCin(11223344L);
        etudiantRepository.save(thirdStudent);

        Reservation firstReservation = reservationService.ajouterReservation(
                blocId, firstStudent.getCin());

        int academicYearStart = LocalDate.now().getMonthValue() >= 9
                ? LocalDate.now().getYear()
                : LocalDate.now().getYear() - 1;
        assertEquals("12-BlocA-" + academicYearStart + "-" + (academicYearStart + 1),
                firstReservation.getIdReservation());
        assertTrue(firstReservation.isEstValide());
        assertEquals(TypeChambre.DOUBLE, firstReservation.getChambre().getTypeC());
        assertEquals(1, firstReservation.getEtudiants().size());

        Reservation secondReservation = reservationService.ajouterReservation(
                blocId, secondStudent.getCin());
        assertEquals(firstReservation.getIdReservation(), secondReservation.getIdReservation());
        assertEquals(2, secondReservation.getEtudiants().size());
        assertThrows(IllegalStateException.class, () -> reservationService.ajouterReservation(
                blocId, thirdStudent.getCin()));
        assertEquals(1, reservationRepository.count());
    }

    @Test
    void cancellationInvalidatesReservationAndReleasesRoomAndAllOccupants() {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(21L);
        chambre.setTypeC(TypeChambre.DOUBLE);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocB");
        bloc.setCapaciteBloc(20L);
        chambre.setBloc(bloc);
        bloc.setChambres(Set.of(chambre));
        bloc = blocRepository.save(bloc);
        long blocId = bloc.getIdBloc();

        Etudiant firstStudent = new Etudiant();
        firstStudent.setCin(22334455L);
        firstStudent = etudiantRepository.save(firstStudent);
        Etudiant secondStudent = new Etudiant();
        secondStudent.setCin(55443322L);
        etudiantRepository.save(secondStudent);

        Reservation reservation = reservationService.ajouterReservation(blocId, firstStudent.getCin());
        reservationService.ajouterReservation(blocId, secondStudent.getCin());
        long roomId = reservation.getChambre().getIdChambre();

        Reservation cancelled = reservationService.annulerReservation(firstStudent.getCin());

        assertTrue(!cancelled.isEstValide());
        assertNull(cancelled.getChambre());
        assertTrue(cancelled.getEtudiants().isEmpty());
        assertTrue(etudiantRepository.findByCin(firstStudent.getCin())
                .orElseThrow().getReservations().isEmpty());
        assertTrue(etudiantRepository.findByCin(secondStudent.getCin())
                .orElseThrow().getReservations().isEmpty());
        assertTrue(chambreRepository.findById(roomId)
                .orElseThrow().getReservations().isEmpty());
        assertTrue(!reservationRepository.findById(reservation.getIdReservation())
                .orElseThrow().isEstValide());
    }
}
