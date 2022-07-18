package reactive.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private Login login;
    private Salary salary;
    private Address address;
    private Education education;
    private Payment payment;
    private Experience experience;
}
