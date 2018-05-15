import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Scanner;

public class Econom {
    public static void main(String[] args){
        HashSet<String> col=new HashSet<String>();
        Scanner in=new Scanner(System.in);

        char[]s=in.nextLine().toCharArray();
        int len=s.length;

        for(int i=0;i<len;i++){
            if(s[i]=='('){
                i++;
                int k=i;
                int c=1;
               
                while(c>0) {
                    if(s[k]==')'){
                        if(c==1) break;
                        else c--;
                    }
                    if(s[k]=='(') c++;
                    k++;
                }
                String v= new String(s,i,k-i);
                col.add(v);
            }
        }
        System.out.println(col.size());
    }
}

