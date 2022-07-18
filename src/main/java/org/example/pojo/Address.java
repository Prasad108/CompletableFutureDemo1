package reactive.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Address {
    private String houseName;
    private String line1;
    private String line2;
    private String state;
    private String district;
}
