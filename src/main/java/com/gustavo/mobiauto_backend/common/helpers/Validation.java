package com.gustavo.mobiauto_backend.common.helpers;

public final class Validation {

    public static final String EMAIL_REGEX = "^[\\w+.]+@\\w+\\.\\w{2,}(?:\\.\\w{2})?$";

    private Validation() {
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }

        String cleanCnpj = cnpj.replaceAll("[./-]", "");

        if (!cleanCnpj.matches("\\d+")) {
            return false;
        }

        if (cleanCnpj.length() > 14) {
            return false;
        }

        String cnpjStr = String.format("%014d", Long.parseLong(cleanCnpj));
        if (cnpjStr.length() != 14) {
            return false;
        }

        if (cnpjStr.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] weights1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] weights2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

        int sum1 = 0;
        for (int i = 0; i < 12; i++) {
            sum1 += Character.getNumericValue(cnpjStr.charAt(i)) * weights1[i];
        }

        int digit1 = (sum1 % 11 < 2) ? 0 : 11 - (sum1 % 11);
        if (digit1 != Character.getNumericValue(cnpjStr.charAt(12))) {
            return false;
        }

        int sum2 = 0;
        for (int i = 0; i < 13; i++) {
            sum2 += Character.getNumericValue(cnpjStr.charAt(i)) * weights2[i];
        }

        int digit2 = (sum2 % 11 < 2) ? 0 : 11 - (sum2 % 11);
        return digit2 == Character.getNumericValue(cnpjStr.charAt(13));
    }
}