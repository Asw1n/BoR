package org.aswinmp.lejos.ev3.bandofrobots.utils;

 class Fingering {
  int string;
  int fret;

  public String toString() {
      return "String : " + stringNames[string] + ", fret : " + fret;
  }


static String[] stringNames = new String[] {"Low E", "A", "D", "G", "B", "High E"}; 

  /** Array showing guitar string's relative pitches, in semi-tones, with "0" being low E */
static int[] strings = new int[]{64, 69, 74, 79, 83, 88};



public static Fingering getIdealFingering(int note) {
          if (note < strings[0])
              throw new RuntimeException("Note " + note + " is not playable on a guitar in standard tuning.");

  Fingering result = new Fingering();

  int idealString = 0;
  for (int x = 1; x < strings.length; x++) {
      if (note < strings[x])
          break;
      idealString = x; 
  }

  result.string = idealString;
  result.fret = note - strings[idealString];

  return result;
}

public static void main(String[] args) {
  System.out.println(getIdealFingering(64));  // Low E
  System.out.println(getIdealFingering(66));  // F#
  System.out.println(getIdealFingering(72));  // C on A string
  System.out.println(getIdealFingering(76));  // E on D string
  System.out.println(getIdealFingering(88));  // guitar's high e string, open
  System.out.println(getIdealFingering(100)); // high E, 12th fret
  System.out.println(getIdealFingering(103)); // high G
}
}