package com.library.service;

import com.library.IRepository.BookRepository;
import com.library.IRepository.BorrowingRecordRepository;
import com.library.IRepository.PatronRepository;
import com.library.UnitOfWork.UnitOfWork;
import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingServiceImplTest {

    @Mock
    private UnitOfWork unitOfWork;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PatronRepository patronRepository;
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    private Book sampleBook;
    private Patron samplePatron;
    private BorrowingRecord sampleBorrowingRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup UnitOfWork repositories
        when(unitOfWork.getBookRepository()).thenReturn(bookRepository);
        when(unitOfWork.getPatronRepository()).thenReturn(patronRepository);
        when(unitOfWork.getBorrowingRecordRepository()).thenReturn(borrowingRecordRepository);

        // Initialize sample data
        sampleBook = new Book();
        sampleBook.setId(1L);

        samplePatron = new Patron();
        samplePatron.setId(1L);

        sampleBorrowingRecord = new BorrowingRecord();
        sampleBorrowingRecord.setBookId(1L);
        sampleBorrowingRecord.setPatronId(1L);
        sampleBorrowingRecord.setBorrowDate(new Date());
    }

    @Test
    void testBorrowBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(samplePatron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(sampleBorrowingRecord);

        BorrowingRecord result = borrowingService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals(1L, result.getPatronId());
        assertNotNull(result.getBorrowDate());

        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(patronRepository.findById(1L)).thenReturn(Optional.of(samplePatron));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals("Book does not exist.", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_PatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals("Patron does not exist.", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(samplePatron));
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.of(sampleBorrowingRecord));

        BorrowingRecord result = borrowingService.returnBook(1L, 1L);

        assertNotNull(result);
        assertNotNull(result.getReturnDate());
        verify(borrowingRecordRepository, times(1)).save(result);
    }

    @Test
    void testReturnBook_NoActiveBorrowingRecord() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(samplePatron));
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            borrowingService.returnBook(1L, 1L);
        });

        assertEquals("No active borrowing record found for this book and patron.", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_BookOrPatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(patronRepository.findById(1L)).thenReturn(Optional.of(samplePatron));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.returnBook(1L, 1L);
        });

        assertEquals("Book or patron does not exist.", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }
}
