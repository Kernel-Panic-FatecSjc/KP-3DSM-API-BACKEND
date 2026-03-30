package com.kernelpanic.api_gateway.dtos;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErroRespostaDTO (
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {}