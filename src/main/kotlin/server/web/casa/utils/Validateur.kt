package server.web.casa.utils
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

fun normalizeAndValidatePhoneNumber(phoneNumber: String?): String? {
    // 1️⃣ Nettoyer le numéro : enlever espaces, tirets, etc.
    var cleaned: String? = phoneNumber?.replace(Regex("[\\s-]"), "")

    // 2️⃣ Supprimer les préfixes possibles
    cleaned = cleaned
        ?.removePrefix("+243")
        ?.removePrefix("00243")
        ?.removePrefix("0").toString()

    // 3️⃣ Définir les préfixes valides en RDC
    val regex = Regex("^(99|97|98|85|89|80|84|81|82|83|91|90)\\d{7}$")

    // 4️⃣ Vérifier si le numéro correspond au format attendu
    return if (regex.matches(cleaned)) {
        // 5️⃣ Retourner le format international standardisé
        "+243$cleaned"
    } else {
        // Retourne null si le numéro n’est pas valide
        phoneNumber
    }
}
fun normalizeAndValidatePhoneNumberCD(phoneNumber: String?): String? {
    // 1️⃣ Nettoyer le numéro : enlever espaces, tirets, etc.
    var cleaned: String? = phoneNumber?.replace(Regex("[\\s-]"), "")

    // 2️⃣ Supprimer les préfixes possibles
    cleaned = cleaned
        ?.removePrefix("+243")
        ?.removePrefix("00243")
        ?.removePrefix("0").toString()

    // 3️⃣ Définir les préfixes valides en RDC
    val regex = Regex("^(99|97|98|85|89|80|84|81|82|83|91|90)\\d{7}$")

    // 4️⃣ Vérifier si le numéro correspond au format attendu
    return if (regex.matches(cleaned)) {
        // 5️⃣ Retourner le format international standardisé
        "+243$cleaned"
    } else {
        // Retourne null si le numéro n’est pas valide
        null
    }
}

fun normalizeAndValidatePhoneNumberUniversal(
    phone: String?,
    defaultRegion: String = "ZZ" // ZZ = inconnu → libphonenumber tente de deviner
): String? {
    if (phone.isNullOrBlank()) return null

    // 1️⃣ Nettoyage
    var cleaned = phone.replace("[^0-9+]+".toRegex(), "")

    // 2️⃣ Transforme 00 → +
    if (cleaned.startsWith("00")) {
        cleaned = "+" + cleaned.drop(2)
    }

    val util = PhoneNumberUtil.getInstance()

    return try {
        val parsed = util.parse(cleaned, defaultRegion)

        // 3️⃣ Valide format complet selon pays / longueur / type
        if (!util.isValidNumber(parsed)) return null

        // 4️⃣ Retourne propre en E.164
        util.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)

    } catch (e: NumberParseException) {
        null
    }
}

fun isEmailValid(email: String?): Boolean {
    if (email.isNullOrBlank()) return false

    val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    return emailRegex.matches(email)
}

fun toPascalCase(input: String): String {
    return input.split(" ", "-", "_") // sépare selon espaces ou tirets
        .filter { it.isNotEmpty() }   // enlève les mots vides
        .joinToString("") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}