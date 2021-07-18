package io.github.israelcarlos.service;

import io.github.israelcarlos.domain.entity.Pedido;
import io.github.israelcarlos.domain.enums.StatusPedido;
import io.github.israelcarlos.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar (PedidoDTO pedidoDTO);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatusPedido(Integer id, StatusPedido status);
}
