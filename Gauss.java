import java.util.Scanner;

class NormFraction {
    private int x, y;

    public NormFraction(int n) {
        x = n;
        y = 1;
    }
  
    public NormFraction(NormFraction b){
        this.x=b.x;
        this.y=b.y;
    }

    private int lcm(int x, int y) {
        return x / gcd(x, y) * y;
    }

    private int gcd(int x, int y) {
        if (y == 0)
            return x;
        else
            return gcd(y, x % y);
    }

    private void norming(NormFraction b){
        int del=gcd(Math.abs(this.x),Math.abs(this.y));
        if(del!=0) {
            this.x /= del;
            this.y /= del;
        }

    }
    
    public boolean isNull(){
        return this.x==0;
    }

    public boolean compare(NormFraction c){
        double a=this.x/this.y;
        double b=c.x/c.y;

        return Math.abs(a)<Math.abs(b);
    }

    public void add(NormFraction b) {
        int del = lcm(this.y, b.y);

        this.x *= del / this.y;
        b.x *= del / b.y;

        this.x += b.x;
        this.y = del;

        norming(this);
    }

    public void subtract(NormFraction b) {
        if(this.x==0){
            this.x=-b.x;
            this.y=b.y;
        }
        else if(b.x==0){
            return;
        }else {
            int del = lcm(this.y, b.y);
            this.x *= del / this.y;
            b.x *= del / b.y;
            this.x -= b.x;
            this.y = del;
            norming(this);
        }
    }

    public void multiply(NormFraction b) {
        this.x *= b.x;
        this.y *= b.y;
        norming(this);
    }

    public void divide(NormFraction b){
        this.x*=b.y;
        this.y*=b.x;

        norming(this);
        if(this.y==0) this.y=1;
        if(this.y<0){
            this.x=-this.x;
            this.y=-this.y;
        }
    }

    public String toString() {
        return x + "/" + y;
    }
}

class Matrix{
    private int size;
    private NormFraction[][] m;
    private boolean solution=true;

    public Matrix(int n,int[][] a){
        this.size=n;
        m=init_matrix(n,a);
    }

    private NormFraction[][] init_matrix(int n,int[][] a){
        NormFraction b[][]=new NormFraction[n][n+1];

        for(int i=0;i<n;i++){
            boolean bool=false;
            for(int j=0;j<n+1;j++){
                b[i][j]=new NormFraction(a[i][j]);
                if(!b[i][j].isNull()) bool=true;
            }
            if(!bool) solution=false;
        }
        return b;
    }

    public void toTriangle(){
        for(int i=0;i<size-1&&solution;i++){
            sortStr(i);
            if(m[i][i].isNull()) {
                int max = findNotNullStr(i);
                if(max==i){
                    solution=false;
                    break;
                }
                swap(i, max);
            }
            
            for(int k=i;k<size;k++){//нормирование
                NormFraction del=new NormFraction(m[k][i]);//находим делитель левого элемента ,чтобы elem/del==1
                
                for(int e=i;e<size+1&&(!del.isNull());e++)//делим все элементы строки на этот делитель
                    m[k][e].divide(del);
            }
            for(int k=i+1;(k<size)&&solution;k++){
               if(!m[k][i].isNull()) strMatrixSubtract(i,k);
            }
        }

    }

    private int findNotNullStr(int i){
        for(int k=i+1;k< size;k++){
            if(!m[k][i].isNull())
                return k;
        }
        return i;
    }

    private void sortStr(int i){
        for(int k=i;k<size-1;k++){
            if(m[k][i].compare(m[k+1][i])){
                swap(k,k+1);
            }
        }
    }

    private void strMatrixSubtract(int i,int k){
        boolean zero_str=true;
        //поочередно вычитаем из элемента данной строки элемент ведущей строки
        for(int j=i;j<size+1;j++){
            NormFraction buf=new NormFraction(m[i][j]);
            m[k][j].subtract(buf);
            if(!m[k][j].isNull()) zero_str=false;
        }
        if(zero_str) this.solution=false;
    }

    private void swap(int a,int b){
        for(int i=0;i<size+1;i++){
            NormFraction buf=new NormFraction(m[a][i]);
            m[a][i]=m[b][i];
            m[b][i]=buf;
        }
    }

    public String getAnswer(){
        if(!solution)
            return "No solution";
        
        NormFraction[] ans=new NormFraction[size];

        for(int i=size-1;i>=0;i--){//цикл для получения ответов
            NormFraction sub_sum=new NormFraction(0);
            for(int k=size-1;k>i;k--){
                NormFraction buf=new NormFraction(ans[k]);
                buf.multiply(m[i][k]);
                sub_sum.add(buf);
            }
            if(sub_sum.isNull()&&m[i][i].isNull()&&(!m[i][size].isNull()))
                return "No solution";
            
                m[i][size].subtract(sub_sum);
                ans[i]=m[i][size];
                ans[i].divide(m[i][i]);
        }
        String str_ans="";

        for(int i=0;i<size;i++){
            str_ans=str_ans+ans[i]+'\n';
        }
        return str_ans;

    }
}

public class Gauss{
    public static void main(String[] args ){
        Scanner in=new Scanner(System.in);

        int n=in.nextInt();
        int arr[][]=new int[n][n+1];

        for(int i=0;i<n;i++){
            for(int j=0;j<n+1;j++)
                arr[i][j]=in.nextInt();
        }
        Matrix matrix=new Matrix(n,arr);
        matrix.toTriangle();

        System.out.print(matrix.getAnswer());
    }
}
