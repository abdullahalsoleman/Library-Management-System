package com.library.service;

import com.library.IRepository.PatronRepository;
import com.library.UnitOfWork.UnitOfWork;
import com.library.dto.RequestStatus;
import com.library.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatronServiceImplTest {

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPatron_Success() {
        Patron patron = new Patron();
        patron.setName("Test Patron");

        // Mock the repository save method to do nothing
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);
        doNothing().when(patronRepository).save(any(Patron.class));

        RequestStatus response = patronService.addPatron(patron);
        assertTrue(response.isSuccess());
        assertEquals("Patron added successfully.", response.getMessage());
    }

    @Test
    void testUpdatePatron_PatronExists() {
        Long patronId = 1L;
        Patron existingPatron = new Patron();
        existingPatron.setId(patronId);
        existingPatron.setName("Existing Patron");

        Patron updatedPatron = new Patron();
        updatedPatron.setName("Updated Patron");

        // Mock the repository behavior
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));

        RequestStatus response = patronService.updatePatron(patronId, updatedPatron);
        assertTrue(response.isSuccess());
        assertEquals("Patron updated successfully.", response.getMessage());
    }

    @Test
    void testUpdatePatron_PatronNotFound() {
        Long patronId = 1L;
        Patron updatedPatron = new Patron();
        updatedPatron.setName("Updated Patron");

        // Mock repository behavior
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        RequestStatus response = patronService.updatePatron(patronId, updatedPatron);
        assertFalse(response.isSuccess());
        assertEquals("Patron does not exist.", response.getMessage());
    }

    @Test
    void testDeletePatron_Success() {
        Long patronId = 1L;

        // Mock repository behavior
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);
        when(patronRepository.existsById(patronId)).thenReturn(true);

        RequestStatus response = patronService.deletePatron(patronId);
        assertTrue(response.isSuccess());
        assertEquals("Patron deleted successfully.", response.getMessage());
    }

    @Test
    void testDeletePatron_PatronNotFound() {
        Long patronId = 1L;

        // Mock repository behavior
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);
        when(patronRepository.existsById(patronId)).thenReturn(false);

        RequestStatus response = patronService.deletePatron(patronId);
        assertFalse(response.isSuccess());
        assertEquals("Patron does not exist.", response.getMessage());
    }
}
