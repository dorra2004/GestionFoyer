package tn.esprit.gestionfoyer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionfoyer.entity.Bloc;
import tn.esprit.gestionfoyer.entity.Chambre;
import tn.esprit.gestionfoyer.repository.BlocRepository;
import tn.esprit.gestionfoyer.repository.ChambreRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ChambreServiceImpl implements tn.esprit.gestionfoyer.service.IChambreService {

    ChambreRepository chambreRepository;
    BlocRepository blocRepository;

    @Override
    public List<Chambre> retrieveAllChambres() {
        return chambreRepository.findAll();
    }

    @Override
    public Chambre retrieveChambre(Long id) {
        return chambreRepository.findById(id).get();
    }

    @Override
    public Chambre addChambre(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public void removeChambre(Long id) {
        chambreRepository.deleteById(id);
    }

    @Override
    public Chambre modifyChambre(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public Chambre assignChambreToBloc(Long chambreId, Long blocId) {
        Bloc bloc = blocRepository.findById(blocId).get();
        Chambre chambre = chambreRepository.findById(chambreId).get();
        chambre.setBloc(bloc);
        return chambreRepository.save(chambre);
    }
}
