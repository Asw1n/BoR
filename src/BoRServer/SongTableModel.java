package BoRServer;


import javax.swing.table.AbstractTableModel;

/** Model to show Song in a table editor
 * @author Aswin
 *
 */
public class SongTableModel extends AbstractTableModel{
  /**
   * 
   */
  private static final long serialVersionUID = -5686596864300068524L;
  Song song;
  
  
  public SongTableModel(Song song) {
      this.song=song;
  }
  
  
  @Override
  public String getColumnName(int col) {
    switch (col) {
      case 0: return "Channel";
      case 1: return "Instrument";
      case 2: return "EV3";
      case 3: return "Lowest tone";
      case 4: return "Highest tone";
      default: return "Error";
    }
}
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int col) {
      switch (col) {
          case 0: return Integer.class;
          case 1: return String.class;
          case 2: return String.class;
          case 3: return Integer.class;
          case 4: return Integer.class;
          default: return String.class;
        }
  }
  
  public boolean isCellEditable(int row, int col) { 
      switch (col) {
          case 2: return true;
          default: return false;
        }
    }
  

  @Override
  public int getColumnCount() {
    return 5;
  }

  @Override
  public int getRowCount() {
      int r=0;
      for (int i=0;i<Channels.CHANNELCOUNT;i++) {
          if (song.channels.hasInstrument(i)) r++;
      }
    return r;
  }
  
  private int getChannelIndex(int row) {
      int r=-1;
      for (int i=0;i<Channels.CHANNELCOUNT;i++) {
          if (song.channels.hasInstrument(i)) r++;
          if (r==row) return i;
      }
    return 0;
  }

  @Override
  public Object getValueAt(int row, int col) {
      int channel=getChannelIndex(row);
    switch (col) {
      case 0:
          return new Integer(channel);
      case 1:
          return song.channels.getInstrumentName(channel);
      case 2:
        return song.channels.getBrickNames(channel);
      case 3:
        return new Integer(song.channels.getLowestNote(channel));
      case 4:
        return new Integer(song.channels.getHighestNote(channel));
      default:
        return "Error";
    }
  }
  
  @Override
  public void setValueAt(Object value, int row, int col) {
  }
  

 
}
