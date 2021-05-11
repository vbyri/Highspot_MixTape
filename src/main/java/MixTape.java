import lombok.Data;

import java.util.List;

@Data
public class MixTape {
    private List<User> users;
    private List<Playlist> playlists;
    private List<Song> songs;
}
