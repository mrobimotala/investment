package za.co.momentun.investment.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "withdrawal")
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "amount", nullable = true)
    private double amount;

    @ManyToOne
    @JoinColumn(name="investor_details_id", nullable=false)
    private InvestorDetails investorDetails;

    @OneToMany(mappedBy="id")
    private Set<AuditTrail> auditTrails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public InvestorDetails getInvestorDetails() {
        return investorDetails;
    }

    public void setInvestorDetails(InvestorDetails investorDetails) {
        this.investorDetails = investorDetails;
    }

    public Set<AuditTrail> getAuditTrails() {
        return auditTrails;
    }

    public void setAuditTrails(Set<AuditTrail> auditTrails) {
        this.auditTrails = auditTrails;
    }
}
