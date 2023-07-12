package tw.hicamp.member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "manager")
@Data
public class Manager {
	@Id
	private String managerAccount;
	private String managerPassword;
	private String managerName;
	public String getManagerAccount() {
		return managerAccount;
	}
	public void setManagerAccount(String managerAccount) {
		this.managerAccount = managerAccount;
	}
	public String getManagerPassword() {
		return managerPassword;
	}
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
}
