package uz.usmanov.appjparelations.payload;


import lombok.Data;

@Data
public class UniversityDto { ///Ma'lumotlarni tashish uchun xizmat qiladi
    private String name;

    private String city;
    private String district;
    private String street;
}
