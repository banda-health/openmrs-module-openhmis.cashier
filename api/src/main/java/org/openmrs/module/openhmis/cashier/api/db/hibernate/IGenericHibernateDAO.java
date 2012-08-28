package org.openmrs.module.openhmis.cashier.api.db.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.openmrs.api.APIException;

public interface IGenericHibernateDAO<E> {
    Criteria createCriteria();

    E save(E entity) throws APIException;
    void delete(E entity) throws APIException;;

    E selectSingle(Serializable id) throws APIException;
    E selectSingle(Criteria criteria) throws APIException;

    List<E> select() throws APIException;
    List<E> select(Criteria criteria) throws APIException;
}
