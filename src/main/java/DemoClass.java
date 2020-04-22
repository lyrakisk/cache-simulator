public class DemoClass {
    private int number;

    public DemoClass(int n) {
        this.number = n;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int n) {
        this.number = n;
    }

    public boolean isPositive() {
        if (number > 0) {
            return true;
        } else {
            return false;
        }
    }
}
