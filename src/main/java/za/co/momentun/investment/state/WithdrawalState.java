package za.co.momentun.investment.state;

public enum WithdrawalState {
    INITIALIZED("Initialized"),
    INPROGRESS("Inprogress"),
    COMPLETED("Completed"),
    ERROR("validation failed");

    public String state;

    WithdrawalState(String value) {
        this.state = value;
    }

}
