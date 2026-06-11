package com.joserodriguezdeveloper.analiticas.repositorio;


import com.joserodriguezdeveloper.analiticas.modelo.Dto.VisitanteFiltroDTO;
import com.joserodriguezdeveloper.analiticas.modelo.EventoAnalitica;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AnaliticaSpecifications {

    public static Specification<EventoAnalitica> conFiltros(VisitanteFiltroDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getCiudad() != null && !filtro.getCiudad().isEmpty()) {
                predicates.add(cb.equal(root.get("ciudad"), filtro.getCiudad()));
            }
            if (filtro.getDispositivo() != null && !filtro.getDispositivo().isEmpty()) {
                predicates.add(cb.equal(root.get("dispositivo"), filtro.getDispositivo()));
            }
            if (filtro.getNavegador() != null && !filtro.getNavegador().isEmpty()) {
                predicates.add(cb.equal(root.get("navegador"), filtro.getCiudad())); // Nota: asegúrate que sea get("navegador")
            }
            if (filtro.getPais() != null && !filtro.getPais().isEmpty()) {
                predicates.add(cb.equal(root.get("pais"), filtro.getPais()));
            }
            if (filtro.getRegion() != null && !filtro.getRegion().isEmpty()) {
                predicates.add(cb.equal(root.get("region"), filtro.getRegion()));
            }
            if (filtro.getSistemaOperativo() != null && !filtro.getSistemaOperativo().isEmpty()) {
                predicates.add(cb.equal(root.get("sistemaOperativo"), filtro.getSistemaOperativo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}