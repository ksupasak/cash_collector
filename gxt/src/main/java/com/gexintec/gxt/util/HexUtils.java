package com.gexintec.gxt.util;

/**
 * Created by soup on 1/7/2018 AD.
 */

public class HexUtils {


    private static char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private HexUtils() { }

    /**
     * Creates a string representation of the supplied byte array in Hexidecimal
     * format
     *
     * @param in the byte array to convert to a hex string.
     * @param start where to begin in the array
     * @param length how many bytes from the array to include in the hexidecimal
     *            representation
     * @return a string containing the hexidecimal representation of the
     *         requested bytes from the array
     */
    public static  String byteArrayToHexString(byte in[], int start, int length) {
        String asHexString = null;
        if (in != null) {
            StringBuilder out = new StringBuilder(in.length * 2);
            for (int x = start; x < length; x++) {
                int nybble = in[x] & 0xF0;
                nybble = nybble >>> 4;
                out.append(hexChars[nybble]);
                out.append(hexChars[in[x] & 0x0F]);
            }
            asHexString = out.toString();
        }
        return asHexString;
    }

    /**
     * Creates a string representation of the supplied byte array in Hexidecimal
     * format
     *
     * @param in the byte array to convert to a hex string.
     * @return a string containing the hexidecimal representation of the array
     */
    public static  String byteArrayToHexString(byte in[]) {
        return byteArrayToHexString(in, 0, in.length);
    }

    /**
     * Convert a hex string into an array of bytes. The string is expected to
     * consist entirely of valid Hex characters i.e. 0123456789abcdefABCDEF. The
     * array is calculated by traversing the string from from left to right,
     * ignoring whitespace. Every 2 valid hex chars will constitute a new byte
     * for the array. If the string is uneven then it the last byte will be
     * padded with a '0'.
     *
     * @param hexString String to be converted
     */
    public static  byte[] hexToByteArray(String hexString) {

        StringBuilder normalize = new StringBuilder(hexString.length());
        for (int x = 0; x < hexString.length(); x++) {
            char current = Character.toLowerCase(hexString.charAt(x));
            if (isHexChar(current)) {
                normalize.append(current);
            } else if (!Character.isWhitespace(current)) {
                throw new IllegalStateException(String.format("Conversion of hex string to array failed. '%c' is not a valid hex character", current));
            }
        }
        // pad with a zero if we have an uneven number of characters.
        if (normalize.length() % 2 > 0) {
            normalize.append('0');
        }
        byte[] hexArray = new byte[hexString.length() + 1 >> 1];
        for (int x = 0; x < hexArray.length; x++) {
            int ni = x << 1;

            int mostSignificantNybble = Character.digit(normalize.charAt(ni), 16);
            int leastSignificantNybble = Character.digit(normalize.charAt(ni + 1), 16);

            int value = ((mostSignificantNybble << 4)) | (leastSignificantNybble & 0x0F);
            hexArray[x] = (byte)value;
        }
        return hexArray;
    }

    public static  boolean isHexChar(char current) {
        return Character.digit(current, 16) >= 0;
    }
}
