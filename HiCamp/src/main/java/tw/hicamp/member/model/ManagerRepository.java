package tw.hicamp.member.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, String> {

	Manager findBymanagerAccount(String account);
	
}
