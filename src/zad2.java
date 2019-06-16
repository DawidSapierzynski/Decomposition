import Jama.Matrix;
import java.util.Random;
import static java.lang.Math.abs;

//Rozwiązanie układu równań z wykorzystaniem dekompozycji LU
public class zad2 {
    public static void main(String[] args) {
        int n = 4, precyzja = 4;
        Random random = new Random();
        Matrix a = new Matrix(n, n);
        Matrix u, l = Matrix.identity(n, n);
        Matrix y, x;
        Matrix b = new Matrix(n, 1);
        Matrix[] m = new Matrix[a.getColumnDimension() - 1];
        Matrix[] pc = new Matrix[a.getColumnDimension() - 1];

        //Zapełnienie macierzy B
        for (int i = 0; i < n; i++) {
            b.set(i, 0, i);
        }

        //Zapełnienie macierzy A
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                do {
                    a.set(i, j, (random.nextInt(20) / 20.0));
                } while (a.get(i, j) == 0);
            }
        }

        System.out.print("A:");
        a.print(6, precyzja);
        System.out.print("B:");
        b.print(6, precyzja);

        u = a.copy();

        for (int i = 0; i < a.getColumnDimension() - 1; i++) {
            pc[i] = macierzPC(u, i);
            pc[i].print(6, precyzja);
            u = u.times(pc[i]);
            m[i] = macierzM(u, i);
            m[i].print(6, precyzja);
            u = m[i].times(u);
            u.print(6, precyzja);
        }

        System.out.print("U:");
        u.print(6, precyzja);

        for (int i = 0; i < m.length; i++) {
            l = l.times(m[i]);
        }

        l = Matrix.identity(n, n).plus(Matrix.identity(n, n).minus(l));
        System.out.print("L:");
        l.print(6, precyzja);

        System.out.print("LU:");
        l.times(u).print(6, precyzja);
        System.out.println("Sprawdzenie: norma F z roznicy LU i APC: " + l.times(u).minus(macierzIloczyn(a, pc)).normF());

        y = l.inverse().times(b);
        x = u.inverse().times(y);

        System.out.print("X:");
        x.print(6, precyzja);
    }

    private static Matrix macierzIloczyn(Matrix a, Matrix[] pc) {
        Matrix nowa = a.copy();
        for (int i=0; i<pc.length; i++){
            nowa = nowa.times(pc[i]);
        }
        return nowa;
    }

    private static Matrix macierzPC(Matrix u, int i) {
        Matrix wektor = Matrix.identity(u.getRowDimension(), u.getColumnDimension());
        int max = i;

        for (int j = i; j < u.getColumnDimension(); j++) {
            if (abs(u.get(i, j)) > abs(u.get(i, max))) {
                max = j;
            }
        }
        if (max != i) {
            wektor.set(i, i, 0);
            wektor.set(i, max, 1);
            wektor.set(max, i, 1);
            wektor.set(max, max, 0);
        }

        return wektor;
    }


    public static Matrix macierzM(Matrix a, int kolumna) {
        Matrix wektor = new Matrix(a.getRowDimension(), a.getColumnDimension());

        for (int i = kolumna + 1; i < a.getRowDimension(); i++) {
            wektor.set(i, kolumna, a.get(i, kolumna) / a.get(kolumna, kolumna));
        }
        wektor = Matrix.identity(a.getRowDimension(), a.getColumnDimension()).minus(wektor);
        return wektor;
    }
}
