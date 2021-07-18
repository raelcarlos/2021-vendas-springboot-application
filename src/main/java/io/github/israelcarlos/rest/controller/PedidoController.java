package io.github.israelcarlos.rest.controller;

import io.github.israelcarlos.domain.entity.ItemPedido;
import io.github.israelcarlos.domain.entity.Pedido;
import io.github.israelcarlos.domain.enums.StatusPedido;
import io.github.israelcarlos.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.israelcarlos.rest.dto.InformacaoItemPedidoDTO;
import io.github.israelcarlos.rest.dto.InformacoesPedidoDTO;
import io.github.israelcarlos.rest.dto.PedidoDTO;
import io.github.israelcarlos.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO pedidoDTO) {
        Pedido pedido = service.salvar(pedidoDTO);
        return pedido.getId();
    }

    @GetMapping ("/{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return service
                .obterPedidoCompleto(id)
                .map(p -> converterPedido(p))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

    }

    @PatchMapping ("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getStatus();
        service.atualizaStatusPedido(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidoDTO converterPedido(Pedido p) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(p.getId())
                .cpf(p.getCliente().getCpf())
                .nomeCliente(p.getCliente().getNome())
                .total(p.getTotal())
                .dataPedido(p.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(p.getStatus().name())
                .itens(converter(p.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map( item -> InformacaoItemPedidoDTO
                                            .builder()
                                            .descricaoProduto(item.getProduto().getDescricao())
                                            .precoUnitario(item.getProduto().getPreco())
                                            .quantidade(item.getQuantidade()).build()
        ).collect(Collectors.toList());
    }

}
