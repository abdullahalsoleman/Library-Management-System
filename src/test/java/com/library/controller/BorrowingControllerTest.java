package com.library.controller;

import com.library.IService.Service;
import com.library.IService.BorrowingService;
import com.library.dto.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingControllerTest {

    @Mock
    private Service service;  // Mocking the main Service class

    @Mock
    private BorrowingService borrowingService;  // Mocking the specific BorrowingService

    @InjectMocks
    private BorrowingController borrowingController;  // Controller under test

    private final Long bookId = 1L;  // Sample book ID for testing
    private final Long patronId = 1L;  // Sample patron ID for testing

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Configuring the mock service to return borrowingService
        when(service.getBorrowingService()).thenReturn(borrowingService);
    }

    @Test
    void testBorrowBook_Success() {
        // Setting up a successful scenario for borrowBook
        doNothing().when(borrowingService).borrowBook(bookId, patronId);

        // Call the borrowBook endpoint on the controller
        ResponseEntity<RequestStatus> response = borrowingController.borrowBook(bookId, patronId);

        // Verify HTTP status and response body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Book borrowed successfully."), response.getBody());

        // Verify that borrowBook was called exactly once on borrowingService
        verify(borrowingService, times(1)).borrowBook(bookId, patronId);
    }

    @Test
    void testBorrowBook_Error() {
        // Simulating an error during borrowBook by throwing a RuntimeException
        doThrow(new RuntimeException()).when(borrowingService).borrowBook(bookId, patronId);

        // Call the borrowBook endpoint and expect an error response
        ResponseEntity<RequestStatus> response = borrowingController.borrowBook(bookId, patronId);

        // Verify HTTP status and response body for error scenario
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error borrowing book or book/patron not found."), response.getBody());

        // Verify borrowBook was attempted once
        verify(borrowingService, times(1)).borrowBook(bookId, patronId);
    }

    @Test
    void testReturnBook_Success() {
        // Setting up a successful scenario for returnBook
        doNothing().when(borrowingService).returnBook(bookId, patronId);

        // Call the returnBook endpoint on the controller
        ResponseEntity<RequestStatus> response = borrowingController.returnBook(bookId, patronId);

        // Check for HTTP OK status and correct success message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Book returned successfully."), response.getBody());

        // returnBook was called exactly once on borrowingService
        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }

    @Test
    void testReturnBook_Error() {
        // Simulate an error by throwing a RuntimeException in returnBook
        doThrow(new RuntimeException()).when(borrowingService).returnBook(bookId, patronId);

        // Call the returnBook endpoint, expecting an error response
        ResponseEntity<RequestStatus> response = borrowingController.returnBook(bookId, patronId);

        // Verify BAD_REQUEST status and the error message in the response body
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error returning book or book/patron not found."), response.getBody());

        // Verify returnBook was attempted once
        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }
}
