package mb.company.reportes.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mb.company.reportes.model.Medico;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
}
