package com.library.repository;

import com.library.IRepository.BorrowingRecordRepository;
import com.library.model.BorrowingRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingRecordRepositoryTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Test
    public void testFindByBookIdAndPatronIdAndReturnDateIsNull() {
        BorrowingRecord record = new BorrowingRecord();
        record.setId(1L);
        record.setBookId(1L);
        record.setPatronId(2L);
        record.setBorrowDate(new Date());

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 2L)).thenReturn(Optional.of(record));

        Optional<BorrowingRecord> foundRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 2L);

        assertTrue(foundRecord.isPresent());
        assertEquals(1L, foundRecord.get().getBookId());
        verify(borrowingRecordRepository, times(1)).findByBookIdAndPatronIdAndReturnDateIsNull(1L, 2L);
    }
}
