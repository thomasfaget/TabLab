package tablab;

import java.math.BigInteger;

class Fraction {
    private BigInteger numerator;
    private BigInteger denominator;

    Fraction(long numerator, long denominator) {
        if(denominator == 0) {
            throw new IllegalArgumentException("denominator is zero");
        }
        if(denominator < 0) {
            numerator *= -1;
            denominator *= -1;
        }
        this.numerator = BigInteger.valueOf(numerator);
        this.denominator = BigInteger.valueOf(denominator);
        simplify();
    }

    public long getNumerator() {
        return numerator.intValue();
    }

    public int getDenominator() {
        return denominator.intValue();
    }

    public double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public boolean equal(Fraction fraction) {
        long t = numerator.longValue() * fraction.denominator.longValue();
        long f = fraction.numerator.longValue() * denominator.longValue();
        return t==f;
    }

    public void multiply(Fraction fraction) {
        this.numerator = numerator.multiply(fraction.numerator);
        this.denominator = denominator.multiply(fraction.denominator);
        simplify();
    }

    public void add(Fraction fraction) {
        this.numerator = denominator.multiply(fraction.numerator).add(numerator.multiply(fraction.denominator));
        this.denominator = denominator.multiply(fraction.denominator);
        simplify();
    }

    private void simplify() {
        if (numerator.intValue() != 0) {
            BigInteger gcd = numerator.gcd(denominator);
            numerator = numerator.divide(gcd);
            denominator = denominator.divide(gcd);
        }
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}