package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(value = "key", required = false) String keyWord) {
        Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, keyWord);
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant byLogin = participantService.findByLogin(participant.getLogin());
        if (byLogin != null) {
            return new ResponseEntity<>(
                    "Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
        } else {
            participantService.addParticipant(participant);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("login") String login) {
        Participant participant = participantService.deleteParticipant(login);
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@RequestBody Participant participant) {
        Participant updatedParticipant = participantService.updateParticipant(participant.getLogin(), participant.getPassword());
        return new ResponseEntity<>(updatedParticipant, HttpStatus.OK);
    }

}
