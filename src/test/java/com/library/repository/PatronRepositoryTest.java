package com.library.repository;

import com.library.IRepository.PatronRepository;
import com.library.model.Patron;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatronRepositoryTest {

    @Mock
    private PatronRepository patronRepository;

    @Test
    public void testSavePatron() {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("john@example.com");
        patron.setPhoneNumber("1234567890");

        when(patronRepository.save(patron)).thenReturn(patron);

        Patron savedPatron = patronRepository.save(patron);

        assertEquals("John Doe", savedPatron.getName());
        verify(patronRepository, times(1)).save(patron);
    }
}
