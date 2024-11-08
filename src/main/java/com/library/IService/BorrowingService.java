package com.library.IService;

import com.library.model.BorrowingRecord;

public interface BorrowingService {
    BorrowingRecord borrowBook(Long bookId, Long patronId);
    BorrowingRecord returnBook(Long bookId, Long patronId);
}
