package br.com.destinify.destinify.domain.enums;

public enum DocumentType {
    RG("Registro Geral"),
    CPF("Cadastro de Pessoas Físicas"),
    CNH("Carteira Nacional de Habilitação"),
    PASSPORT("Passaporte");

    private final String description;

    DocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
