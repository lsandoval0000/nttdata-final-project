package com.nttdata.creditspersonalservice.service.externalapi.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    @JsonIgnore
    private int status;
    private String message;
}
