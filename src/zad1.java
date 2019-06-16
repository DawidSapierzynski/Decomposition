import Jama.Matrix;

import java.util.Random;

import static java.lang.Math.sqrt;

//Rozkład macierzy z wykorzystaniem dekompozycji(rozkładu) choleskiego
public class zad1 {
    public static void main(String[] args) {
        int n = 5;
        Random random = new Random();
        Matrix a = new Matrix(n, n);
        Matrix s;
        Matrix l;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                do {
                    a.set(i, j, random.nextInt(199) - 100);
                } while (a.get(i, j) == 0);
            }
        }
        a=a.transpose().times(a);
        s=symetryczna(a);
        l=choleski(s);

        System.out.print("A:");
        a.print(6, 3);
        System.out.print("S:");
        s.print(6, 3);
        System.out.print("L:");
        if(l!=null){
            l.print(6, 3);
            System.out.println("Sprawdzenie:");

            System.out.println(l.times(l.transpose()).minus(s).normF());

        }
        else {
            System.out.println("Nie można utworzyć macierzy, ponieważ nie istnieje pierwiastek stopina 2 z liczby ujemnej");
        }

    }

    public static Matrix choleski(Matrix a){
        Matrix l = new Matrix(a.getRowDimension(), a.getColumnDimension());
        double b, c, d;

        if(a.get(0,0)<0){
            return null;
        }
        l.set(0,0,sqrt(a.get(0,0)));

        for(int i=0; i<a.getColumnDimension(); i++){
            for (int j=0; j<=i; j++){
                if(i==j){
                    b = a.get(j, j);
                    c=0.0;
                    for(int k=0; k<j; k++){
                        c = c + l.get(j,k)*l.get(j,k);
                    }
                    if(b-c<0){
                        return null;
                    }
                    l.set(j,j, sqrt(b-c));

                }else{
                    b = a.get(i, j);
                    c=0.0;
                    for(int k=0; k<=(j-1); k++){
                        c = c + l.get(i,k)*l.get(j,k);
                    }
                    d=0.0;
                    d=b-c;
                    if(d==0){
                        return null;
                    }
                    l.set(i,j, d/l.get(j,j));
                }
            }
        }

        return l;

    }

    public static Matrix symetryczna(Matrix a){
        Matrix b;

        b=a.plus(a.transpose());
        b=b.times(1/2.0);
        return b;
    }


}
