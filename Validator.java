import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

final class Validator{
  private List<Person> injured;
  private List<Integer> hospitals;
  private Map<Integer, Ambulance> ambulances;
  private int numHospitals = 0;

  public static void main(String args[]){
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
      if(!locs.matches("[(]{1}\\d{0,4}[,]{1}\\d{0,4}[)]{1}")) throw new IllegalArgumentException();
      int x = getX(locs);
      int y = getY(locs);
      System.out.println("look at Amb "+ ambulanceID+" at ("+x+","+y+")");
      if(line.length>3){ //pick up
        int i = 3;
        while (i < line.length){
          int pId = Integer.parseInt(line[i]);
          Person rescuing = injured.get(pId);
          int px = getX(line[++i]); int py = getY(line[++i]);
          if(rescuing.x!=px || rescuing.y!=py) throw new AlreadyBoundException("Already Rescued");
          
         
        }
      }
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
    ambulances = new HashMap<Integer, Ambulance>();
  }

  private void buildInput(Scanner in){
    in.nextLine();//let the first line go
    while(in.hasNextLine()){
      String[] ints = in.nextLine().split(",");
      if(ints.length==3){
        Person p = new Person(Integer.parseInt(ints[0]), 
            Integer.parseInt(ints[1]), Integer.parseInt(ints[2]));
        //  System.out.println(p.toString());
        injured.add(p);  
      } else if (ints[0].length() == 1) {
        for (int i = numHospitals,len = Integer.parseInt(ints[0])+numHospitals;
        i < len; i++) {
          ambulances.put(i, new Ambulance());
        }
        numHospitals++;
      }
    }
  }

  private class Person{
    final int x;
    final int y;
    final int time;
    boolean rescued = false;
    Person(int x, int y, int time){
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
    void rescue(){
      rescued = true;
    }
  }

  private class Ambulance{
    int hosX;
    int hosY;
    ArrayList<Person> carrying;
    int time;
    Ambulance(){
      carrying = new ArrayList<Person>();
      time = 0;
    }
    void setHospitalLocation(int x, int y){
      hosX = x;
      hosY = y;
    }
    void setTime(int t){
      time = t;
    }
    void addRescued(Person p){
      carrying.add(p);
    }
  }


}