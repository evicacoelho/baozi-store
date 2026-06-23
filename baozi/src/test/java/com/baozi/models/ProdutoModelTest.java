package com.baozi.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidProduto() {
        Produto produto = new Produto();
        produto.setNome("Pão Chinês Tradicional");
        produto.setPreco(new BigDecimal("5.50"));
        produto.setDisponivel(true);

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNomeIsNull() {
        Produto produto = new Produto();
        produto.setPreco(new BigDecimal("5.50"));
        produto.setDisponivel(true);

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("nome"))
                .count());
    }

    @Test
    void shouldFailWhenPrecoIsNull() {
        Produto produto = new Produto();
        produto.setNome("Pão Chinês");
        produto.setDisponivel(true);

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("preco"))
                .count());
    }

    @Test
    void shouldFailWhenPrecoIsZero() {
        Produto produto = new Produto();
        produto.setNome("Pão Chinês");
        produto.setPreco(BigDecimal.ZERO);
        produto.setDisponivel(true);

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenPrecoIsNegative() {
        Produto produto = new Produto();
        produto.setNome("Pão Chinês");
        produto.setPreco(new BigDecimal("-1.00"));
        produto.setDisponivel(true);

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldSetDataCadastroOnCreate() {
        Produto produto = new Produto();
        produto.setNome("Pão Chinês");
        produto.setPreco(new BigDecimal("5.50"));
        produto.setDisponivel(true);

        produto.onCreate();

        assertNotNull(produto.getDataCadastro());
    }

    @Test
    void shouldSetDisponivelTrueOnCreateIfNull() {
        Produto produto = new Produto();
        produto.setNome("Pão Chinês");
        produto.setPreco(new BigDecimal("5.50"));
        produto.setDisponivel(null);

        produto.onCreate();

        assertTrue(produto.getDisponivel());
    }
}