package uk.co.sancode.skeleton_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A record with this id already exists")
public class DuplicateRecordException extends Exception {
}
