package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Meeting findById(long id) {
        String hql = "FROM Meeting WHERE id = :id";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("id", id);
        return (Meeting) query.uniqueResult();
    }

    public void createMeeting(Meeting meeting) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(meeting);
        transaction.commit();

    }


    public Meeting deleteById(long id) {
        String hql = "FROM Meeting WHERE id = :id";
        Session session = connector.getSession();
        Query<Meeting> query = session.createQuery(hql, Meeting.class);
        query.setParameter("id", id);
        Meeting meeting = query.uniqueResult();
        Transaction transaction = session.beginTransaction();
        session.delete(meeting);
        session.flush();
        transaction.commit();
        return meeting;
    }

    public Meeting updateMeeting(long id, String title, String description, String date) {
        String hql = "FROM Meeting WHERE id = :id";
        Session session = connector.getSession();
        Query<Meeting> query = session.createQuery(hql, Meeting.class);
        query.setParameter("id", id);
        Meeting foundMeeting = query.uniqueResult();
        foundMeeting.setTitle(title);
        foundMeeting.setDescription(description);
        foundMeeting.setDate(date);
        Transaction transaction = session.beginTransaction();
        session.save(foundMeeting);
        transaction.commit();
        return foundMeeting;
    }

    public Meeting addParticipantToMeeting(long id, Collection<Participant> participants) {
        String hql = "FROM Meeting WHERE id = :id";
        Session session = connector.getSession();
        Query<Meeting> query = session.createQuery(hql, Meeting.class);
        query.setParameter("id", id);
        Meeting foundMeeting = query.uniqueResult();
        for (Participant participant : participants) {
            foundMeeting.addParticipant(participant);
        }
        Transaction transaction = session.beginTransaction();
        session.save(foundMeeting);
        transaction.commit();
        return foundMeeting;
    }

    public List<Participant> getParticipantsForSpecificMeetingId(long id) {
        String hql = "FROM Meeting WHERE id = :id";
        Session session = connector.getSession();
        Query<Meeting> query = session.createQuery(hql, Meeting.class);
        query.setParameter("id", id);
        Meeting meeting = query.uniqueResult();
        return meeting.getParticipants().stream().toList();

    }
}
