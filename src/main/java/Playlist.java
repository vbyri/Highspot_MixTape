import lombok.Data;

import java.util.List;

@Data
public class Playlist {
    private String id;
    private String user_id;
    private List<String> song_ids;
}
