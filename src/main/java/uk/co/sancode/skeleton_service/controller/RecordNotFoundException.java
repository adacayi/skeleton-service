package uk.co.sancode.skeleton_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "A record with this id does not exist")
public class RecordNotFoundException extends Exception {
}
