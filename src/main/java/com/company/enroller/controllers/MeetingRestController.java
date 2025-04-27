package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> allMeetings = meetingService.getAll();

        return new ResponseEntity<>(allMeetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingById(@PathVariable long id) {
        Meeting meeting = meetingService.findById(id);
        if(meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(meeting, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {

        meetingService.createMeeting(meeting);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable long id) {
        meetingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting) {
        Meeting updatedMeeting = meetingService.updateMeeting(meeting.getId(), meeting.getTitle(), meeting.getDescription(), meeting.getDate());
        return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable long id) {
        List<Participant> participants = meetingService.getParticipantsForSpecificMeetingId(id);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable long id, @RequestBody Collection<Participant> participants) {
        for (Participant participant : participants) {
            if(participantService.findByLogin(participant.getLogin()) == null && ) {
                participantService.addParticipant(participant);
            }else{
                continue;
            }
        }
        Meeting meeting = meetingService.addParticipantToMeeting(id, participants);
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }
}
