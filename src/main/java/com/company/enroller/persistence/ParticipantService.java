package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(String orderBy, String sortOrder, String keyword) {
        String hql= "FROM Participant";

        if(keyword == null || keyword.isEmpty()) {
            if (sortOrder == null) {
                hql += " order by " + orderBy;
                System.out.println(hql);
            } else if (sortOrder != null && orderBy != null) {
                hql += " order by " + orderBy + " " + sortOrder;
                System.out.println(hql);
            }
        } else {
            hql += " where login like '%" + keyword + "%'" ;
            System.out.println(hql);
        }

        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }


    public Participant findByLogin(String login) {
        String hql = "FROM Participant WHERE login = :login";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("login", login);
        return (Participant) query.uniqueResult();
    }

    public void addParticipant(Participant participant) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);
        session.save(participant);
        transaction.commit();
    }

    public Participant deleteParticipant(String login) {
        String hql = "FROM Participant WHERE login = :login";
        Session session = connector.getSession();
        Query<Participant> query = session.createQuery(hql, Participant.class);
        query.setParameter("login", login);
        Participant participant = query.uniqueResult();
        Transaction transaction = session.beginTransaction();
        session.delete(participant);
        session.flush();
        transaction.commit();
        return participant;
    }

    public Participant updateParticipant(String login, String password) {
        String hql = "FROM Participant WHERE login = :login";
        Session session = connector.getSession();
        Query<Participant> query = session.createQuery(hql, Participant.class);
        query.setParameter("login", login);
        Participant foundParticipant = query.uniqueResult();
        foundParticipant.setPassword(password);
        Transaction transaction = session.beginTransaction();
        session.save(foundParticipant);
        transaction.commit();
        return foundParticipant;
    }
}
