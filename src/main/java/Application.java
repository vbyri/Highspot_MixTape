import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Application {
    static MixTape mixTape;
    static HashMap<String, Playlist> playListMap;
    static ChangePlaylists changePlayLists;
    static String path;

    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Please enter mixtape.json and change.json file location along with file name");
            return;
        }
        final String mixtapefile = args[0];
        final String changefile = args[1];

        mixTape = getMixTape(mixtapefile);
        if(mixTape == null){
            System.out.println("mixtape file is empty!");
            return;
        }
        if(mixTape.getPlaylists().size() > 0) {
            playListMap = new HashMap<>();
            loadPlayListMap();
        }

        changePlayLists = getChangePlayLists(changefile);
        if(changePlayLists == null){
            System.out.println("Change PlayList file is empty!");
            return;
        }
        //update playlist and write to file
        updatePlayList();

    }

    private static void updatePlayList(){
        List<Playlist> playLists = changePlayLists.getAddUpdatePlaylists();
        for(Playlist p: playLists){
            addUpdatePlaylist(p);
        }

        for(String id: changePlayLists.getRemovePlaylistIds()){
            removePlaylist(id);
        }

        writePlayListFile();
    }

    private static void writePlayListFile(){
        List<Playlist> resultPlayList = new ArrayList<>(playListMap.values());
        mixTape.setPlaylists(resultPlayList);
        try {
            ObjectMapper mapper = new ObjectMapper();

            String jsonString = mapper.writeValueAsString(mixTape);
            String filepath = path.substring(0,path.lastIndexOf("\\"));
            FileWriter writer = new FileWriter(filepath + "\\Modifiedmixtape.json");
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing result to file");
        }
    }

    private static ChangePlaylists getChangePlayLists(final String filename){
        try {
            File file = new File(filename);
            ObjectMapper mapper = new ObjectMapper();
            ChangePlaylists changePlaylists = mapper.readValue(file, ChangePlaylists.class);
            return changePlaylists;
        }catch (Exception ex){
            System.out.println("Error reading json file");
            System.out.println(ex);
        }
        return null;
    }
    private static MixTape getMixTape(final String filename){
        try {
            File file = new File(filename);
            path = file.getPath();
            ObjectMapper mapper = new ObjectMapper();
            mixTape = mapper.readValue(file, MixTape.class);
            return mixTape;
        }catch(Exception ex){
           System.out.println("Error reading json file");
           System.out.println(ex);
        }
        return null;
    }

    private static void loadPlayListMap(){
        for(Playlist p:mixTape.getPlaylists()){
            playListMap.put(p.getId(), p);
        }
    }

    private static void addUpdatePlaylist(final Playlist playlist){
        if(playListMap == null){
            playListMap = new HashMap<>();
        }
        if(playListMap.containsKey(playlist.getId())){
            addsongsToPlayList(playlist);
        } else {
            playListMap.put(playlist.getId(), playlist);
        }
    }

    private static void removePlaylist(final String playlistId){
        if(playListMap == null){
            return;
        }
        if(playListMap.containsKey(playlistId)) {
            playListMap.remove(playlistId);
        }
    }

    private static void addsongsToPlayList(final Playlist playlist){
        if(playListMap == null){
            playListMap = new HashMap<>();
        }
          if(playListMap.containsKey(playlist.getId())){
              Playlist oldPlaylist = playListMap.get(playlist.getId());
              for(String songId:playlist.getSong_ids()) {
                  if(!oldPlaylist.getSong_ids().contains(songId)) {
                      oldPlaylist.getSong_ids().add(songId);
                  }
              }
          }
    }
}
