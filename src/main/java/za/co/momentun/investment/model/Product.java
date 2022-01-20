package za.co.momentun.investment.model;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "balance", nullable = true)
    private double balance;

    @ManyToOne
    @JoinColumn(name="InvestorDetails_id", nullable=false)
    private InvestorDetails InvestorDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public za.co.momentun.investment.model.InvestorDetails getInvestorDetails() {
        return InvestorDetails;
    }

    public void setInvestorDetails(za.co.momentun.investment.model.InvestorDetails investorDetails) {
        InvestorDetails = investorDetails;
    }
}
