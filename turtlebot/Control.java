package turtlebot;

/**
 * Created with IntelliJ IDEA.
 * User: robotics
 * Date: 17/09/14
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */


public class Control {


  //  private Robot robot;

    /**
     * Method     : Control::Control()
     * Purpose    : Secondary Control class constructor that initialises a Robot object.
     * Parameters : robot : An object of Class Robot.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public Control()
    {

    }
     /////////////////////////////////////////////////////////////////////////////////


    /** distance between wheels on the roomba, in millimeters */
    public static final int wheelbase = 258;
    public static final int DRIVE   =  137;  // 4
    public boolean debug = false;
    public OutputStream output;
    public boolean flushOutput = false;
    public static final int defaultSpeed  =  200;
    public static final float
            millimetersPerDegree = (float)(wheelbase * Math.PI / 360.0);
    /** current speed for movement operations that don't take a speed */
    public int speed = defaultSpeed;




    public String hex(byte b) {
        return Integer.toHexString(b&0xff);
    }

    public String hex(int i) {
        return Integer.toHexString(i);
    }

    public void logmsg(String msg) {
        if(debug)
            System.err.println("RoombaComm ("+System.currentTimeMillis()+"):"+
                    msg);
    }

    /**
     * Just a simple pause function.
     * Makes the thread block with Thread.sleep()
     * @param millis number of milliseconds to wait
     */
    public void pause( int millis ) {
        try { Thread.sleep(millis); } catch(Exception e) { }
    }


    public boolean send(byte[] bytes) {
        try {
            output.write(bytes);
            System.out.println("\n output="+output);
            if( flushOutput ) output.flush();   // hmm, not sure if a good idea
        } catch (Exception e) { // null pointer or serial port dead
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This will handle both ints, bytes and chars transparently.
     */
    public boolean send(int b) {  // will also cover char or byte
        try {
            output.write(b & 0xff);  // for good measure do the &
            if( flushOutput ) output.flush();   // hmm, not sure if a good idea
        } catch (Exception e) { // null pointer or serial port dead
            //errorMessage("send", e);
            e.printStackTrace();
        }
        return true;
    }

    //***********************************************************************
    /**
     * Move the Roomba via the low-level velocity + radius method.
     * See the 'Drive' section of the Roomba ROI spec for more details.
     * Low-level command.
     * @param velocity  speed in millimeters/second,
     *                  positive forward, negative backward
     * @param radius    radius of turn in millimeters
     */
    public void drive( int velocity, int radius ) {
        byte cmd[] = { (byte)DRIVE,(byte)(velocity>>>8),(byte)(velocity&0xff),
                (byte)(radius >>> 8), (byte)(radius & 0xff) };

        System.out.print("cmd="+cmd);

        logmsg("drive: "+hex(cmd[0])+","+hex(cmd[1])+","+hex(cmd[2])+","+
                hex(cmd[3])+","+hex(cmd[4]));

        send( cmd );
    }
    //************************************************************************
    /**
     * Go straight at the current speed for a specified distance.
     * Positive distance moves forward, negative distance moves backward.
     * This method blocks until the action is finished.
     * @param distance distance in millimeters, positive or negative
     */
    public void goStraight( int distance ) {
        float pausetime = Math.abs(distance / speed);  // mm/(mm/sec) = sec
        if (distance > 0)
            goStraightAt( speed );
        else
            goStraightAt( -speed);
        pause( (int)(pausetime*1000) );
        stop();
    }
    public void goStraight2( int distance, int speed ) {
        float pausetime = Math.abs(distance / speed);  // mm/(mm/sec) = sec
        if (distance > 0)
            goStraightAt( speed );
        else
            goStraightAt( -speed);
        pause( (int)(pausetime*1000) );
        stop();
    }

    public void goStraightAt( int velocity ) {
        System.out.println("goStraightAt: velocity:"+velocity);
        if( velocity > 500 ) velocity = 500;
        if( velocity < -500 ) velocity = -500;
        drive( velocity, 0x8000 );
    }

    /**
     *
     */
    public void turnLeft() {
        turn(129);
    }
    public void turnRight() {
        turn(-129);
    }
    public void turn( int radius ) {
        drive( speed, radius );
    }

    /**
     * Spin right or spin left a particular number of degrees
     * @param angle angle in degrees,
     *              positive to spin left, negative to spin right
     */
    public void spin( int angle ) {
        if( angle > 0 )       spinLeft( angle );
        else if( angle < 0 )  spinRight( -angle );
    }

    /**
     * Spin right the current speed for a specified angle
     * @param angle angle in degrees, positive
     */
    public void spinRight( int angle ) {
        if( angle < 0 ) return;
        float pausetime = Math.abs( millimetersPerDegree * angle / speed );
        spinRightAt( Math.abs(speed) );
        pause( (int)(pausetime*1000) );
        stop();
    }

    /**
     * Spin left a specified angle at a specified speed
     * @param angle angle in degrees, positive
     */
    public void spinLeft( int angle ) {
        if( angle<0 ) return;
        //float pausetime =
        float pausetime = Math.abs( millimetersPerDegree * angle / speed );
        spinLeftAt( Math.abs(speed) );
        pause( (int)(pausetime*1000) );
        stop();
    }

    /**
     * Spin in place anti-clockwise, at the current speed.
     * @param aspeed speed to spin at
     */
    public void spinLeftAt(int aspeed) {
        drive( aspeed, 1 );
    }

    /**
     * Spin in place clockwise, at the current speed.
     * @param aspeed speed to spin at, positive
     */
    public void spinRightAt(int aspeed) {
        drive( aspeed, -1 );
    }

    /**
     * Spin in place anti-clockwise, at the current speed
     */
    public void spinLeft() {
        spinLeftAt( speed );
    }
    /**
     * Spin in place clockwise, at the current speed
     */
    public void spinRight() {
        spinRightAt( speed );
    }


        //////////////////////////////////////////////////////////////////////////////////
    /**
     * Method     : Control::stop()
     * Purpose    : To stop the robot.
     * Parameters : None
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void stop()
    {
        logmsg("stop");
        drive( 0, 0 );
    }

    /**
     * Method     : Control::move()
     * Purpose    : To .
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void move(double vel)
    {
        goStraightAt( (int) vel );
    }

    /**
     * Method     : Control::turnSpot()
     * Purpose    : To turn on the spot.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void turnSpot(double vel, int radius)
    {
        drive( (int) vel, radius )   ;
    }

    /**
     * Method     : Control::turnSharpLeft() -   turnSharpRight()
     * Purpose    : To turn on one wheel.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void turnSharpLeft(double vel)
    {
        spinLeftAt( (int) vel );
    }
    public void turnSharpRight(double vel)
    {
        spinRightAt( (int) vel );
    }

    /**
     * Method     : Control::turnSmooth()
     * Purpose    : To turn with half speed on one wheel.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void turnSmooth(double vel)
    {
        int distance = 10; //10 mm default distance for each turn
        //first move a bit, then turn a bit
        goStraight2(  distance, (int) vel )  ;
        spinLeftAt((int) vel)   ;

    }

}
