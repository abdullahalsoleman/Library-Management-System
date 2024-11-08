package com.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestStatus {
    private boolean status;
    private String message;

    // Make the constructor public
    public RequestStatus(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
    public boolean isSuccess(){
        return status;
    }
}
