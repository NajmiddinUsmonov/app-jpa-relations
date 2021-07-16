package uz.usmanov.appjparelations.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")

public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;



    @Column(nullable = false)
    private String name;

    @ManyToOne//=>Many groups TO One faculty
    private Faculty faculty;

//    @OneToMany //=> One Group TO Many students
//    private List<Student> students;
}
