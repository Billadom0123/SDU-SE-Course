
public class TwoOne {
	public static void main(String[] args) {
	    for(int i = 0; i < 100; i++) {
	      if(i == 72) 
				break; //Out of for loop
	      if(i % 9 != 0) 
				continue; // Next iteration
	      System.out.println(i);
	    }
	  }
}
