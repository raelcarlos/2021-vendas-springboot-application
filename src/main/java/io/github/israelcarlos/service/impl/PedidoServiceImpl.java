package io.github.israelcarlos.service.impl;

import io.github.israelcarlos.domain.entity.Cliente;
import io.github.israelcarlos.domain.entity.ItemPedido;
import io.github.israelcarlos.domain.entity.Pedido;
import io.github.israelcarlos.domain.entity.Produto;
import io.github.israelcarlos.domain.enums.StatusPedido;
import io.github.israelcarlos.domain.repository.Clientes;
import io.github.israelcarlos.domain.repository.ItensPedido;
import io.github.israelcarlos.domain.repository.Pedidos;
import io.github.israelcarlos.domain.repository.Produtos;
import io.github.israelcarlos.exception.PedidoNaoEncontradoException;
import io.github.israelcarlos.exception.RegraNegocioException;
import io.github.israelcarlos.rest.dto.ItemPedidoDTO;
import io.github.israelcarlos.rest.dto.PedidoDTO;
import io.github.israelcarlos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO pedidoDTO) {
        Integer idCliente = pedidoDTO.getCliente();
        Cliente cliente = clientesRepository.findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        repository.save(pedido);
        List<ItemPedido> itensPedido = converterItens(pedido, pedidoDTO.getItens());
        itensPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);

        return pedido;
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens");
        }
        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository.findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: " + idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer idPedido) {
        return repository.findByIdFetchItens(idPedido);
    }

    @Override
    @Transactional
    public void atualizaStatusPedido(Integer id, StatusPedido status) {
        repository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(status);
                    return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }
}
