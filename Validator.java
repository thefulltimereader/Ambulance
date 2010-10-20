import java.io.File;
import java.io.FileNotFoundException;
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
      input.close();
      if(args.length == 1) input = new Scanner(System.in);
      else input = new Scanner(new File(args[1]));
    } catch (FileNotFoundException e) {
      System.err.println("Input file DNE");
      e.printStackTrace();
    }
    //validate!
    v.validate(input);


  }
  /**
   * the core method. Reads result line by line and validate it.
   */
  private void validate(Scanner in){
    String line = in.nextLine();
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
  }

  private class Ambulance{
    int hosX;
    int hosY;
    Person[] carrying;
    Ambulance(){
      carrying = new Person[4];
    }
    void setHospitalLocation(int x, int y){
      hosX = x;
      hosY = y;
    }
  }


}