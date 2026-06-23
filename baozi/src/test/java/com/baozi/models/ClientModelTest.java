package com.baozi.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClienteModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva123456");
        cliente.setClienteDesde(LocalDate.of(2024, 1, 15));

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNomeIsNull() {
        Cliente cliente = new Cliente();
        cliente.setClienteDesde(LocalDate.now());

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("nome"))
                .count());
    }

    @Test
    void shouldFailWhenNomeIsTooShort() {
        Cliente cliente = new Cliente();
        cliente.setNome("A");
        cliente.setClienteDesde(LocalDate.now());

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenClienteDesdeIsNull() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("clienteDesde"))
                .count());
    }

    @Test
    void shouldSetDataCadastroOnCreate() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setClienteDesde(LocalDate.now());

        cliente.onCreate();

        assertNotNull(cliente.getDataCadastro());
        assertEquals(LocalDate.now(), cliente.getDataCadastro());
    }

    @Test
    void shouldSetClienteDesdeOnCreateIfNull() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setClienteDesde(null);

        cliente.onCreate();

        assertNotNull(cliente.getClienteDesde());
        assertEquals(LocalDate.now(), cliente.getClienteDesde());
    }
}