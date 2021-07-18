package io.github.israelcarlos;

import io.github.israelcarlos.domain.entity.Cliente;
import io.github.israelcarlos.domain.entity.Produto;
import io.github.israelcarlos.domain.repository.Clientes;
import io.github.israelcarlos.domain.repository.Produtos;
import org.h2.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class VendasApplication {

    @Bean
    public CommandLineRunner commandLineRunner(@Autowired Clientes clientes, @Autowired Produtos produtos) {
        return args -> {
            Cliente c = new Cliente("João", "07374924909");
            clientes.save(c);

            Produto p = new Produto(null, "Cadeira", BigDecimal.valueOf(100));
            produtos.save(p);

            p = new Produto(null, "Mesa", BigDecimal.valueOf(500));
            produtos.save(p);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}
