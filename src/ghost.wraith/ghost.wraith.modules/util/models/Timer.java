package ghost.wraith.util.models;

public class Timer {
    private long lastTime = System.nanoTime();

    public boolean passedSeconds(double seconds) {
        return elapsedMs() >= (long) (seconds * 1000.0);
    }

    public boolean passedMinutes(double minutes) {
        return elapsedMs() >= (long) (minutes * 60_000.0);
    }

    public boolean passedDeciseconds(double deciSeconds) {
        return elapsedMs() >= (long) (deciSeconds * 100.0);
    }

    public boolean passedMs(long ms) {
        return elapsedMs() >= ms;
    }

    public boolean passedNs(long ns) {
        return (System.nanoTime() - lastTime) >= ns;
    }

    public void setMs(long ms) {
        lastTime = System.nanoTime() - ms * 1_000_000L;
    }

    public long elapsedMs() {
        return (System.nanoTime() - lastTime) / 1_000_000L;
    }

    public void reset() {
        lastTime = System.nanoTime();
    }
}
