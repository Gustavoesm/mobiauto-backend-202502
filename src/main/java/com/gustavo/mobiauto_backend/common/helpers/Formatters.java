package com.gustavo.mobiauto_backend.common.helpers;

public class Formatters {
    private Formatters() {
    }

    public static String formatCnpj(String cnpj) {
        if (cnpj == null) {
            return null;
        }

        String cleanCnpj = cnpj.replaceAll("[./-]", "");

        String cnpjStr = String.format("%014d", Long.parseLong(cleanCnpj));

        return String.format("%s.%s.%s/%s-%s",
                cnpjStr.substring(0, 2),
                cnpjStr.substring(2, 5),
                cnpjStr.substring(5, 8),
                cnpjStr.substring(8, 12),
                cnpjStr.substring(12, 14));
    }
}
