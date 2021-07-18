package io.github.israelcarlos.domain.repository;

import io.github.israelcarlos.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
