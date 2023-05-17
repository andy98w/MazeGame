package byow.Core.Input;
import byow.InputDemo.InputSource;

public class StringInputDevice {
    private int i;
    private String input;
    public StringInputDevice(String string) {
        input = string;
        i = 0;
    }
    public boolean possibleNextInput() {
        return i < input.length();
    }
    public char getNextKey() {
        char ans = input.charAt(i);
        i += 1;
        return ans;
    }
}
