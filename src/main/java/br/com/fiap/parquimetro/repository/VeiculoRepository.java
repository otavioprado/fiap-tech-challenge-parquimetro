package br.com.fiap.parquimetro.repository;

import br.com.fiap.parquimetro.model.Veiculo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends MongoRepository<Veiculo, String> {
    boolean existsByPlaca(String placa);

}
