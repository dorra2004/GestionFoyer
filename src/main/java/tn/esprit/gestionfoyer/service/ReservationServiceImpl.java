package tn.esprit.gestionfoyer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.gestionfoyer.entity.Bloc;
import tn.esprit.gestionfoyer.entity.Chambre;
import tn.esprit.gestionfoyer.entity.Etudiant;
import tn.esprit.gestionfoyer.entity.Reservation;
import tn.esprit.gestionfoyer.entity.TypeChambre;
import tn.esprit.gestionfoyer.repository.BlocRepository;
import tn.esprit.gestionfoyer.repository.EtudiantRepository;
import tn.esprit.gestionfoyer.repository.ReservationRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final BlocRepository blocRepository;
    private final EtudiantRepository etudiantRepository;

    @Override
    public List<Reservation> retrieveAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation retrieveReservation(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation introuvable avec l'id : " + id
                ));
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        if (reservation.getIdReservation() == null) {
            reservation.setIdReservation(UUID.randomUUID().toString());
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public void removeReservation(String id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation modifyReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation ajouterReservation(long idBloc, long cinEtudiant) {
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bloc introuvable avec l'id : " + idBloc
                ));
        Etudiant etudiant = etudiantRepository.findByCin(cinEtudiant)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Etudiant introuvable avec le CIN : " + cinEtudiant
                ));

        LocalDate today = LocalDate.now();
        int academicYearStart = today.getMonthValue() >= 9
                ? today.getYear()
                : today.getYear() - 1;
        LocalDate academicYearDate = LocalDate.of(academicYearStart, 9, 1);
        String academicYear = academicYearStart + "-" + (academicYearStart + 1);

        List<Chambre> chambres = bloc.getChambres() == null
                ? new ArrayList<>()
                : bloc.getChambres().stream()
                .sorted((first, second) -> first.getIdChambre().compareTo(second.getIdChambre()))
                .toList();

        for (Chambre chambre : chambres) {
            int capacity = capacityFor(chambre.getTypeC());
            Reservation reservation = chambre.getReservations() == null
                    ? null
                    : chambre.getReservations().stream()
                    .filter(existing -> existing.getAnneeUniversitaire() != null
                            && new Date(existing.getAnneeUniversitaire().getTime())
                            .toLocalDate().equals(academicYearDate))
                    .findFirst()
                    .orElse(null);

            if (reservation != null && reservation.getEtudiants() != null
                    && reservation.getEtudiants().contains(etudiant)) {
                return reservation;
            }

            int occupancy = reservation == null || reservation.getEtudiants() == null
                    ? 0
                    : reservation.getEtudiants().size();
            if (occupancy >= capacity) {
                continue;
            }

            if (reservation == null) {
                reservation = new Reservation();
                reservation.setIdReservation(
                        chambre.getNumeroChambre() + "-" + bloc.getNomBloc() + "-" + academicYear
                );
                reservation.setAnneeUniversitaire(Date.valueOf(academicYearDate));
                reservation.setEstValide(true);
                reservation.setChambre(chambre);
                reservation.setEtudiants(new HashSet<>());
            }

            reservation.getEtudiants().add(etudiant);
            if (etudiant.getReservations() == null) {
                etudiant.setReservations(new HashSet<>());
            }
            etudiant.getReservations().add(reservation);

            Reservation savedReservation = reservationRepository.save(reservation);
            chambre.setReservations(chambre.getReservations() == null
                    ? new HashSet<>()
                    : chambre.getReservations());
            chambre.getReservations().add(savedReservation);
            etudiantRepository.save(etudiant);
            return savedReservation;
        }

        throw new IllegalStateException(
                "Aucune chambre disponible dans le bloc " + idBloc
        );
    }

    @Override
    @Transactional
    public Reservation annulerReservation(long cinEtudiant) {
        Etudiant etudiant = etudiantRepository.findByCin(cinEtudiant)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Etudiant introuvable avec le CIN : " + cinEtudiant
                ));

        Reservation reservation = etudiant.getReservations() == null
                ? null
                : etudiant.getReservations().stream()
                .filter(Reservation::isEstValide)
                .max(Comparator.comparing(
                        Reservation::getAnneeUniversitaire,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ))
                .orElse(null);
        if (reservation == null) {
            throw new IllegalStateException(
                    "Aucune réservation active pour l'étudiant avec le CIN : " + cinEtudiant
            );
        }

        List<Etudiant> occupants = reservation.getEtudiants() == null
                ? List.of()
                : new ArrayList<>(reservation.getEtudiants());
        occupants.stream()
                .filter(occupant -> occupant.getReservations() != null)
                .forEach(occupant -> occupant.getReservations().remove(reservation));
        if (!occupants.isEmpty()) {
            etudiantRepository.saveAll(occupants);
        }

        Chambre chambre = reservation.getChambre();
        if (chambre != null && chambre.getReservations() != null) {
            chambre.getReservations().remove(reservation);
        }

        reservation.setEtudiants(new HashSet<>());
        reservation.setChambre(null);
        reservation.setEstValide(false);
        return reservationRepository.save(reservation);
    }

    private int capacityFor(TypeChambre type) {
        if (type == null) {
            throw new IllegalStateException("Le type de chambre doit être renseigné");
        }
        return switch (type) {
            case SIMPLE -> 1;
            case DOUBLE -> 2;
            case TRIPLE -> 3;
        };
    }
}