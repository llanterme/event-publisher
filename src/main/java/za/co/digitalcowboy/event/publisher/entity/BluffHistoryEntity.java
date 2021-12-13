package za.co.digitalcowboy.event.publisher.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bluff_history")
public class BluffHistoryEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bluff_history_id")
    private int bluffHistoryId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "angry_score")
    private String angryScore;

    @Column(name = "joy_score")
    private String joyScore;

    @Column(name = "sorrow_score")
    private String sorrowScore;

    @Column(name = "surprised_score")
    private String surprisedScore;

    @Column(name = "date_added")
    private String dateAdded;

    @Column(name = "total_weighting")
    private String totalWeighting;

    @Column(name = "image_url")
    private String imageUrl;




}


