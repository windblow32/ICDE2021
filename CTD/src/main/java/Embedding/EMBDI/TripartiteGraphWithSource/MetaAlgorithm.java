package main.java.Embedding.EMBDI.TripartiteGraphWithSource;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MetaAlgorithm {
    public List<String> nodes = new ArrayList<>();

    /**
     * @param fileList    文件路径
     * @param n_walks number of random walks
     * @param n_nodes number of nodes
     * @param length  embedding的长度
     */
    // todo: 返回值不应该是void，查看Generate中的返回值修改
    public List<List<String>> Meta_Algorithm(List<String> fileList, int n_walks, int n_nodes, int length) throws InterruptedException {
        List<List<String>> walks = new ArrayList<>();
        GenerateSourceTripartite graph = new GenerateSourceTripartite();
        graph = graph.generateSourceTripartiteGraph(fileList);
        GenerateSourceTripartiteRandomWalk walkGraph = new GenerateSourceTripartiteRandomWalk(graph);
        nodes = graph.getAll_nodes();
        for (String str : nodes) {
            for (int i = 0; i < n_walks / n_nodes; i++) {
                List<String> list = walkGraph.randomWalk(str, length);
//                StringBuilder sentence = null;
//                for (int index = 0; index < length; index++) {
//                    sentence = (sentence == null ? new StringBuilder("null") : sentence).append(list.get(index));
//                }
//
//                walks.add(sentence == null ? null : sentence.toString());
                walks.add(list);
            }
            // Thread.sleep(200);
        }
//        Word2Vec method = new Word2Vec();
//         method.generateEmbedding(walks);
        return walks;
    }

    public List<String> Meta_AlgorithmUseGraphFilePath(String graphFilePath, int n_walks, int n_nodes, int length){
        List<String> walks = new ArrayList<>();
        FileInputStream fileInputStream = null;
        GenerateSourceTripartite graph = new GenerateSourceTripartite();
        try {
            fileInputStream = new FileInputStream(graphFilePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            graph = (GenerateSourceTripartite)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("graphFile can't be found!");
            e.printStackTrace();
        }
        GenerateSourceTripartiteRandomWalk walkGraph = new GenerateSourceTripartiteRandomWalk(graph);
        nodes = graph.getAll_nodes();
        for (String str : nodes) {
            for (int i = 0; i < n_walks / n_nodes; i++) {
                List<String> list = walkGraph.randomWalk(str, length);
//                StringBuilder sentence = null;
//                for (int index = 0; index < length; index++) {
//                    sentence = (sentence == null ? new StringBuilder("null") : sentence).append(list.get(index));
//                }
//
//                walks.add(sentence == null ? null : sentence.toString());
                walks.addAll(list);
            }
            // Thread.sleep(200);
        }
//        Word2Vec method = new Word2Vec();
//         method.generateEmbedding(walks);
        return walks;
    }


}
