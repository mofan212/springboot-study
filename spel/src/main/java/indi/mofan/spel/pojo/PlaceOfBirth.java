package indi.mofan.spel.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mofan
 * @date 2022/10/18 16:22
 */
@Getter
@Setter
public class PlaceOfBirth {
    private String city;
    private String country;

    public PlaceOfBirth(String city) {
        this.city=city;
    }

    public PlaceOfBirth(String city, String country) {
        this(city);
        this.country = country;
    }
}
