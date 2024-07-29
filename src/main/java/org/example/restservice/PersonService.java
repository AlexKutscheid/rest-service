package org.example.restservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class PersonService {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("personPU");

    public PersonObject createPerson(PersonObject person) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return person;
    }

    public PersonObject update(PersonObject person) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            PersonObject updatedPerson = entityManager.merge(person);
            transaction.commit();
            return updatedPerson;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void delete(PersonObject person) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            PersonObject personToRemove = entityManager.find(PersonObject.class, person.getId());
            if (personToRemove != null) {
                entityManager.remove(personToRemove);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public PersonObject findPersonById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.find(PersonObject.class, id);
        } finally {
            entityManager.close();
        }
    }

    public List<PersonObject> findAllPersons() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            TypedQuery<PersonObject> query = entityManager.createQuery("SELECT p FROM PersonObject p", PersonObject.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
}