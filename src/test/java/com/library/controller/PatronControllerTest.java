package com.library.controller;

import com.library.IService.Service;
import com.library.IService.PatronService;
import com.library.dto.AddAndUpdatePatronDTO;
import com.library.dto.PatronDTO;
import com.library.dto.RequestStatus;
import com.library.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PatronControllerTest {

    // Mocked dependencies
    @Mock
    private Service service;

    @Mock
    private PatronService patronService;

    @Mock
    private ModelMapper modelMapper;

    // Inject PatronController with mocked dependencies
    @InjectMocks
    private PatronController patronController;

    // Sample data used across test cases
    private Patron patron;
    private PatronDTO patronDTO;
    private AddAndUpdatePatronDTO addAndUpdatePatronDTO;


    @BeforeEach
    void setUp() {
        // Open mocks and initialize the injected dependencies
        MockitoAnnotations.openMocks(this);
        // Return the mocked patronService when the service.getPatronService() method is called
        when(service.getPatronService()).thenReturn(patronService);

        // Sample patron data to be used in tests
        patron = new Patron(1L, "John Doe", "1234567890", "john@example.com");
        patronDTO = new PatronDTO(1L, "John Doe", "1234567890", "john@example.com");
        addAndUpdatePatronDTO = new AddAndUpdatePatronDTO("John Doe", "1234567890", "john@example.com");

        // Configure ModelMapper to map objects correctly
        when(modelMapper.map(any(Patron.class), eq(PatronDTO.class))).thenReturn(patronDTO);
        when(modelMapper.map(any(AddAndUpdatePatronDTO.class), eq(Patron.class))).thenReturn(patron);
    }

    /**
     * Test for the getAllPatrons method, verifying that all patrons are retrieved correctly.
     */
    @Test
    void testGetAllPatrons() {
        // Return a list of patrons from the patronService mock
        List<Patron> patrons = Arrays.asList(patron);
        when(patronService.getAllPatrons()).thenReturn(patrons);

        // Call the method being tested
        ResponseEntity<List<PatronDTO>> response = patronController.getAllPatrons();

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());  // Ensure there is one patron in the list
        assertEquals(patronDTO, response.getBody().get(0));  // Ensure the patronDTO is returned

        // Verify that patronService.getAllPatrons() was called once
        verify(patronService, times(1)).getAllPatrons();
    }

    /**
     * Test for the getPatronById method, ensuring the correct patron is returned for a valid ID.
     */
    @Test
    void testGetPatronById_Success() {
        // Arrange: Return an Optional containing the patron from the patronService mock
        when(patronService.getPatronById(1L)).thenReturn(Optional.of(patron));

        // Call the method being tested
        ResponseEntity<?> response = patronController.getPatronById(1L);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patronDTO, response.getBody());  // Ensure the patronDTO is returned

        // Verify that patronService.getPatronById() was called once
        verify(patronService, times(1)).getPatronById(1L);
    }

    /**
     * Test for the `getPatronById` method when the patron is not found.
     */
    @Test
    void testGetPatronById_NotFound() {
        // Return an empty Optional when searching for the patron
        when(patronService.getPatronById(1L)).thenReturn(Optional.empty());

        // Call the method being tested
        ResponseEntity<?> response = patronController.getPatronById(1L);

        // Verify the response status and body for the error case
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Patron not found or error occurred."), response.getBody());

        // Verify that patronService.getPatronById() was called once
        verify(patronService, times(1)).getPatronById(1L);
    }

    /**
     * Test for the `addPatron` method when adding a new patron is successful.
     */
    @Test
    void testAddPatron_Success() {
        // Mock the patronService to do nothing when addPatron is called
        doNothing().when(patronService).addPatron(patron);

        // Call the method being tested
        ResponseEntity<RequestStatus> response = patronController.addPatron(addAndUpdatePatronDTO);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Patron added successfully."), response.getBody());

        // Verify that patronService.addPatron() was called once
        verify(patronService, times(1)).addPatron(patron);
    }

    /**
     * Test for the `addPatron` method when an error occurs during patron addition.
     */
    @Test
    void testAddPatron_Error() {
        // Mock the patronService to throw an exception when addPatron is called
        doThrow(new RuntimeException()).when(patronService).addPatron(patron);

        // Call the method being tested
        ResponseEntity<RequestStatus> response = patronController.addPatron(addAndUpdatePatronDTO);

        // Verify the response status and body for the error case
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error adding patron."), response.getBody());

        // Verify that patronService.addPatron() was called once
        verify(patronService, times(1)).addPatron(patron);
    }

    /**
     * Test for the `updatePatron` method when the update is successful.
     */
    @Test
    void testUpdatePatron_Success() {
        // Mock the patronService to do nothing when updatePatron is called
        doNothing().when(patronService).updatePatron(1L, patron);

        // Call the method being tested
        ResponseEntity<RequestStatus> response = patronController.updatePatron(1L, addAndUpdatePatronDTO);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Patron updated successfully."), response.getBody());

        // Verify that patronService.updatePatron() was called once
        verify(patronService, times(1)).updatePatron(1L, patron);
    }

    /**
     * Test for the `updatePatron` method when an error occurs during the update.
     */
    @Test
    void testUpdatePatron_Error() {
        // Mock the patronService to throw an exception when updatePatron is called
        doThrow(new RuntimeException()).when(patronService).updatePatron(1L, patron);

        // Call the method being tested
        ResponseEntity<RequestStatus> response = patronController.updatePatron(1L, addAndUpdatePatronDTO);

        // Verify the response status and body for the error case
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error updating patron or patron not found."), response.getBody());

        // Verify that patronService.updatePatron() was called once
        verify(patronService, times(1)).updatePatron(1L, patron);
    }

    /**
     * Test for the `deletePatron` method when deletion is successful.
     */
    @Test
    void testDeletePatron_Success() {
        // Mock the patronService to do nothing when deletePatron is called
        doNothing().when(patronService).deletePatron(1L);

        // Call the method being tested
        ResponseEntity<RequestStatus> response = patronController.deletePatron(1L);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Patron deleted successfully."), response.getBody());

        // Verify that patronService.deletePatron() was called once
        verify(patronService, times(1)).deletePatron(1L);
    }

    /**
     * Test for the `deletePatron` method when an error occurs during deletion.
     */
    @Test
    void testDeletePatron_Error() {
        // Mock the patronService to throw an exception when deletePatron is called
        doThrow(new RuntimeException()).when(patronService).deletePatron(1L);

        // Call the method being tested
        ResponseEntity<RequestStatus> response = patronController.deletePatron(1L);

        // Verify the response status and body for the error case
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error deleting patron."), response.getBody());

        // Verify that patronService.deletePatron() was called once
        verify(patronService, times(1)).deletePatron(1L);
    }
}
