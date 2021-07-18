package io.github.israelcarlos.domain.repository;

import io.github.israelcarlos.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
