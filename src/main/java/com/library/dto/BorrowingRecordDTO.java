package com.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BorrowingRecordDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Patron ID is required")
    private Long patronId;

    @NotNull(message = "Borrow date is required")
    private LocalDate borrowDate;

    private LocalDate returnDate;
}
