package turtlebot;

/**
 * Created with IntelliJ IDEA.
 * User: robotics
 * Date: 17/09/14
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class Sensor {

    public boolean debug = false;
    /** internal storage for all roomba sensor data */
    byte[] sensor_bytes = new byte[1024];

    public static final int VIRTUALWALL         = 6;
    public static final int MOTOROVERCURRENTS   = 7;
    public static final int DIRTLEFT            = 8;
    public static final int DIRTRIGHT           = 9;
    public static final int REMOTEOPCODE        = 10;
    public static final int BUTTONS             = 11;
    public static final int DISTANCE_HI         = 12;
    public static final int DISTANCE_LO         = 13;
    public static final int ANGLE_HI            = 14;
    public static final int ANGLE_LO            = 15;
    public static final int CHARGINGSTATE       = 16;
    public static final int VOLTAGE_HI          = 17;
    public static final int VOLTAGE_LO          = 18;
    public static final int CURRENT_HI          = 19;
    public static final int CURRENT_LO          = 20;
    public static final int TEMPERATURE         = 21;
    public static final int CHARGE_HI           = 22;
    public static final int CHARGE_LO           = 23;
    public static final int CAPACITY_HI         = 24;
    public static final int CAPACITY_LO         = 25;
    public static final int BUMPSWHEELDROPS     = 0;
    // bitmasks for various thingems
    public static final int WHEELDROP_MASK      = 0x1C;
    public static final int BUMP_MASK           = 0x03;
    public static final int BUMPRIGHT_MASK      = 0x01;
    public static final int BUMPLEFT_MASK       = 0x02;
    public static final int WHEELDROPRIGHT_MASK = 0x04;
    public static final int WHEELDROPLEFT_MASK  = 0x08;
    public static final int WHEELDROPCENT_MASK  = 0x10;

    public static final int WALL                = 1;
    public static final int CLIFFLEFT           = 2;
    public static final int CLIFFFRONTLEFT      = 3;
    public static final int CLIFFFRONTRIGHT     = 4;
    public static final int CLIFFRIGHT          = 5;


    public short distance() {
        return toShort(sensor_bytes[DISTANCE_HI],
                sensor_bytes[DISTANCE_LO]);
    }

    static public final short toShort(byte hi, byte lo) {
        return (short)((hi << 8) | (lo & 0xff));
    }

    static public final int toUnsignedShort(byte hi, byte lo) {
        return (int)(hi & 0xff) << 8 | lo & 0xff;
    }

    /** lower-level func, returns raw byte */
    public int virtual_wall() {
        return sensor_bytes[VIRTUALWALL];
    }

    public String hex(byte b) {
        return Integer.toHexString(b&0xff);
    }

    /** lower-level func, returns raw byte */
    public int motor_overcurrents() {
        return sensor_bytes[MOTOROVERCURRENTS];
    }
    /**  */
    public int dirt_left() {
        return sensor_bytes[DIRTLEFT] & 0xff;
    }
    /** */
    public int dirt_right() {
        return sensor_bytes[DIRTRIGHT] & 0xff;
    }
    /** lower-level func, returns raw byte */
    public int remote_opcode() {
        return sensor_bytes[REMOTEOPCODE];
    }
    /** lower-level func, returns raw byte */
    public int buttons() {
        return sensor_bytes[BUTTONS];

    }

     public short angle() {
        return toShort(sensor_bytes[ANGLE_HI],
                sensor_bytes[ANGLE_LO]);
    }

    /**
     * Charging state
     * units: enumeration
     * range:
     */
    public int charging_state() {
        return sensor_bytes[CHARGINGSTATE] & 0xff;
    }
    /**
     * Voltage of battery
     * units: mV
     * range: 0 - 65535
     */
    public int voltage() {
        return toUnsignedShort(sensor_bytes[VOLTAGE_HI],
                sensor_bytes[VOLTAGE_LO]);
    }
    /**
     * Current flowing in or out of battery
     * units: mA
     * range: -332768 - 32767
     */
    public short current() {
        return toShort(sensor_bytes[CURRENT_HI],
                sensor_bytes[CURRENT_LO]);
    }
    /**
     * temperature of battery
     * units: degrees Celcius
     * range: -128 - 127
     */
    public byte temperature() {
        return sensor_bytes[TEMPERATURE];
    }
    /**
     * Current charge of battery
     * units: mAh
     * range: 0-65535
     */
    public int charge() {
        return toUnsignedShort(sensor_bytes[CHARGE_HI],
                sensor_bytes[CHARGE_LO]);
    }
    /**
     * Estimated charge capacity of battery
     * units: mAh
     * range: 0-65535
     */
    public int capacity() {
        return toUnsignedShort(sensor_bytes[CAPACITY_HI],
                sensor_bytes[CAPACITY_LO]);
    }



    //*********************************************************************************************
    public String sensorsAsString() {
        String sd="";
        if( debug ) {
            sd = "\n";
            for( int i=0; i<26; i++ )
                sd += " "+hex(sensor_bytes[i]);
        }
        return
                "bump:" +
                        (bumpLeft()?"l":"_") +
                        (bumpRight()?"r":"_") +
                        " wheel:" +
                        (wheelDropLeft()  ?"l":"_") +
                        (wheelDropCenter()?"c":"_") +
                        (wheelDropLeft()  ?"r":"_") +
                        " wall:" + (wall() ?"Y":"n") +
                        " cliff:" +
                        (cliffLeft()       ?"l":"_") +
                        (cliffFrontLeft()  ?"L":"_") +
                        (cliffFrontRight() ?"R":"_") +
                        (cliffRight()      ?"r":"_") +
                        " dirtL:"+dirtLeft()+
                        " dirtR:"+dirtRight()+
                        " vwal:" + virtual_wall() +
                        " motr:" + motor_overcurrents() +
                        " dirt:" + dirt_left() + "," + dirt_right() +
                        " remo:" + hex((byte)remote_opcode()) +
                        " butt:" + hex((byte)buttons()) +
                        " dist:" + distance() +
                        " angl:" + angle() +
                        " chst:" + charging_state() +
                        " volt:" + voltage() +
                        " curr:" + current() +
                        " temp:" + temperature() +
                        " chrg:" + charge() +
                        " capa:" + capacity() +
                        sd;
    }
     //**********************************************************************************************
    /** Did we bump into anything */
    public boolean bump() {
        return (sensor_bytes[BUMPSWHEELDROPS] & BUMP_MASK) !=0;
    }
    /** Left bump sensor */
    public boolean bumpLeft() {
        return (sensor_bytes[BUMPSWHEELDROPS] & BUMPLEFT_MASK) !=0;
    }
    /** Right bump sensor */
    public boolean bumpRight() {
        return (sensor_bytes[BUMPSWHEELDROPS] & BUMPRIGHT_MASK) !=0;
    }
    /** Left wheeldrop sensor */
    public boolean wheelDropLeft() {
        return (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROPLEFT_MASK) !=0;
    }
    /** Right wheeldrop sensor */
    public boolean wheelDropRight() {
        return (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROPRIGHT_MASK) !=0;
    }
    /** Center wheeldrop sensor */
    public boolean wheelDropCenter() {
        return (sensor_bytes[BUMPSWHEELDROPS] & WHEELDROPCENT_MASK) !=0;
    }
    /** Can we see a wall? */
    public boolean wall() {
        return sensor_bytes[WALL] != 0;
    }

    /**
     * @return true if dirt present
     */
    public boolean dirt() {
        int dl = sensor_bytes[DIRTLEFT] & 0xff;
        int dr = sensor_bytes[DIRTRIGHT] & 0xff;
        //if(debug) println("Roomba:dirt: dl,dr="+dl+","+dr);
        return (dl > 100) || (dr > 100);
    }
    /**
     * amount of dirt seen by left dirt sensor
     */
    public int dirtLeft() {
        return dirt_left();  // yeah yeah
    }
    /**
     * amount of dirt seen by right dirt sensor
     */
    public int dirtRight() {
        return dirt_right();
    }

    /** left cliff sensor */
    public boolean cliffLeft() {
        return (sensor_bytes[CLIFFLEFT] != 0);
    }
    /** front left cliff sensor */
    public boolean cliffFrontLeft() {
        return (sensor_bytes[CLIFFFRONTLEFT] != 0);
    }
    /** front right cliff sensor */
    public boolean cliffFrontRight() {
        return (sensor_bytes[CLIFFFRONTRIGHT] != 0);
    }
    /** right cliff sensor */
    public boolean cliffRight() {
        return sensor_bytes[CLIFFRIGHT] != 0;
    }


}
