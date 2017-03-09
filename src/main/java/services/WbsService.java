package services;

import domain.business.Activity;
import domain.business.WBS;
import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public interface WbsService {

    WBS getById(int id);
    List<WBS> findAll();
    void insert(WBS entity);
    void insertAll(List<WBS> entities);
}
