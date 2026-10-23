package tn.esprit.gestionfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionfoyer.entity.Reservation;
import tn.esprit.gestionfoyer.service.IReservationService;

import java.util.List;

@RestController

@RequestMapping("/Reservation")
public class ReservationRestController {
    @Autowired

    private final IReservationService reservationService;

    public ReservationRestController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/retrieve-all-reservations")
    public List<Reservation> retrieveAllReservations() {
        return reservationService.retrieveAllReservations();
    }

    @GetMapping("/retrieve-reservation/{id}")
    public Reservation retrieveReservation(@PathVariable("id") String id) {
        return reservationService.retrieveReservation(id);
    }

    @PostMapping("/add-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation addReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    @DeleteMapping("/remove-reservation/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReservation(@PathVariable("id") String id) {
        reservationService.removeReservation(id);
    }

    @PutMapping("/modify-reservation")
    public Reservation modifyReservation(
            @RequestBody Reservation reservation) {
        return reservationService.modifyReservation(reservation);
    }
}