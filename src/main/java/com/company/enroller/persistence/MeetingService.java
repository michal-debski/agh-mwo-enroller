package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

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

	public Meeting deleteByTitle(String title) {
			String hql = "FROM Meeting WHERE title = :title";
			Session session = connector.getSession();
			Query<Meeting> query = session.createQuery(hql, Meeting.class);
			query.setParameter("title", title);
			Meeting meeting = query.uniqueResult();
			Transaction transaction = session.beginTransaction();
			session.delete(meeting);
			session.flush();
			transaction.commit();
			return meeting;

	}
}
