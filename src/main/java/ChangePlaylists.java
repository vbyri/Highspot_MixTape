import lombok.Data;

import java.util.List;

@Data
public class ChangePlaylists {
    private List<Playlist> addUpdatePlaylists;
    private List<String> removePlaylistIds;
}
