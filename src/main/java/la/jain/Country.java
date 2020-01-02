package la.jain;

public class Country implements Comparable<Country> {
    private String name;
    private String shortCode;
    private String capital;

    public Country(String name, String code){
        this.name=name;
        this.shortCode=code;
    }

    public Country() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getShortCode() {
        return shortCode;
    }
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getCapital() {
        return capital;
    }
    public void setCapital(String capital) {
        this.capital = capital;
    }

    public int compareTo(Country country) {
        return this.getShortCode().compareTo(country.getShortCode())*-1;
    }

    @Override
    public String toString(){
        return this.getShortCode() + "::" + this.getName() + "::" + this.getCapital();
    }
}
