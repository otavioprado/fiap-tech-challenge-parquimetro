package br.com.fiap.parquimetro.repository;

import br.com.fiap.parquimetro.model.Estacionamento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstacionamentoRepository extends MongoRepository<Estacionamento, String> {
}

