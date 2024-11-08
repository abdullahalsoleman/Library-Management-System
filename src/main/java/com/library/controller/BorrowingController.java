package com.library.controller;

import com.library.IService.Service;
import com.library.dto.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingController {

    // Injected service dependency for borrowing and returning books
    @Autowired
    private Service service;

    /**
     * Endpoint to borrow a book by a patron.
     */
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<RequestStatus> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            // Call the borrowing service to handle the borrowing logic
            service.getBorrowingService().borrowBook(bookId, patronId);
            // Return a success message if the book was successfully borrowed
            return ResponseEntity.ok(new RequestStatus(true, "Book borrowed successfully."));
        } catch (Exception e) {
            // If any error occurs (example: book or patron not found), return an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error borrowing book or book/patron not found."));
        }
    }

    /**
     * Endpoint to return a borrowed book by a patron.
     */
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<RequestStatus> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            // Call the borrowing service to handle the returning logic
            service.getBorrowingService().returnBook(bookId, patronId);
            // Return a success message if the book was successfully returned
            return ResponseEntity.ok(new RequestStatus(true, "Book returned successfully."));
        } catch (Exception e) {
            // If any error occurs (e.g., book or patron not found), return an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error returning book or book/patron not found."));
        }
    }
}
