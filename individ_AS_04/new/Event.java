import java.io.Serializable;
import java.util.Random;

public class Event implements Serializable {

    private static final long serialVersionUID = -2308613611325993159L;
    private String car_id;
    private int type;
    private int[][] leftImage;
    private int[][] rightImage;

    // EVENT TYPES
    public static final int START = 1;
    public static final int STOP = 2;
    public static final int RESUME = 3;
    public static final int ON_LEFT = 4;
    public static final int ON_RIGHT = 5;
    public static final int GONE_LEFT = 6;
    public static final int GONE_RIGHT = 7;

    public Event(String car_id, int type) {
        this.car_id = car_id;
        this.type = type;
        this.leftImage = new int[10][10];
        this.rightImage = new int[10][10];
        populateArray(leftImage);
        populateArray(rightImage);
    }

    public String getCarID() {
        return this.car_id;
    }

    public int getType() {
        return this.type;
    }

    public int[][] getLeftImage() {
        return this.leftImage;
    }

    public int[][] getRightImage() {
        return this.rightImage;
    }

    public int getLeftProximity() {
        return this.sumOfArray(leftImage);
    }

    public int getRightProximity() {
        return this.sumOfArray(rightImage);
    }

    private void populateArray(int[][] arr) {
        Random rand = new Random();
        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[row].length; col++) {
                double randomNumber = rand.nextDouble();
                if (randomNumber < 0.3) {
                    arr[row][col] = 0; // 30% probability
                } else {
                    arr[row][col] = 1; // 70% probability
                }
            }
        }
    }

    private int sumOfArray(int[][] arr) {
        int sum = 0;
        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[row].length; col++) {
                sum += arr[row][col];
            }
        }
        return sum;
    }

}
