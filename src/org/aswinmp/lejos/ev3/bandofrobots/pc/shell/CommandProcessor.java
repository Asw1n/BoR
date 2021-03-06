package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.AssignMusicianCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.AssignSingerCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.DumpBricksCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.DumpChannelsCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.PlayCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.QuitCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.SelectSongCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.ShellCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.ShellExecute;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.StartPlaylistCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.StopCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.StopPlaylistCommand;

/**
 * An input process for the interactive BoR shell.
 * @author mpscholz
 */
public class CommandProcessor {

  private final SortedMap<String, Object> commandMap = new TreeMap<String, Object>();
  private final CommandContext commandContext = new CommandContext();

  public CommandProcessor() throws MidiUnavailableException {
    // populate commands map
    populateCommandsMap();
  }

  /**
   * Processes the user input.
   * @param input
   *          the user input
   * @return true in case the user signaled "quit", else false
   */
  public boolean process(final String input) throws InvocationTargetException,
      IllegalAccessException {
    boolean result = false;
    // split input into command label and params
    final String[] inputParts = input.split(" ");
    final String commandLabel = inputParts[0];
    final String[] parameters = new String[inputParts.length - 1];
    System.arraycopy(inputParts, 1, parameters, 0, inputParts.length - 1);
    // retrieve matching command
    final Object command = commandMap.get(commandLabel.trim());
    if (command == null) {
      // show help
      displayHelp();
    } else {
      // is this the quit command?
      if (command instanceof QuitCommand) {
        result = true;
      }
      // execute command
      invokeShellExecutes(command, parameters);
    }
    return result;

  }

  private void displayHelp() {
    System.out.println("Available commands:");
    for (final Object command : commandMap.values()) {
      final ShellCommand shellCommand = command.getClass()
          .getAnnotation(ShellCommand.class);
      System.out
          .println(String.format("%s %s [%s]", shellCommand.label(), shellCommand
              .parameters(), shellCommand.description()));
    }
  }

  private void invokeShellExecutes(final Object shellCommand, final Object[] parameters)
      throws InvocationTargetException, IllegalAccessException {
    final Method[] methods = shellCommand.getClass().getMethods();
    for (final Method method : methods) {
      final ShellExecute shellExecute = method
          .getAnnotation(ShellExecute.class);
      if (shellExecute != null) {
        try {
          method.invoke(shellCommand, parameters);
        } catch (final Exception exc) {
          exc.printStackTrace();
        }
      }
    }
  }

  private void populateCommandsMap() {
    // TODO populate command map dynamically by reflection on commands
    // package
    final QuitCommand quitCommand = new QuitCommand(commandContext);
    commandMap.put(quitCommand.getClass().getAnnotation(ShellCommand.class)
        .label(), quitCommand);
    final SelectSongCommand selectSongCommand = new SelectSongCommand(commandContext);
    commandMap.put(selectSongCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), selectSongCommand);
    final DumpChannelsCommand dumpChannelsCommand = new DumpChannelsCommand(commandContext);
    commandMap.put(dumpChannelsCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), dumpChannelsCommand);
    final PlayCommand playCommand = new PlayCommand(commandContext);
    commandMap.put(playCommand.getClass().getAnnotation(ShellCommand.class)
        .label(), playCommand);
    final StopCommand stopCommand = new StopCommand(commandContext);
    commandMap.put(stopCommand.getClass().getAnnotation(ShellCommand.class)
        .label(), stopCommand);
    final DumpBricksCommand dumpBricksCommand = new DumpBricksCommand(commandContext);
    commandMap.put(dumpBricksCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), dumpBricksCommand);
    final AssignMusicianCommand assignMusicianCommand = new AssignMusicianCommand(commandContext);
    commandMap.put(assignMusicianCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), assignMusicianCommand);
    final AssignSingerCommand assignSingerCommand = new AssignSingerCommand(commandContext);
    commandMap.put(assignSingerCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), assignSingerCommand);
    final StartPlaylistCommand startPlaylistCommand = new StartPlaylistCommand(commandContext);
    commandMap.put(startPlaylistCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), startPlaylistCommand);
    final StopPlaylistCommand stopPlaylistCommand = new StopPlaylistCommand(commandContext);
    commandMap.put(stopPlaylistCommand.getClass()
        .getAnnotation(ShellCommand.class).label(), stopPlaylistCommand);
  }

}
