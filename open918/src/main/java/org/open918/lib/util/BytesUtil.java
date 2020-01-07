package org.open918.lib.util;

import java.util.BitSet;

/**
 * Created by joelhaasnoot on 22/11/2016.
 */
public class BytesUtil {

    private BitSet bitSetToDecode;

    /**
     * BytesDecoder constructor
     * Init the BitSet used for decoding
     * @param bytes the byte array to decode
     */
    public BytesUtil(byte[] bytes) {
        // Translate bytes array as binary
        String frameBinary ="";
        for (byte b : bytes) {
            frameBinary  += toBinary(b,8);
        }

        // Init the BitSet with an array of reversed bytes to handle the little endian representation expected
        bitSetToDecode = BitSet.valueOf(reverse(bytes));
    }

    /**
     * Decode a part of the byte array between the startIndex and the endIndex
     * @param startIndex index of the first bit to include
     * @param endIndex index after the last bit to include
     * @return the int value of the decoded bits
     */
    public int decode(int startIndex, int endIndex) {
        int length = endIndex - startIndex;

        int decodedInt = convert(bitSetToDecode.get(startIndex, endIndex), length);

        return decodedInt;
    }

    public int decodeLength(int startIndex, int length) {
        int decodedInt = convert(bitSetToDecode.get(startIndex, startIndex+length), length);

        return decodedInt;
    }

    public String decodeString(int start, int numberOfCharacters) {
        char[] str = new char[numberOfCharacters];
        for (int i = 0; i < numberOfCharacters; i += 1) {
            str[i] = AsciiSixBits.translate(decode(start + (i*6), start + (i*6) + 6));
        }
        return new String(str);
    }

    /**
     * Reverse bit order of each byte of the array
     * @param data the bytes array
     * @return the bytes array with bit order reversed for each byte
     */
    private byte[] reverse(byte [] data) {
        byte [] bytes = data.clone();

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (Integer.reverse(bytes[i]) >>> 24);
        }

        return bytes;
    }

    /**
     * Get the binary form of an int
     * @param num the int number
     * @param length the bit length
     * @return the string value of the binary form of the int
     */
    private String toBinary(int num, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(((num & 1) == 1) ? '1' : '0');
            num >>= 1;
        }

        return sb.reverse().toString();
    }

    /**
     * Convert a BitSet into an int
     * @param bits the BitSet
     * @param length the BitSet theorical lenght
     * @return the int value corresponding
     */
    private int convert(BitSet bits, int length) {
        int value = 0;
        // Set the increment to the difference between the therocial length and the effective lenght of the bitset
        // to take into account the fact that the BitSet just represent significative bits
        // (i.e instead of 110, the bitset while contains 11 since the 0 is irrelevant in his representation)
        int increment = length - bits.length();

        // Browse the BitSet from the end to the begining to handle the little endian representation
        for (int i = bits.length() - 1; i >= 0; --i) {
            value += bits.get(i) ? (1L << increment) : 0L;
            increment++;
        }

        return value;
    }
}