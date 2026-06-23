package com.baozi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.baozi.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase (String nome);

    List<Cliente> findByClienteDesdeAfter (LocalDate data);

    boolean existsByNome(String nome);

}
