package server.web.casa.utils

fun normalizeAndValidatePhoneNumber(phoneNumber: String): String? {
    // 1️⃣ Nettoyer le numéro : enlever espaces, tirets, etc.
    var cleaned = phoneNumber.replace(Regex("[\\s-]"), "")

    // 2️⃣ Supprimer les préfixes possibles
    cleaned = cleaned
        .removePrefix("+243")
        .removePrefix("00243")
        .removePrefix("0")

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
