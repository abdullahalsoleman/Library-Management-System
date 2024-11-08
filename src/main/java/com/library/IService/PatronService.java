package com.library.IService;

import com.library.dto.RequestStatus;
import com.library.model.Patron;

import java.util.List;
import java.util.Optional;

public interface PatronService {
    List<Patron> getAllPatrons();
    Optional<Patron> getPatronById(Long id);
    RequestStatus addPatron(Patron patron);
    RequestStatus updatePatron(Long id, Patron patron);
    RequestStatus deletePatron(Long id);
}
