package services;

import database.WbsRepository;
import domain.business.Activity;
import domain.business.WBS;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class WbsServiceImpl implements WbsService {

    WbsRepository repository;
    BusinessMapperService businessMapperService;
    public WbsServiceImpl(WbsRepository repository, BusinessMapperService businessMapperService) {
        this.repository = repository;
        this.businessMapperService = businessMapperService;
        businessMapperService.subscribe(this);
    }

    @Override
    public WBS getById(int id) {
        WBS item = businessMapperService.map(repository.getById(id));
      //  item.calculateSum();
        return item;
    }

    @Override
    public List<WBS> findAll() {
        List<WBS> wbses = repository.findAll().stream().map(item -> businessMapperService.map(item)).collect(Collectors.toList());
      //  wbses.forEach(item -> item.calculateSum());
        return wbses;
    }

    @Override
    public void insert(WBS entity) {
        repository.insert(businessMapperService.map(entity));
    }
    @Override
    public void insertAll(List<WBS> entities) {
        if (entities == null)
            return;

        Set<Integer> ids = new HashSet<>();
        for(WBS wbsItem : entities) {
            WBS next = wbsItem;
            while(next != null) {
                if (!ids.contains(next.getId())) {
                    insert(next);
                    ids.add(next.getId());
                }
                next = next.getParent();
            }
        }
    }
}
