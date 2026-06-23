package com.baozi.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Id do cliente é obrigatório")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @NotNull(message = "Id do produto é obrigatório")
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(nullable = false)
    @NotNull(message = "Precisa-se de uma quantidade do produto")
    @Positive(message = "Precisa ser mais que 0")
    private Integer quantidade;

    @Column(name = "data_pedido", updatable = false)
    private LocalDateTime dataPedido;

    @PrePersist
    protected void onCreate() {
        dataPedido = LocalDateTime.now();
    }

}
