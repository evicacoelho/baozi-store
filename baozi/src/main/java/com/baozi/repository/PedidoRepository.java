package com.baozi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baozi.models.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByProdutoId(Long produtoId);

    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

}
