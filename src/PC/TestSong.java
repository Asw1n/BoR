package PC;

import java.io.File;

public class TestSong {
  Song mySong = new Song();
  
  public static void main(String[] args) {
    TestSong foobar = new TestSong();
    foobar.mySong.setSong(new File("MIDI/blues.mid"));
    foobar.mySong.dump();
  }

}
