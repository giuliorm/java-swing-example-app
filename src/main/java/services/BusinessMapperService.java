package services;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public interface BusinessMapperService {

    void subscribe(ActivityService service);
    void subscribe(WbsService service);
    domain.business.Activity map(domain.database.Activity activity);
    domain.database.Activity map(domain.business.Activity activity);
    domain.business.WBS map(domain.database.WBS wbs);
    domain.database.WBS map(domain.business.WBS wbs);
}
