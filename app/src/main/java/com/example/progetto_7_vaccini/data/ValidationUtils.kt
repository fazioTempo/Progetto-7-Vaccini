package com.example.progetto_7_vaccini.data

object ValidationUtils {
    /**
     * Valida il nome o cognome: permette solo lettere (incluse accentate e Unicode),
     * spazi, apostrofi e trattini.
     */
    fun isValidName(text: String): Boolean {
        // Utilizziamo una Regex che include \p{L} (qualsiasi lettera Unicode)
        val nameRegex = "^[\\p{L}\\s'-]*$".toRegex()
        return text.matches(nameRegex)
    }

    /**
     * Valida il formato email utilizzando una Regex standard.
     */
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    /**
     * Valida la password: almeno 10 caratteri, una maiuscola, una minuscola,
     * un numero e un carattere speciale.
     */
    fun isValidPassword(password: String): Boolean {
        if (password.length < 10) return false
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return hasUppercase && hasLowercase && hasDigit && hasSpecial
    }
}
