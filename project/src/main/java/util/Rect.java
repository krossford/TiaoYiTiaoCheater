package util;

/**
 * Created by krosshuang on 2018/3/12.
 */
public class Rect {

    public int left;
    public int right;
    public int top;
    public int bottom;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("l:").append(left);
        sb.append(" r:").append(right);
        sb.append(" t:").append(top);
        sb.append(" b:").append(bottom);

        return sb.toString();
    }

    public int width() {
        return right - left + 1;
    }

    public int height() {
        return bottom - top + 1;
    }
}
