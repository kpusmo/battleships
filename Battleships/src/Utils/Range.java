package Utils;

public class Range {
    public static int[] Range(int stop) {
        return Range(0, stop, 1);
    }

    public static int[] Range(int start, int stop) {
        return Range(start, stop, 1);
    }

    public static int[] Range(int start, int stop, int step) {
        int length = (int) Math.ceil((Math.abs(stop - start) + 1) / Math.abs(step));
        int range[] = new int[length];
        for (int i = 0; i < length; ++i) {
            range[i] = start;
            start += step;
        }
        return range;
    }
}
