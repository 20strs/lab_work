import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

class Pair {
    String relativePath;
    String absolutePath;

    public Pair(String x, String y) {
        relativePath = x;
        absolutePath = y;
    }
}

class FileSync {
    private File file1, file2;
    private ArrayList<String> delete, copy;
    private ArrayList<Pair> f1, f2;

    public FileSync(String x, String y) {
        file1 = new File(x);
        file2 = new File(y);
        f1 = findFile(file1);
        f2 = findFile(file2);
        delete = new ArrayList<>();
        copy = new ArrayList<>();
    }

    private ArrayList<Pair> findFile(File file) {
        ArrayList<Pair> out = new ArrayList<>();
        if (file.canRead()) {
            for (File x : file.listFiles()) {
                findFileRec(x, out, "");
            }
        }
        return out;
    }

    private void findFileRec(File file, ArrayList<Pair> OUT, String catalogName) {
        if (file.isDirectory()) {
            for (File x : file.listFiles()) {
                findFileRec(x, OUT, catalogName + file.getName() + "/");
            }
        } else {
            Pair pair = new Pair(catalogName + file.getName(), file.getAbsolutePath());
            OUT.add(pair);
        }
    }

    private boolean filesEquals(String path1,String path2) throws IOException {
        File x1=new File(path1);
        File x2=new File(path2);

        if(x1.length()==x2.length()) {
            String a = new String(Files.readAllBytes(Paths.get(path1)));
            String b = new String(Files.readAllBytes(Paths.get(path2)));
            return a.equals(b);
        }
        return false;
    }

    private void getChanges(ArrayList<Pair> a, ArrayList<Pair> b, ArrayList<String> out) throws IOException {
        for (Pair pair1 : a) {
            boolean needToAdd = true;
            for (Pair pair2 : b) {
                if (pair1.relativePath.equals(pair2.relativePath)) {
                    if (filesEquals(pair1.absolutePath,pair2.absolutePath)) {
                        needToAdd = false;
                        break;
                    } else {
                        out.add(pair1.relativePath);
                        needToAdd = false;
                        break;
                    }
                }
            }
            if (needToAdd) out.add(pair1.relativePath);
        }
    }

    private void getSync() throws IOException {
        getChanges(f2, f1, delete);
        getChanges(f1, f2, copy);
    }

    public void report() throws IOException {
        getSync();
        Collections.sort(delete);
        Collections.sort(copy);
        for (String s : delete)
            System.out.println("DELETE " + s);

        for (String s : copy)
            System.out.println("COPY " + s);

        if(delete.size()==0&&copy.size()==0) System.out.println("IDENTICAL");
    }
}

public class Sync  {
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            FileSync sync = new FileSync(args[0], args[1]);
            sync.report();
        } else System.out.println("Usage: argv[0](first directory) argv[v](second directory)");
    }
}
