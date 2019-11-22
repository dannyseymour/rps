package edu.cnm.deepdive.res.model;

import java.util.Arrays;
import java.util.Random;

public class Arena {
  private final byte numBreeds;
  private final int arenaSize;
  private final Random rng;
  private final byte[][] terrain;
  private final Object lock = new Object();

  private Arena(byte numBreeds, int arenaSize, Random rng) {
    this.numBreeds = numBreeds;
    this.arenaSize = arenaSize;
    this.rng = rng;
    terrain = new byte[arenaSize][arenaSize];
  }
  public void init(){
    for (int row = 0; row<arenaSize; row++){
      for (int column = 0; column<arenaSize; column++){
        terrain[row][column] = (byte) rng.nextInt(numBreeds);
      }
    }
  }

  public void advance(int numIterations){
    for (int i = 0; i<numIterations; i++){

      synchronized (lock) {
        int challengerRow = rng.nextInt(arenaSize);
        int challengerCol = rng.nextInt(arenaSize);
        byte challenger = terrain[challengerRow][challengerCol];
        Direction direction = Direction.random(rng);
        int defenderRow = wrap(challengerRow+ direction.getRowOffset());
        int defenderColumn = wrap(challengerCol+direction.getColumnOffset());
        byte defender = terrain[defenderRow][defenderColumn];
        int comparison = compare(challenger, defender);
        if (comparison>0){
          terrain[defenderRow][defenderColumn] = challenger;
        }else if (comparison<0){
          terrain[challengerRow][challengerCol] = defender;
        }
      }
    }
  }

  public byte getNumBreeds() {
    return numBreeds;
  }

  public byte[][] getTerrain(){
    byte[][] safeCopy = new byte[arenaSize][];
    synchronized (lock) {
      for (int row = 0; row<arenaSize; row++){
        safeCopy[row] = Arrays.copyOf(terrain[row], arenaSize);
      }
    }
    return safeCopy;
  }
  private int wrap(int value){
    value %= arenaSize;
    return (value>=0) ? value: value+arenaSize;
  }

  private int compare (byte cell1, byte cell2){
    int comparison;
    if (cell1==cell2){
      comparison = 0;
    }else {
      int distanceClockwise = (cell2-cell1 + numBreeds) % numBreeds;
      int distanceCounterClockwise = (cell1-cell2 + numBreeds) % numBreeds;
      comparison = (distanceClockwise > distanceCounterClockwise) ? 1 : -1;
    }
    return comparison;
  }
  private enum Direction{
    NORTH(-1,0),
    SOUTH(1,0),
    EAST(0,1),
    WEST(0,-1);

    private final int rowOffset;
    private final int columnOffset;

    Direction(int rowOffset, int columnOffset) {
      this.rowOffset = rowOffset;
      this.columnOffset = columnOffset;
    }
    public int getRowOffset() {
      return rowOffset;
    }

    public int getColumnOffset() {
      return columnOffset;
    }
    private static Direction random(Random rng){
      Direction[] allDirections = Direction.values();
      return allDirections[rng.nextInt(allDirections.length)];
    }
  }
  public static class Builder{
    public static final byte DEFAULT_NUM_BREEDS = 3;
    public static final int DEFAULT_ARENA_SIZE = 50;

    private byte numBreeds = DEFAULT_NUM_BREEDS;
    private int arenaSize = DEFAULT_ARENA_SIZE;
    private Random rng;

    public Builder setNumBreeds(byte numBreeds) {
      this.numBreeds = numBreeds;
      return this;
    }

    public Builder setArenaSize(int arenaSize) {
      this.arenaSize = arenaSize;
      return this;
    }

    public Builder setRng(Random rng) {
      this.rng = rng;
      return this;
    }
    public Arena build(){
      return new Arena(numBreeds, arenaSize, (rng!= null)? rng: new Random());
    }
  }

}
