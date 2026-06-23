package com.baozi.controllers;

import com.baozi.models.Pedido;
import com.baozi.repository.PedidoRepository;
import com.baozi.repository.ClienteRepository;
import com.baozi.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody Pedido pedido) {
        // Validar se o cliente existe
        if (!clienteRepository.existsById(pedido.getClienteId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cliente não encontrado com ID: " + pedido.getClienteId());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Validar se o produto existe
        if (!produtoRepository.existsById(pedido.getProdutoId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Produto não encontrado com ID: " + pedido.getProdutoId());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            Pedido novoPedido = pedidoRepository.save(pedido);
            return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao criar pedido: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> new ResponseEntity<>(pedido, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPedido(@PathVariable Long id, @Valid @RequestBody Pedido pedidoAtualizado) {
        return pedidoRepository.findById(id)
                .map(pedidoExistente -> {
                    // Validar cliente
                    if (!clienteRepository.existsById(pedidoAtualizado.getClienteId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Cliente não encontrado com ID: " + pedidoAtualizado.getClienteId());
                        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                    }

                    // Validar produto
                    if (!produtoRepository.existsById(pedidoAtualizado.getProdutoId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Produto não encontrado com ID: " + pedidoAtualizado.getProdutoId());
                        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                    }

                    pedidoExistente.setClienteId(pedidoAtualizado.getClienteId());
                    pedidoExistente.setProdutoId(pedidoAtualizado.getProdutoId());
                    pedidoExistente.setQuantidade(pedidoAtualizado.getQuantidade());

                    Pedido pedidoSalvo = pedidoRepository.save(pedidoExistente);
                    return new ResponseEntity<>(pedidoSalvo, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarPedido(@PathVariable Long id) {
        try {
            if (pedidoRepository.existsById(id)) {
                pedidoRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> buscarPorCliente(@PathVariable Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cliente não encontrado com ID: " + clienteId);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<?> buscarPorProduto(@PathVariable Long produtoId) {
        if (!produtoRepository.existsById(produtoId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Produto não encontrado com ID: " + produtoId);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        List<Pedido> pedidos = pedidoRepository.findByProdutoId(produtoId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<Pedido>> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio, 
            @RequestParam LocalDateTime fim) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(inicio, fim);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }
}