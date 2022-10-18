package indi.mofan.spel.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mofan
 * @date 2022/10/18 16:23
 */
public class Society {

    @Getter
    @Setter
    private String name;

    public static String Advisors = "advisors";
    public static String President = "president";

    @Getter
    private final List<Inventor> members = new ArrayList<>();

    @Getter
    private final Map<String, Object> officers = new HashMap<>();

    public boolean isMember(String name) {
        for (Inventor inventor : members) {
            if (inventor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
