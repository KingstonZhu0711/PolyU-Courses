package hk.edu.polyu.comp.comp2021.assignment1.complex;

public class Complex {

    private Rational real;
    private Rational imag;


    public Complex(Rational real, Rational imag) {
        // Todo: complete the constructor
        this.real=real;
        this.imag=imag;
    }

    public Complex add(Complex other) {
        // Todo: complete the method

        Rational Realresult = real.add(other.real);
        Rational Imaginaryresult = imag.add(other.imag);
        return new Complex(Realresult, Imaginaryresult);
    }

    public Complex subtract(Complex other) {
        // Todo: complete the method
        Rational Realresult = real.subtract(other.real);
        Rational Imaginaryresult = imag.subtract(other.imag);
        return new Complex(Realresult, Imaginaryresult);

    }

    public Complex multiply(Complex other) {
        // Todo: complete the method
        Rational Realresult = real.multiply(other.real).subtract(imag.multiply(other.imag));
        Rational Imaginaryresult = real.multiply(other.imag).add(imag.multiply(other.real));
        return new Complex(Realresult, Imaginaryresult);
    }

    public Complex divide(Complex other) {
        // Todo: complete the method
        // you may assume 'other' is never equal to '0+/-0i'

        Rational Realresult = real.multiply(other.real).add(imag.multiply(other.imag)).divide(other.real.multiply(other.real).add(other.imag.multiply(other.imag)));
        Rational Imaginaryresult = imag.multiply(other.real).subtract(real.multiply(other.imag)).divide(other.real.multiply(other.real).add(other.imag.multiply(other.imag)));
        return new Complex(Realresult, Imaginaryresult);
    }

    public void simplify() {
        // Todo: complete the method
        real.simplify();
        imag.simplify();
    }

    public String toString() {
        // Todo: complete the method
        String real=this.real.toString();
        String imag=this.imag.toString();
        return "(" + real + "," + imag + ")";
    }

    // ==================================

}
