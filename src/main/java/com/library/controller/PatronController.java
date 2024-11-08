package com.library.controller;

import com.library.IService.Service;
import com.library.dto.AddAndUpdatePatronDTO;
import com.library.dto.PatronDTO;
import com.library.dto.RequestStatus;
import com.library.model.Patron;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patrons")
@Validated
public class PatronController {

    // Injected service dependency for patron-related operations
    @Autowired
    private Service service;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Endpoint to get all patrons.
     */
    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        // Fetch all patrons from the service
        List<Patron> patrons = service.getPatronService().getAllPatrons();
        // Convert List<Patron> to List<PatronDTO> using ModelMapper
        List<PatronDTO> patronDTOs = patrons.stream()
                .map(patron -> modelMapper.map(patron, PatronDTO.class))
                .collect(Collectors.toList());
        // Return the list of PatronDTOs in the response
        return ResponseEntity.ok(patronDTOs);
    }

    /**
     * Endpoint to get a patron by their ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatronById(@PathVariable Long id) {
        try {
            // Attempt to fetch the patron by ID
            Optional<Patron> patron = service.getPatronService().getPatronById(id);
            // Convert Patron entity to PatronDTO
            PatronDTO patronDTO = modelMapper.map(patron.get(), PatronDTO.class);
            return ResponseEntity.ok(patronDTO);  // Return the PatronDTO in the response
        } catch (Exception e) {
            // If any error occurs (example: patron not found), return an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Patron not found or error occurred."));
        }
    }

    /**
     * Endpoint to add a new patron.
     */
    @PostMapping
    public ResponseEntity<RequestStatus> addPatron(@Valid @RequestBody AddAndUpdatePatronDTO newPatron) {
        try {
            // Convert AddAndUpdatePatronDTO to Patron entity before passing to the service
            Patron patron = modelMapper.map(newPatron, Patron.class);
            // Call the service to add the new patron
            service.getPatronService().addPatron(patron);
            // Return a success response
            return ResponseEntity.ok(new RequestStatus(true, "Patron added successfully."));
        } catch (Exception e) {
            // If there is an error adding the patron, return an error response
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error adding patron."));
        }
    }

    /**
     * Endpoint to update an existing patron's information.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequestStatus> updatePatron(@PathVariable Long id, @Valid @RequestBody AddAndUpdatePatronDTO updatedPatron) {
        try {
            // Convert AddAndUpdatePatronDTO to Patron entity
            Patron patron = modelMapper.map(updatedPatron, Patron.class);
            // Call the service to update the patron information
            service.getPatronService().updatePatron(id, patron);
            // Return a success response
            return ResponseEntity.ok(new RequestStatus(true, "Patron updated successfully."));
        } catch (Exception e) {
            // If there is an error or the patron is not found, return an error response
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error updating patron or patron not found."));
        }
    }

    /**
     * Endpoint to delete a patron by their ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RequestStatus> deletePatron(@PathVariable Long id) {
        try {
            // Call the service to delete the patron by ID
            service.getPatronService().deletePatron(id);
            // Return a success response
            return ResponseEntity.ok(new RequestStatus(true, "Patron deleted successfully."));
        } catch (Exception e) {
            // If there is an error or the patron is not found, return an error response
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error deleting patron or patron not found."));
        }
    }
}
