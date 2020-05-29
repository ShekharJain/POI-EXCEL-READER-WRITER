package la.jain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Country implements Comparable<Country> {
    private String name;
    private String shortCode;
    private String capital;
    private String continent;

    public Country(String continent){
        this.continent=continent;
    }

    @JsonProperty("countryName")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("countryCode")
    public String getShortCode() {
        return shortCode;
    }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    @JsonIgnore
    public String getCapital() {
        return capital;
    }
    public void setCapital(String capital) {
        this.capital = capital;
    }

    @JsonProperty("continent")
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.capital = continent;
    }

    public int compareTo(Country country) {
        return this.getShortCode().compareTo(country.getShortCode())*-1;
    }

    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
        //return this.getShortCode() + "::" + this.getName() + "::" + this.getCapital();
    }
}