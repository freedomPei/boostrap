package preparedata;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by secret on 16-3-19.
 */
public class prepare {
    public prepare() {

    }

    public HashMap<String,HashSet<wordbag>> preparedata(String fileName,int num) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf8"));
        String data = new String();
        HashSet<String> tagset = new HashSet<>();
        HashMap<String,HashSet<wordbag> > wordlist = new HashMap<String,HashSet<wordbag>>();

        Random r = new Random();
        while((data = reader.readLine())!= null) {
            String[] dataArray = data.split("\t");
            if(dataArray.length>=num) {
                String extern = dataArray[num];
                ArrayList<String> typenum = extractWord(extern);
                wordbag word = new wordbag();
                word.setWordBag(dataArray[0],typenum);
                tagset.addAll(typenum);
                String key = typenum.get(r.nextInt(typenum.size()));
                wordlist = insertWord(wordlist,key,word);
            }
        }
        reader.close();
        return wordlist;
    }

    public void splitData(String outfilePath,HashMap<String,HashSet<wordbag>> totalword,int type) throws IOException {
        if(type == 1) {
            Iterator<Map.Entry<String,HashSet<wordbag>>> it = totalword.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String,HashSet<wordbag> > element = it.next();
                HashSet<wordbag> list = element.getValue();
                int num = list.size()/5;

                Iterator<wordbag> itwordlist = list.iterator();
                int count = 0;
                while(itwordlist.hasNext()) {
                    String trainFile = outfilePath + "/traindata";
                    String devFile = outfilePath + "/devdata";
                    String testFile = outfilePath + "/testdata";
                    BufferedWriter trainwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(trainFile,true),"utf8"));
                    BufferedWriter devwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(devFile,true),"utf8"));
                    BufferedWriter testwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testFile,true),"utf8"));

                    wordbag word = itwordlist.next();
                    ArrayList<String> typelist = word.getTypeList();
                    String result = new String();
                    result = word.getSkuid()+"\t"+element.getKey();
                    for(String type:typelist) {
                        result = result + "\t" + type;
                    }
                    if(count<num*3) {
                        trainwriter.write(result + "\n");
                    } else if(num*3<=count&&count<num*4) {
                        devwriter.write(result + "\n");
                    } else {
                        testwriter.write(result + "\n");
                    }
                    count++;
                }
            }
        }
    }

    public HashMap<String,HashSet<wordbag>> insertWord(HashMap<String,HashSet<wordbag>> map,String key,wordbag word) {
        HashSet<wordbag> list = new HashSet<wordbag>();
        if(map.containsKey(key)) {
            list = map.get(key);
            list.add(word);
        }
        map.put(key,list);
    }

    public ArrayList<String> extractWord(String wordtext) {
        String regex = "";
        ArrayList<String> wordlist = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(wordtext);
        if(m.find()) {
            String wordbuffer = m.group(1);
            String[] dataArray = wordbuffer.split("\t");
            for(String line:dataArray) {
                wordlist.add(line);
            }
        }

        return wordlist;
    }

    public void process(String inFileName,String outFilePath,int colnum) throws IOException {
        HashMap<String,HashSet<wordbag> > result = preparedata(inFileName,colnum);
        splitData(outFilePath,result,1);
    }

    public static void main(String[] args) throws IOException {
        String inFileName = "";
        String outFilePath = "";
        int colnum = 3;
        prepare pre = new prepare();
        pre.process(inFileName,outFilePath,colnum);
    }
}
