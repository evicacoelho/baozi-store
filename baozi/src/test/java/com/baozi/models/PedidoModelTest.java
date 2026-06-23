package com.baozi.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PedidoModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidPedido() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(1L);
        pedido.setProdutoId(1L);
        pedido.setQuantidade(10);

        Set<ConstraintViolation<Pedido>> violations = validator.validate(pedido);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenClienteIdIsNull() {
        Pedido pedido = new Pedido();
        pedido.setProdutoId(1L);
        pedido.setQuantidade(10);

        Set<ConstraintViolation<Pedido>> violations = validator.validate(pedido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("clienteId"))
                .count());
    }

    @Test
    void shouldFailWhenProdutoIdIsNull() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(1L);
        pedido.setQuantidade(10);

        Set<ConstraintViolation<Pedido>> violations = validator.validate(pedido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("produtoId"))
                .count());
    }

    @Test
    void shouldFailWhenQuantidadeIsNull() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(1L);
        pedido.setProdutoId(1L);

        Set<ConstraintViolation<Pedido>> violations = validator.validate(pedido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("quantidade"))
                .count());
    }

    @Test
    void shouldFailWhenQuantidadeIsZero() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(1L);
        pedido.setProdutoId(1L);
        pedido.setQuantidade(0);

        Set<ConstraintViolation<Pedido>> violations = validator.validate(pedido);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldSetDataPedidoOnCreate() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(1L);
        pedido.setProdutoId(1L);
        pedido.setQuantidade(10);

        pedido.onCreate();

        assertNotNull(pedido.getDataPedido());
    }
}