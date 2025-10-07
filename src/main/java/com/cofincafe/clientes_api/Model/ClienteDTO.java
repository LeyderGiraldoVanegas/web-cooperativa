package com.cofincafe.clientes_api.Model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String displayName;
    private Long officeId;
    private String officeName;
    private Long legalFormId;
    private String legalFormName;
    private LocalDate activationDate;
    private Double balance;
    private Boolean active;
    private String status; // por ejemplo: Active, Closed
    private Boolean isStaff;
    private String savingsProductName;
    private List<String> groups; // nombres de grupos asociados
    private List<String> collaterals; // colaterales

    // Constructor vacío
    public ClienteDTO() {}

    // Constructor simplificado
    public ClienteDTO(Long id, String firstname, Double balance) {
        this.id = id;
        this.firstname = firstname;
        this.balance = balance;
    }

   // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Long getOfficeId() { return officeId; }
    public void setOfficeId(Long officeId) { this.officeId = officeId; }

    public String getOfficeName() { return officeName; }
    public void setOfficeName(String officeName) { this.officeName = officeName; }

    public Long getLegalFormId() { return legalFormId; }
    public void setLegalFormId(Long legalFormId) { this.legalFormId = legalFormId; }

    public String getLegalFormName() { return legalFormName; }
    public void setLegalFormName(String legalFormName) { this.legalFormName = legalFormName; }

    public LocalDate getActivationDate() { return activationDate; }
    public void setActivationDate(LocalDate activationDate) { this.activationDate = activationDate; }

    public Double getAccountBalance() { return balance; } 
    public void setAccountBalance(Double accountBalance) { this.balance = accountBalance; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsStaff() { return isStaff; }
    public void setIsStaff(Boolean isStaff) { this.isStaff = isStaff; }

    public String getSavingsProductName() { return savingsProductName; }
    public void setSavingsProductName(String savingsProductName) { this.savingsProductName = savingsProductName; }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }

    public List<String> getCollaterals() { return collaterals; }
    public void setCollaterals(List<String> collaterals) { this.collaterals = collaterals; }


     @Override
    public String toString() {
        return "ClienteDTO{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", balance=" + balance +
                '}';
    }
}
