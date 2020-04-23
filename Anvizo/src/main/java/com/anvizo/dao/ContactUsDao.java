package com.anvizo.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.anvizo.model.ContactUs;

public class ContactUsDao extends HibernateDaoSupport {

	public ContactUs find(Long key) {
		return (ContactUs) getHibernateTemplate().get(ContactUs.class, key);
	}

	@SuppressWarnings("unchecked")
	public List<ContactUs> findAll() {
		final Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ContactUs.class);
		criteria.addOrder(Order.asc("name"));
		return (List<ContactUs>) criteria.list();
	}
	
	public void create( ContactUs contactus )
    {
        getHibernateTemplate().save( contactus );
        //flush immediately to throw exception at this point instead of at commit, to avoid further actions completion
        getHibernateTemplate().flush();
    }
	

    public ContactUs update( ContactUs glossary )
    {
        getHibernateTemplate().update( glossary );
        //return ( ContactUs ) getHibernateTemplate().get( ContactUs.class, glossary.getKey() );
        return null;
    }

    public void delete( ContactUs glossary )
    {
        this.getHibernateTemplate().delete( glossary );
        //flush immediately to throw exception at this point instead of at commit, avoid creation of glossary on solr and eureka if error
        getHibernateTemplate().flush();
    }
    
}
