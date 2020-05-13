package ru.skillbench.tasks.basics.math;

import java.util.Arrays;

import static java.lang.Math.pow;

public class ComplexNumberImpl implements ComplexNumber {
    private Double re;
    private Double im;

    public ComplexNumberImpl(Double re, Double im) {
        this.re = re;
        this.im = im;
    }

    public ComplexNumberImpl(ComplexNumber complexNumber) {
        this.re = complexNumber.getRe();
        this.im = complexNumber.getIm();
    }

    public ComplexNumberImpl() {
        this.re = 0.;
        this.im = 0.;
    }

    @Override
    public double getRe() {
        return re;
    }

    @Override
    public double getIm() {
        return im;
    }

    @Override
    public boolean isReal() {
        if (im != 0)
            return false;
        else
            return true;
    }

    @Override
    public void set(double re, double im) {
        this.re = re;
        this.im = im;
    }
    /**
     * Parses the given string value and sets the real and imaginary parts of this number accordingly.<br/>
     * The string format is "re+imi", where re and im are numbers (floating point or integer) and 'i' is a special symbol
     *  denoting imaginary part (if present, it's always the last character in the string).<br/>
     * Both '+' and '-' symbols can be the first characters of re and im; but '*' symbol is NOT allowed.<br/>
     * Correct examples: "-5+2i", "1+i", "+4-i", "i", "-3i", "3". Incorrect examples: "1+2*i", "2+2", "j".<br/>
     * Note: explicit exception generation is an OPTIONAL requirement for this task,
     *   but NumberFormatException can be thrown by {@link Double#parseDouble(String)}).<br/>
     * Note: it is not reasonable to use regex while implementing this method: the parsing logic is too complicated.
     * @param value
     * @throws NumberFormatException if the given string value is incorrect
     */
    @Override
    public void set(String value) throws NumberFormatException {
        String tempRe = "0";
        String tempIm = "0";

        if (value.isEmpty()) {
            throw new NumberFormatException();
        }

        int lastIndexPlus = value.lastIndexOf('+');
        int lastIndexMinus = value.lastIndexOf('-');
        int lastSignIndex = Math.max(lastIndexPlus, lastIndexMinus);

        if (value.indexOf('i') < 0) {
            if (lastSignIndex > 0) {
                throw new NumberFormatException();
            }
            else {
                tempRe = value;
            }
        }
        else {
            if (value.compareTo("i") == 0) {
                tempIm = "1";
            }
            else if (lastSignIndex > 0) {
                tempRe = value.substring(0, lastSignIndex);
                tempIm = value.substring(lastSignIndex, value.length() - 1);
            }
            else {
                tempIm = value.substring(0, value.length() - 1);
            }
        }

        if (tempRe.compareTo("+") == 0) {
            tempRe = "1";
        }
        else if (tempRe.compareTo("-") == 0) {
            tempRe = "-1";
        }

        if (tempIm.compareTo("+") == 0) {
            tempIm = "1";
        }
        else if (tempIm.compareTo("-") == 0) {
            tempIm = "-1";
        }

        this.re = Double.parseDouble(tempRe);
        this.im = Double.parseDouble(tempIm);
    }

    @Override
    public ComplexNumber copy() {
        ComplexNumber object = new ComplexNumberImpl();
        object.set(re, im);
        return object;
    }

    @Override
    public ComplexNumber clone() throws CloneNotSupportedException {
        ComplexNumber object = new ComplexNumberImpl();
        object.set(re, im);
        return object;
    }

    @Override
    public String toString(){
        if ((re == 0) && (im == 0)){
            return "0.0";
        }
        StringBuilder builder = new StringBuilder();
        if (re != 0) {
            builder.append(re);
            if (im > 0){
                builder.append("+");
            }
        }
        if (im != 0) {
            builder.append(im + "i");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (other instanceof ComplexNumber)
            return ((ComplexNumber) other).getRe() == re && ((ComplexNumber) other).getIm() == im;
        return false;
    }
    /**
     * Compares this number with the other number by the absolute values of the numbers:
     * x < y if and only if |x| < |y| where |x| denotes absolute value (modulus) of x.<br/>
     * Can also compare the square of the absolute value which is defined as the sum
     * of the real part squared and the imaginary part squared: |re+imi|^2 = re^2 + im^2.
     * @see Comparable#compareTo(Object)
     * @param other the object to be compared with this object.
     * @return a negative integer, zero, or a positive integer as this object
     *   is less than, equal to, or greater than the given object.
     */
    @Override
    public int compareTo(ComplexNumber other) {
        return Double.compare(pow(this.re, 2) + pow(this.im, 2), pow(other.getRe(), 2) + pow(other.getIm(), 2));
    }
    /**
     * Sorts the given array in ascending order according to the comparison rule defined in
     *   {@link #compareTo(ComplexNumber)}.<br/>
     * It's strongly recommended to use {@link Arrays} utility class here
     *   (and do not transform the given array to a double[] array).<br/>
     * Note: this method could be static: it does not use this instance of the ComplexNumber.
     *    Nevertheless, it is not static because static methods can't be overridden.
     * @param array an array to sort
     */
    @Override
    public void sort(ComplexNumber[] array) {
        Arrays.sort(array);
    }
    /**
     * Changes the sign of this number. Both real and imaginary parts change their sign here.
     * @return this number (the result of negation)
     */
    @Override
    public ComplexNumber negate() {
        this.re = this.re*-1;
        this.im = this.im*-1;
        return this;
    }
    /**
     * Adds the given complex number arg2 to this number. Both real and imaginary parts are added.
     * @param arg2 the second operand of the operation
     * @return this number (the sum)
     */
    @Override
    public ComplexNumber add(ComplexNumber arg2) {
        this.re += arg2.getRe();
        this.im += arg2.getIm();
        return  this;
    }

    /**
     * Multiplies this number by the given complex number arg2. If this number is a+bi and arg2 is c+di then
     * the result of their multiplication is (a*c-b*d)+(b*c+a*d)i<br/>
     * The method should work correctly even if arg2==this.
     * @param arg2 the second operand of the operation
     * @return this number (the result of multiplication)
     */
    @Override
    public ComplexNumber multiply(ComplexNumber arg2) {
        double real = this.re * arg2.getRe() - this.im * arg2.getIm();
        this.im = this.re * arg2.getIm() + this.im * arg2.getRe();
        this.re = real;
        return this;
    }

    public static void main(String[] args) {
        ComplexNumber complexNumber = new ComplexNumberImpl(1.,1.);
        System.out.println(complexNumber);
    }
}
