package mb.company.reportes.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mb.company.reportes.model.ActividadesAvaladas;


@Repository
public interface ActividadesAvaladasRepository extends JpaRepository<ActividadesAvaladas, Long> {

}
