/**
 * Timer class to represent all the logic that depends on change in time
 * @author Taiyo Siew-Adachi
 */
public class Timer {
    /** The full duration of the timer */
    private final int duration;

    /** The current count of the timer */
    private int count;

    /**
     * Constructs a new timer with the specified duration
     * @param duration The duration of the timer
     */
    public Timer(int duration) {
        this.duration = duration;
    }

    /**
     * Decrements the count of the timer
     */
    public void countdown() {
        count--;
    }

    /**
     * Restarts the timer by setting the count to the original duration of the timer
     */
    public void restart() {
        count = duration;
    }

    /**
     * Checks if the timer is active
     * @return true if the timer is active, false otherwise
     */
    public boolean isActive() {
        return (count > 0);
    }
}
