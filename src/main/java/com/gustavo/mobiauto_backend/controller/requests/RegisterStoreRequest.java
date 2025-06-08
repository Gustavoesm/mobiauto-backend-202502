package com.gustavo.mobiauto_backend.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterStoreRequest {
    private String storeName;
    private Long cnpj;
}
