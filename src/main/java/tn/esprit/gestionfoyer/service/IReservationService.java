package tn.esprit.gestionfoyer.service;

import tn.esprit.gestionfoyer.entity.Reservation;

import java.util.List;

public interface IReservationService {

    List<Reservation> retrieveAllReservations();

    Reservation retrieveReservation(String id);

    Reservation addReservation(Reservation reservation);

    void removeReservation(String id);

    Reservation modifyReservation(Reservation reservation);

    Reservation ajouterReservation(long idBloc, long cinEtudiant);

    Reservation annulerReservation(long cinEtudiant);
}