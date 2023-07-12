package tw.hicamp.campsite.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampspaceRepository extends JpaRepository<Campspace, Integer> {
	 Campspace findByCampspaceNo(Integer campspaceNo);
}
