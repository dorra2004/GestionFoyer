package tn.esprit.gestionfoyer.service;

import tn.esprit.gestionfoyer.entity.Bloc;

import java.util.List;

public interface IBlocService {

    List<Bloc> retrieveAllBlocs();

    Bloc retrieveBloc(Long id);

    Bloc addBloc(Bloc bloc);

    void removeBloc(Long id);

    Bloc modifyBloc(Bloc bloc);
}