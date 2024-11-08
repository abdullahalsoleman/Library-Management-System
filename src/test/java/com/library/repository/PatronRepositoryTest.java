package com.library.repository;

import com.library.model.Patron;
import com.library.IRepository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PatronRepositoryTest {

    @Autowired
    private PatronRepository patronRepository;

    private Patron mockPatron;

    @BeforeEach
    public void setup() {
        mockPatron = new Patron();
        mockPatron.setId(1L);
        mockPatron.setName("John Doe");
        mockPatron.setEmail("johndoe@example.com");
    }

    @Test
    public void testFindById() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(mockPatron));

        Optional<Patron> result = patronRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals("johndoe@example.com", result.get().getEmail());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        when(patronRepository.save(mockPatron)).thenReturn(mockPatron);

        Patron savedPatron = patronRepository.save(mockPatron);

        assertNotNull(savedPatron);
        assertEquals("John Doe", savedPatron.getName());
        assertEquals("johndoe@example.com", savedPatron.getEmail());
        verify(patronRepository, times(1)).save(mockPatron);
    }

    @Test
    public void testDelete() {
        doNothing().when(patronRepository).deleteById(1L);

        patronRepository.deleteById(1L);

        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Patron> result = patronRepository.findById(1L);

        assertFalse(result.isPresent());
        verify(patronRepository, times(1)).findById(1L);
    }
}
