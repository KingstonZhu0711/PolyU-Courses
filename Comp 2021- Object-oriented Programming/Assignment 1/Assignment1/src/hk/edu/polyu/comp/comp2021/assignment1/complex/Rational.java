package hk.edu.polyu.comp.comp2021.assignment1.complex;

public class Rational {
    // Todo: add the missing fields
    private int numerator;
    private int denominator;

    public Rational(int numerator, int denominator) {
        // Todo: complete the constructor
        this.numerator=numerator;
        this.denominator=denominator;
    }

    public Rational add(Rational other) {
        // Todo: complete the method
        int numresult=this.numerator*other.denominator+other.numerator*this.denominator;
        int denresult=this.denominator*other.denominator;

        return new Rational(numresult,denresult);
    }

    public Rational subtract(Rational other) {
        // Todo: complete the method
        int numresult=this.numerator*other.denominator-other.numerator*this.denominator;
        int denresult=this.denominator*other.denominator;

        return new Rational(numresult,denresult);

    }

    public Rational multiply(Rational other) {
        // Todo: complete the method
        int numresult=this.numerator*other.numerator;
        int denresult=this.denominator*other.denominator;
        return new Rational(numresult,denresult);
    }

    public Rational divide(Rational other) {
        // Todo: complete the method
        int numresult=this.numerator*other.denominator;
        int denresult=this.denominator*other.numerator;
        return new Rational(numresult,denresult);
    }

    public String toString() {
        // Todo: complete the method

            return numerator + "/" + denominator;
        }


    public void simplify() {
        // Todo: complete the method
        if (numerator == 0) {
            this.denominator=1;
            return ;
        }
            int gcd = computeGCD(Math.abs(this.numerator), Math.abs(this.denominator));
            this.numerator /= gcd;
            this.denominator /= gcd;
        }

    private int computeGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    // ==================================

}
