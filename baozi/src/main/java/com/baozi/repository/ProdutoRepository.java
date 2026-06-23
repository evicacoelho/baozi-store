package com.baozi.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baozi.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByDisponivelTrue();

    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);

}
