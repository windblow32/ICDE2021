package main.java.Embedding.EMBDI.TripartiteGraphWithSource;


import main.java.Embedding.abstruct_Graph.Graph;
import main.java.Embedding.abstruct_Graph.ConcreteEdgesGraph;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class GenerateSourceTripartite implements Serializable{
    // serialize!!
    private static final long serialVersionUID = 111111;
    private Graph<String> graph = new ConcreteEdgesGraph<>();
    // RID的set
    private Set<String> RID_set = new HashSet<>();
    // column的列表
    private List<String> column_i = new ArrayList<>();
    // 图中所有点nodes
    public List<String> all_nodes = new ArrayList<>();
    // 代理Graph中的方法
    public boolean addVertex(String vertex){
        return graph.add(vertex);
    }
    public int addEdge(String node1, String node2){
        return graph.set(node1, node2, 1);
    }
    public Set<String> vertices(){
        return new HashSet<>(graph.vertices());
    }
    public Map<String,Integer> sources(String target){
        return new HashMap<>(graph.sources(target));
    }
    public Map<String, Integer> targets(String sources){
        return new HashMap<>(graph.targets(sources));
    }

    public GenerateSourceTripartite generateSourceTripartiteGraph(@NotNull List<String> fileList){
        //  修改代码一定注意，对于函数中声明该类对象的函数，当调用成员变量时，必须用该对象引出，否则无法
        //  加入，也就是红色变量必须graph.引出！

        GenerateSourceTripartite sourceGraph = new GenerateSourceTripartite();
        try {
            // 读取第0个文件，用于初始化attribute等
            String file = fileList.get(0);
            FileReader fd = new FileReader(file);
            BufferedReader br = new BufferedReader(fd);
            // 文件操作所需变量
            String strPro = null;
            // 存储str读取的一行的值
            String[] dataPro = null;
            // 首先读取一行，作为属性
            strPro = br.readLine();
            dataPro = strPro.split(",");
            // 存储全部属性
            sourceGraph.column_i.addAll(Arrays.asList(dataPro));
            for(String s : sourceGraph.column_i){
                // store attribute into sourceGraph
                sourceGraph.addVertex(s);
                sourceGraph.all_nodes.add(s);
            }
            fd.close();
            br.close();
            // 按顺序读取文件
            int column;
            // FIXME:读取当前文件,是第i个文件，则有：
            int fileNum = 0;
            // 判断是否是最后一个文件
            int truthFlag = 0;
            for(String files: fileList){
                FileReader fr = new FileReader(files);
                BufferedReader newBr = new BufferedReader(fr);
                // 每个文件是不同的数据源
                String source_id = "source_" + fileNum;
                sourceGraph.addVertex(source_id);
                sourceGraph.all_nodes.add(source_id);
                // 从第二行开始处理
                // process null using avg
                // double[] current_sum = new double[column_i.size()];
                int row_id = 0;
                // 判断是否是最后一个文件，即真值文件，如果是，就进行标记
                if(fileList.get(fileList.size()-1).equals(files)){
                    truthFlag = 1;
                }
                String str = null;
                String[] data = null;
                while((str=newBr.readLine())!=null){
                    // 每一行是不同的tuple和row_id
                    // String tuple = "tuple_" + row_id;
                    // 为每个数据源的表分配row的id
                    column = 0;
                    List<String> row_i = new ArrayList<>();
                    // add node row_id
                    // row_1代表第一个元组
                    // row_1_s1来自第1个数据源的第一行，来自真值
                    // String Ri = "row_" + row_id + "_s" + fileNum;
                    String Ri;
                    if(truthFlag == 0){
                        Ri = "row_" + row_id;
                    }
                    else{
                        Ri = "row_" + row_id + "_s" + fileNum;
                    }
                    // set 互斥
                    sourceGraph.RID_set.add(Ri);

                    sourceGraph.addVertex(Ri);
                    if(!sourceGraph.all_nodes.contains(Ri)){
                        sourceGraph.all_nodes.add(Ri);
                    }
                    row_id++;
                    data = str.split(",");
                    // FIXME:假设表中的数值都是数字类型，现在判断是否为空
                    for(int index = 0;index<data.length;index++){
                        if(data[index]==null){
                            // true
                            // 并且为空
                            // FIXME : null 用当前平均值替代
                            // data[index] = String.valueOf(current_sum[index]/index);
                            data[index] = "0";
//                        if(judge(data[index])){
//                            data[index] = String.valueOf(current_sum[index]/index);
//                        }
                            // fixme : 取消了平均值替代的方式
                            // current_sum[index] += Double.parseDouble(data[index]);
                        }
                    }
                    // row_i暂存每行的数据
                    row_i.addAll(Arrays.asList(data));
                    String[] value = null;
                    List<String> value_i = new ArrayList<>();
                    // 处理多值
                    for(String Vk:row_i ){
                        if(Vk.contains(" ")) {
                            value = Vk.split(" ");
                            // 对于这个属性的成员,每个word储存到value_i中
                            value_i.addAll(Arrays.asList(value));
                            for (String word : value_i) {
                                // G.addNode(word)
                                sourceGraph.addVertex(word);
                                sourceGraph.all_nodes.add(word);
                                // G.addEdge(word,Ri)
                                sourceGraph.addEdge(word, Ri);
                                // G.addEdge(word,Ck)
                                sourceGraph.addEdge(word, sourceGraph.column_i.get(column));
                                sourceGraph.addEdge(word, source_id);
                            }
                        }
                        else{
                            sourceGraph.addVertex(Vk);
                            sourceGraph.all_nodes.add(Vk);
                            // 将数值和属性连接
                            sourceGraph.addEdge(Vk,sourceGraph.column_i.get(column));
                            sourceGraph.addEdge(Vk,source_id);
                            sourceGraph.addEdge(Vk,Ri);
                        }
                        column++;
                    }
                    // 平面图方向，连接了source和row_id
                    // sourceGraph.addEdge(source_id,Ri);
                    // sourceGraph.addEdge(tuple,Ri);
                    // fixme clear
                    row_i.clear();
                    // Thread.sleep(1000);
                }
                newBr.close();
                fr.close();
                fileNum++;
                // Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // fixme clear

        // rename path use dataset name and number of sources
//        String graphPath = "data/stock100/graph/55SourceStockGraph.txt";
//        File f = new File(graphPath);
//        try {
//            f.createNewFile();
//            FileOutputStream outputStream = new FileOutputStream(f);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            objectOutputStream.writeObject(sourceGraph);
//            outputStream.close();
//            System.out.println("Graph is saved");
//        } catch (IOException e) {
//            System.out.println("graph saving encounters error");
//            e.printStackTrace();
//        }
        return sourceGraph;
    }

//    public Set<String> getRID_set(){
//        return new HashSet<>(this.RID_set);
//    }
//    public List<String> getColumn_i(){
//        return new ArrayList<>(this.column_i);
//    }

    /**
     * @return token后的nodes
     */
    public List<String> getAll_nodes(){return new ArrayList<>(this.all_nodes);}


}

