package properties;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class Session {
    private int id;
    private LocalDateTime dateAndTime;
    private int filmId;

    public String getDay(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String text = dateAndTime.format(formatter);
        return text;
    }

    public String getTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String text = dateAndTime.format(formatter);
        return text;
    }
}
