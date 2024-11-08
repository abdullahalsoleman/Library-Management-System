package com.library.repository;

import com.library.model.BorrowingRecord;
import com.library.IRepository.BorrowingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BorrowingRecordRepositoryTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private BorrowingRecord mockRecord;

    @BeforeEach
    public void setup() {
        mockRecord = new BorrowingRecord();
        mockRecord.setId(1L);
        mockRecord.setBookId(101L);
        mockRecord.setPatronId(201L);
        mockRecord.setBorrowDate(new Date(System.currentTimeMillis()));
    }

    @Test
    public void testFindById() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(mockRecord));

        Optional<BorrowingRecord> result = borrowingRecordRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("2024-11-01", result.get().getBorrowDate());
        verify(borrowingRecordRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        when(borrowingRecordRepository.save(mockRecord)).thenReturn(mockRecord);

        BorrowingRecord savedRecord = borrowingRecordRepository.save(mockRecord);

        assertNotNull(savedRecord);
        assertEquals("2024-11-01", savedRecord.getBorrowDate());
        verify(borrowingRecordRepository, times(1)).save(mockRecord);
    }

    @Test
    public void testDelete() {
        doNothing().when(borrowingRecordRepository).deleteById(1L);

        borrowingRecordRepository.deleteById(1L);

        verify(borrowingRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BorrowingRecord> result = borrowingRecordRepository.findById(1L);

        assertFalse(result.isPresent());
        verify(borrowingRecordRepository, times(1)).findById(1L);
    }
}
