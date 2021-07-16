package uz.usmanov.appjparelations.payload;

import lombok.Data;

@Data
public class Student1Dto {
    private String firstName;

    private String lastName;

    private String city;

    private String district;

    private String street;

    private Integer subject_id;
}
