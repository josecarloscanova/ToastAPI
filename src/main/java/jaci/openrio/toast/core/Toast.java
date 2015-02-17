package jaci.openrio.toast.core;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import jaci.openrio.toast.core.loader.RobotLoader;
import jaci.openrio.toast.core.monitoring.power.PDPMonitor;
import jaci.openrio.toast.lib.FRCHooks;
import jaci.openrio.toast.lib.crash.CrashHandler;
import jaci.openrio.toast.lib.log.Logger;

import java.util.Random;

/**
 * The Toast Base Class. This is the base for the Toast API.
 * In order to take advantage of this, change your Robot-Class
 * to this instead of your own in the Manifest.MF file.
 *
 * @author Jaci
 */
public class Toast extends RobotBase {

    private static Logger log;
    private static String[] tastes = new String[] {"Delicious", "Yummy", "Like a buttery heaven", "Needs more salt", "Hot, Hot, HOT!!"};

    private static Toast instance;

    public Toast() {
        super();
        instance = this;
    }

    public static Toast getToast() {
        return instance;
    }

    public DriverStation station() {
        return m_ds;
    }

    /**
     * Get the default logger for the Toast API. This logger uses
     * ATTR_DEFAULT, including Date, Time and current Thread.
     */
    public static Logger log() {
        if (log == null) log = new Logger("Toast", Logger.ATTR_DEFAULT);
        return log;
    }

    /**
     * Yum
     */
    public static String getRandomTaste() {
        return tastes[new Random().nextInt(tastes.length)];
    }

    /**
     * Executed when WPILib detects this class and loads it. This
     * is used for Pre-Initialization Tasks.
     */
    @Override
    protected void prestart() {
        try {
            Thread.currentThread().setName("Pre-Initialization");
            log().info("Buttering Bread...");

            CrashHandler.init();
            RobotLoader.init();

            RobotLoader.prestart();
            FRCHooks.robotReady();
        } catch (Exception e) {
            CrashHandler.handle(e);
        }
    }

    /**
     * The robot is setup and ready to go. Let's rock.
     */
    @Override
    public void startCompetition() {
        try {
            Thread.currentThread().setName("Initialization");
            log().info("Fabricating Sandwich...");
            PDPMonitor.init();
            StateTracker.addTicker(new ToastStateManager());

            Thread.currentThread().setName("Main");
            log().info("Verdict: " + getRandomTaste());
            RobotLoader.start();
            StateTracker.init(this);
        } catch (Exception e) {
            CrashHandler.handle(e);
        }
    }

    public void shutdownSafely() {
        log().info("Robot Shutting Down...");
        System.exit(1);
    }

}
