package database;

import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public interface Repository<T> {

    T getById(int id);
    List<T> findAll();
    void insert(T entity);
}
