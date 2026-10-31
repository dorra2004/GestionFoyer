package tn.esprit.gestionfoyer.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tn.esprit.gestionfoyer.service.IBlocService;
import tn.esprit.gestionfoyer.service.IChambreService;
import tn.esprit.gestionfoyer.service.IEtudiantService;
import tn.esprit.gestionfoyer.service.IFoyerService;
import tn.esprit.gestionfoyer.service.IReservationService;
import tn.esprit.gestionfoyer.service.IUniversiteService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        BlocRestController.class,
        ChambreRestController.class,
        EtudiantRestController.class,
        FoyerRestController.class,
        ReservationRestController.class,
        UniversiteRestController.class
})
class RestEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IBlocService blocService;

    @MockitoBean
    private IChambreService chambreService;

    @MockitoBean
    private IEtudiantService etudiantService;

    @MockitoBean
    private IFoyerService foyerService;

    @MockitoBean
    private IReservationService reservationService;

    @MockitoBean
    private IUniversiteService universiteService;

    @Test
    void blocCrudEndpoints() throws Exception {
        assertEndpoint(get("/Bloc/retrieve-all-blocs"), status().isOk());
        assertEndpoint(get("/Bloc/retrieve-bloc/1"), status().isOk());
        assertEndpoint(jsonPost("/Bloc/add-bloc"), status().isCreated());
        assertEndpoint(jsonPut("/Bloc/modify-bloc"), status().isOk());
        assertEndpoint(delete("/Bloc/remove-bloc/1"), status().isNoContent());
    }

    @Test
    void chambreEndpoints() throws Exception {
        assertEndpoint(get("/Chambre/retrieve-all-chambres"), status().isOk());
        assertEndpoint(get("/Chambre/retrieve-chambre/1"), status().isOk());
        assertEndpoint(jsonPost("/Chambre/add-chambre"), status().isCreated());
        assertEndpoint(jsonPut("/Chambre/modify-chambre"), status().isOk());
        assertEndpoint(delete("/Chambre/remove-chambre/1"), status().isNoContent());
        assertEndpoint(put("/Chambre/assign-chambre-to-bloc/1/2"), status().isOk());
    }

    @Test
    void etudiantCrudEndpoints() throws Exception {
        assertEndpoint(get("/Etudiant/retrieve-all-etudiants"), status().isOk());
        assertEndpoint(get("/Etudiant/retrieve-etudiant/1"), status().isOk());
        assertEndpoint(jsonPost("/Etudiant/add-etudiant"), status().isCreated());
        assertEndpoint(jsonPut("/Etudiant/modify-etudiant"), status().isOk());
        assertEndpoint(delete("/Etudiant/remove-etudiant/1"), status().isNoContent());
    }

    @Test
    void foyerCrudEndpoints() throws Exception {
        assertEndpoint(get("/Foyer/retrieve-all-foyers"), status().isOk());
        assertEndpoint(get("/Foyer/retrieve-foyer/1"), status().isOk());
        assertEndpoint(jsonPost("/Foyer/add-foyer"), status().isCreated());
        assertEndpoint(jsonPut("/Foyer/modify-foyer"), status().isOk());
        assertEndpoint(delete("/Foyer/remove-foyer/1"), status().isNoContent());
    }

    @Test
    void reservationCrudEndpoints() throws Exception {
        assertEndpoint(get("/Reservation/retrieve-all-reservations"), status().isOk());
        assertEndpoint(get("/Reservation/retrieve-reservation/test-id"), status().isOk());
        assertEndpoint(jsonPost("/Reservation/add-reservation"), status().isCreated());
        assertEndpoint(jsonPut("/Reservation/modify-reservation"), status().isOk());
        assertEndpoint(delete("/Reservation/remove-reservation/test-id"), status().isNoContent());
    }

    @Test
    void universiteCrudEndpoints() throws Exception {
        assertEndpoint(get("/Universite/retrieve-all-universites"), status().isOk());
        assertEndpoint(get("/Universite/retrieve-universite/1"), status().isOk());
        assertEndpoint(jsonPost("/Universite/add-universite"), status().isCreated());
        assertEndpoint(jsonPut("/Universite/modify-universite"), status().isOk());
        assertEndpoint(delete("/Universite/remove-universite/1"), status().isNoContent());
    }

    private MockHttpServletRequestBuilder jsonPost(String path) {
        return post(path).contentType(MediaType.APPLICATION_JSON).content("{}");
    }

    private MockHttpServletRequestBuilder jsonPut(String path) {
        return put(path).contentType(MediaType.APPLICATION_JSON).content("{}");
    }

    private void assertEndpoint(MockHttpServletRequestBuilder request, ResultMatcher expectedStatus)
            throws Exception {
        mockMvc.perform(request).andExpect(expectedStatus);
    }
}
