package properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private int id;
    private String title;
    private String description;
    private String posterName;
    private String trailer;
    private boolean isInRent;
}
