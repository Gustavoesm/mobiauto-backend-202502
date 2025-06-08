package com.gustavo.mobiauto_backend.infra.config;

public class Formatters {
    private Formatters() {
    }

    public static String formatCnpj(Long cnpj) {
        String cnpjStr = String.format("%014d", cnpj);
        return String.format("%s.%s.%s/%s-%s",
                cnpjStr.substring(0, 2),
                cnpjStr.substring(2, 5),
                cnpjStr.substring(5, 8),
                cnpjStr.substring(8, 12),
                cnpjStr.substring(12, 14));
    }
}
