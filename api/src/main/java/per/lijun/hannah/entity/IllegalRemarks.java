package per.lijun.hannah.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IllegalRemarks {


    private String id;

    private String title;

    private String senderId;

    private String receiverId;

    private String content;

    private String date;
}
