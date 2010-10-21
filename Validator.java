import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

final class Validator{
  private final int MAX_PASSENGER = 4;//of an amb 
  private List<Person> injured;
  private List<Integer> hospitals;
  private List<Ambulance> ambulances;
  private int numHospitals = 0;
  private int totalRescued = 0;

  public static void main(String[] args){
    Validator v = new Validator();
    Scanner input = null;
    try {
      input = new Scanner(new File(args[0]));
      v.buildInput(input);
      if(args.length == 1) input = new Scanner(System.in);
      else input = new Scanner(new File(args[1]));
    } catch (FileNotFoundException e) {
      System.err.println("Input file DNE");
      //e.printStackTrace();
      System.exit(1);
    }
    ///validate!
    try {
      v.validate(input);
    } catch (Exception e) {
      
      e.printStackTrace();
    }
  }
  /**
   * the core method. Reads result line by line and validate it.
   */
  private void validate(Scanner in) throws Exception{
    //String line = in.nextLine();
 //   in = in.skip("(,)");
    while(in.hasNextLine()){
      String[] line = in.nextLine().split(" ");//line[0] = Ambulance
      //for(String s : line)System.out.println(s);
      int ambulanceID = Integer.parseInt(line[1]);
      String locs = line[2];
      checkCoordinateInput(locs); //line[2] = (x,y)
      int x = getX(locs); int y = getY(locs);
      Ambulance amb = ambulances.get(ambulanceID);
      System.out.println("looking at Amb "+ ambulanceID+" at ("+x+","+y+")");
      checkHospitalLocation(x,y,amb);
      if(line.length>3){ //pick up
        int i = 3;
        while (i < line.length){
          int pId = Integer.parseInt(line[i]);
          Person rescuing = injured.get(pId);
          int px = getX(line[++i]); int py = getY(line[++i]);
          checkStatusOfInjured(px,py,rescuing);
          updateAmbulancePickup(rescuing, amb);
        }
      }
      else{//drop off
        updateAmbulanceDropOff(x,y, amb);
      }
    }
  }
  /**
   * Drops off everyone in the ambulance to x, y hospital
   * @param x
   * @param y
   */
  private void updateAmbulanceDropOff(int x, int y, Ambulance amb) {
    int timeSpent = 1;
    timeSpent += getManhattanDistance(amb.getCurrX(), amb.getCurrY(), x, y);
    amb.addTime(timeSpent);
    int rescued = amb.dropOff();
    totalRescued += rescued;
    
    
  }
  /**
   * Updates the time remaining for people in the ambulance and 
   * the ambulance itself
   * This calculates the time required to get to the person and load him/her
   * @param dist
   * @param amb
   * @throws exception if the ambulance is full
   */
  private void updateAmbulancePickup(Person p, Ambulance amb) {
    if(amb.getNumOfPassengers()==4)
      throw new IllegalStateException("This ambulance is full");
    amb.addRescued(p);
    int timeTaken = getManhattanDistance(amb.getCurrX(), amb.getCurrY(),p.getX(), p.getY());
    timeTaken++; //for loading p
    amb.addTime(timeTaken);
    amb.setNewLocation(p.getX(), p.getY());
  }
  /**
   * Checks if the input is valid/whether the person has been rescued or not
   * @param px
   * @param py
   * @param rescuing
   * @throws Exception
   */
  private void checkStatusOfInjured(int px, int py, Person rescuing) throws Exception {
    if(rescuing.x!=px || rescuing.y!=py) 
            throw new IllegalArgumentException("The location of the person "+
                                              rescuing.getId()+" is not the same as input");
    if (rescuing.isRescued()) 
            throw new AlreadyBoundException("Already Rescued");
    
  }
  private void checkCoordinateInput(String locs) {
    if(!locs.matches("[(]{1}\\d{0,4}[,]{1}\\d{0,4}[)]{1}")) 
        throw new IllegalArgumentException();
  }
  private void checkHospitalLocation(int x, int y, Ambulance amb) {
    //if hostpial location not set, set it
      if(amb.getHospitalX()==-1) amb.setHospitalLocation(x, y);
      else if(amb.getHospitalX()!=x || amb.getHospitalY()!=y){
        throw new IllegalArgumentException("The location of the hospital once" +
        		" set cannot change!");
      }
    
  }
  private int getX(String locs){
    return Integer.parseInt(locs.split("\\(")[1].split(",")[0]);
  }
  private int getY(String locs){
    return Integer.parseInt(locs.split("\\(")[1].split(",")[1].split("\\)")[0]);
  }
  private Validator(){
    injured = new ArrayList<Person>();
    hospitals = new ArrayList<Integer>();
    ambulances = new ArrayList<Ambulance>();
  }

  private void buildInput(Scanner in){
    in.nextLine();//let the first line go
    int pId=0, hosId = 0;
    while(in.hasNextLine()){
      String[] ints = in.nextLine().split(",");
      if(ints.length==3){
        Person p = new Person(pId, Integer.parseInt(ints[0]), 
            Integer.parseInt(ints[1]), Integer.parseInt(ints[2]));
        //  System.out.println(p.toString());
        injured.add(p);  
        pId++;
      } else if (ints[0].length() == 1) {
        for (int i = numHospitals,len = Integer.parseInt(ints[0])+numHospitals;
        i < len; i++) {
          ambulances.add(new Ambulance(hosId));
          hosId++;
        }
        numHospitals++;
      }
    }
  }

  private class Person{
    final int id;
    final int x;
    final int y;
    final int time;
    boolean rescued = false;
    boolean dead = false;
    Person(int id, int x, int y, int time){
      this.id = id;
      this.x = x;
      this.y = y;
      this.time = time;
    }
    boolean isAlive(int now){
      return time<now; 
    }
    public String toString(){
      return "I am at ("+x+","+y+") with time:" + time;
    }
    int getId(){
      return id;
    }
    
    int getX() {
      return x;
    }
    int getY() {
      return y;
    }
    int getTime() {
      return time;
    }
    void rescue(){
      rescued = true;
    }
    boolean isRescued(){
      return rescued;
    }
    void kill(){ dead = true;}
    boolean isDead(){ return dead;}
    
  }
  //sum of horizontal + vertical
  int getManhattanDistance(int xOld, int yOld, int xNew, int yNew){
    return Math.abs(xOld - xNew) + Math.abs(yOld - yNew);
  }

  private class Ambulance{
    int hosX = -1, hosY = -1;
    int id, time, currX, currY;
    ArrayList<Person> carrying;
    Ambulance(int id){
      this.id = id;
      carrying = new ArrayList<Person>();
      time = 0;
    }
    
    int getId(){
      return id;
    }
    void setHospitalLocation(int x, int y){
      currX = hosX = x;
      currY = hosY = y;
    }
    void setNewLocation(int x, int y){
      currX = x;
      currY = y;
    }
    /**
     * Update time for this amb and for all the people in it
     * @param t
     */
    void addTime(int t){
      if(t>0){
        time += t;
        if(time > 160) 
          throw new IllegalStateException("2min time over for ambulance "+ id);
        for(Person p: carrying){
          if(!p.isAlive(time)){
            System.out.println("Person:"+p.getId()+" has died in ambulance"
                + id);
            p.kill();
          }
        }
      }
    }
    /**
     * Clears the ambulance
     * @return the number of people alive when dropped off
     */
    public int dropOff() {
      int count = 0;
      for(Person p: carrying){
        if(p.isAlive(time)) count++;
      }
      carrying.clear();
      return count;
    }
    void addRescued(Person p){
      carrying.add(p);
    }
    int getNumOfPassengers(){
      return carrying.size();
    }
    
    int getHospitalX(){
      return hosX;
    }
    int getHospitalY(){
      return hosY;
    }
    int getCurrX() {
      return currX;
    }
    int getCurrY() {
      return currY;
    }
    
  }


}