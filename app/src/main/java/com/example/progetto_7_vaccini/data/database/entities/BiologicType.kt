package com.example.progetto_7_vaccini.data.database.entities

enum class BiologicType(
    val nome: String,
    val principioAttivo: String
) {

    // =========================
    // ANTI‑TNF
    // =========================
    ADALIMUMAB("Adalimumab", "Humira"),
    ETANERCEPT("Etanercept", "Enbrel"),
    INFLIXIMAB("Infliximab", "Remicade"),
    GOLIMUMAB("Golimumab", "Simponi"),
    CERTOLIZUMAB("Certolizumab pegol", "Cimzia"),

    // =========================
    // ANTI‑IL‑17
    // =========================
    SECUKINUMAB("Secukinumab", "Cosentyx"),
    IXEKIZUMAB("Ixekizumab", "Taltz"),

    // =========================
    // ANTI‑IL‑12/23
    // =========================
    USTEKINUMAB("Ustekinumab", "Stelara"),

    // =========================
    // ANTI‑IL‑23
    // =========================
    GUSELKUMAB("Guselkumab", "Tremfya"),
    RISANKIZUMAB("Risankizumab", "Skyrizi"),
    TILDRAKIZUMAB("Tildrakizumab", "Ilumetri"),

    // =========================
    // ANTI‑IL‑6
    // =========================
    TOCILIZUMAB("Tocilizumab", "RoActemra"),
    SARILUMAB("Sarilumab", "Kevzara"),

    // =========================
    // JAK‑INHIBITORS
    // =========================
    TOFACITINIB("Tofacitinib", "Xeljanz"),
    UPADACITINIB("Upadacitinib", "Rinvoq"),
    BARICITINIB("Baricitinib", "Olumiant")
}

