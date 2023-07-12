package tw.hicamp.member.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.hicamp.member.model.Manager;
import tw.hicamp.member.model.ManagerRepository;

@Service
public class ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	public Manager findbyaccount(String account) {
		return managerRepository.findBymanagerAccount(account);
	}
	
}
