package com.baozi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baozi.models.Pedido;
import com.baozi.repository.ClienteRepository;
import com.baozi.repository.PedidoRepository;
import com.baozi.repository.ProdutoRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/api/pedidos")
    public ResponseEntity<?> criarPedido(@Valid @RequestBody Pedido pedido) {
        // Validação se cliente existe
        if (!clienteRepository.existsById(pedido.getClienteId())) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.BAD_REQUEST);
        }

        // Validação se produto existe
        if (!produtoRepository.existsById(pedido.getProdutoId())) {
            return new ResponseEntity<>("Produto não encontrado", HttpStatus.BAD_REQUEST);
        }

        try {
            Pedido novoPedido = pedidoRepository.save(pedido);
            return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
        } catch (Exception e ) {
            return new ResponseEntity<>("Falha ao criar pedido: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
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
    public ResponseEntity<?> atualizarPedido(
        @PathVariable Long id,
        @Valid @RequestBody Pedido pedidoAtualizado
    ) {
        return pedidoRepository.findById(id)
            .map(pedidoExistente -> {
                // Validar cliente
                if (!clienteRepository.existsById(pedidoAtualizado.getClienteId())) {
                    return new ResponseEntity<>("Cliente não encontrado", HttpStatus.BAD_REQUEST);
                }

                // Validar produto
                if (!produtoRepository.existsById(pedidoAtualizado.getProdutoId())) {
                    return new ResponseEntity<>("Produto não encontrado", HttpStatus.BAD_REQUEST);
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
            pedidoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/produto/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Pedido> pedidos = pedidoRepository.findClienteId(clienteId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Pedido>> buscarPorProduto(@PathVariable Long produtoId) {
        if(!produtoRepository.existsById(produtoId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Pedido> pedidos = pedidoRepository.findProdutoId(produtoId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }
    
}
