package tn.esprit.gestionfoyer.service;

import tn.esprit.gestionfoyer.entity.Chambre;

import java.util.List;

public interface IChambreService {

    public List<Chambre> retrieveAllChambres();
    public Chambre retrieveChambre(Long id);
    public Chambre addChambre(Chambre c);
    public void removeChambre(Long id);
    public Chambre modifyChambre(Chambre c);

    public Chambre assignChambreToBloc(Long chambreId, Long blocId);

}
