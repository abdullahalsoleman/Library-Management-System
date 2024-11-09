package com.library.service;

import com.library.IRepository.PatronRepository;
import com.library.IService.PatronService;
import com.library.UnitOfWork.UnitOfWork;
import com.library.dto.RequestStatus;
import com.library.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatronServiceImplTest {

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    private Patron patron;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
    }

    @Test
    public void testGetAllPatrons() {
        when(patronRepository.findAll()).thenReturn(Arrays.asList(patron));

        List<Patron> patrons = patronService.getAllPatrons();

        assertEquals(1, patrons.size());
        assertEquals("John Doe", patrons.get(0).getName());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    public void testGetPatronById_Exists() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Optional<Patron> foundPatron = patronService.getPatronById(1L);

        assertTrue(foundPatron.isPresent());
        assertEquals("John Doe", foundPatron.get().getName());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPatronById_NotExists() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Patron> foundPatron = patronService.getPatronById(1L);

        assertFalse(foundPatron.isPresent());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddPatron_Success() {
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        RequestStatus status = patronService.addPatron(patron);

        assertTrue(status.isStatus());
        assertEquals("Patron added successfully.", status.getMessage());
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testAddPatron_Failure() {
        doThrow(new RuntimeException("Database error")).when(patronRepository).save(any(Patron.class));

        RequestStatus status = patronService.addPatron(patron);

        assertFalse(status.isStatus());
        assertEquals("Unable to add the patron. Please try again.", status.getMessage());
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testUpdatePatron_Exists() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        RequestStatus status = patronService.updatePatron(1L, patron);

        assertTrue(status.isStatus());
        assertEquals("Patron updated successfully.", status.getMessage());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testUpdatePatron_NotExists() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        RequestStatus status = patronService.updatePatron(1L, patron);

        assertFalse(status.isStatus());
        assertEquals("Patron does not exist.", status.getMessage());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(0)).save(any(Patron.class));
    }

    @Test
    public void testUpdatePatron_Failure() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        doThrow(new RuntimeException("Database error")).when(patronRepository).save(any(Patron.class));

        RequestStatus status = patronService.updatePatron(1L, patron);

        assertFalse(status.isStatus());
        assertEquals("Unable to update the patron. Please try again.", status.getMessage());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testDeletePatron_Exists() {
        when(patronRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patronRepository).deleteById(1L);

        RequestStatus status = patronService.deletePatron(1L);

        assertTrue(status.isStatus());
        assertEquals("Patron deleted successfully.", status.getMessage());
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePatron_NotExists() {
        when(patronRepository.existsById(1L)).thenReturn(false);

        RequestStatus status = patronService.deletePatron(1L);

        assertFalse(status.isStatus());
        assertEquals("Patron does not exist.", status.getMessage());
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testDeletePatron_Failure() {
        when(patronRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(patronRepository).deleteById(1L);

        RequestStatus status = patronService.deletePatron(1L);

        assertFalse(status.isStatus());
        assertEquals("Unable to delete the patron. Please try again.", status.getMessage());
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }
}
