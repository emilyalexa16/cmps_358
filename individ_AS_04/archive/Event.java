public class Event {
    private int car_id;
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

    public Event(int car_id, int type) {
        this.car_id = car_id;
        this.type = type;
        this.leftImage = new int[10][10];
    }

    public int getCarID() {
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
        return this.sumArray(leftImage);
    }

    public int getRightProximity() {
        return this.sumArray(rightImage);
    }

    public long getTimestamp() {
        return System.nanoTime();
    }

    public int sumArray(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; i++) {
                sum += arr[i][j];
            }
        }
        return sum;
    }

}