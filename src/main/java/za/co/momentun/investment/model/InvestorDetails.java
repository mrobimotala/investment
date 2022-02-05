package za.co.momentun.investment.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "investor_details")
public class InvestorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "dateOfBirth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "mobileNumber", nullable = false)
    private String mobileNumber;

    @Column(name = "emailAddress", nullable = false)
    private String emailAddress;

    @OneToMany(mappedBy="id")
    private List<Product> product;

    @OneToMany(mappedBy="id")
    private Set<Withdrawal> withdrawal;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public Set<Withdrawal> getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Set<Withdrawal> withdrawals) {
        this.withdrawal = withdrawal;
    }

    public int getAge() {
        LocalDate currentDate =  LocalDate.now();
        LocalDate birthDate = LocalDate.parse(this.getDateOfBirth().toString());
        if ((birthDate != null) && (currentDate != null))
            return Period.between(birthDate, currentDate).getYears();
        return 0;
    }
}
