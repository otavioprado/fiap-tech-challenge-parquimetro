package br.com.fiap.parquimetro.repository;

import br.com.fiap.parquimetro.model.Estacionamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EstacionamentoRepository extends MongoRepository<Estacionamento, String> {
    List<Estacionamento> findAllBySaidaIsNullAndEntradaLessThanEqual(LocalDateTime entrada);
}

