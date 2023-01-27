package main.java.Embedding.EMBDI.TripartiteNormalizeDistributeGraph;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetaAlgorithmNormalizeDistribute {
    public List<String> nodes = new ArrayList<>();

    /**
     * @param fileList 文件路径
     * @param n_walks  number of random walks
     * @param n_nodes  number of nodes
     * @param length   embedding的长度
     */
    // todo: 返回值不应该是void，查看Generate中的返回值修改
    public List<List<String>> Meta_Algorithm(List<String> fileList, int n_walks, int n_nodes, int length, int AttrDistributeLow,
                                       int AttrDistributeHigh,
                                       int ValueDistributeLow,
                                       int ValueDistributeHigh,
                                       int TupleDistributeLow,
                                       int TupleDistributeHigh,
                                       int dropSourceEdge,
                                       int dropSampleEdge) throws InterruptedException {
        List<List<String>> walks = new ArrayList<>();
        GenerateNormalizeDistributeSourceTripartite graph = new GenerateNormalizeDistributeSourceTripartite();
        graph = graph.generateSourceTripartiteGraph(fileList, AttrDistributeLow,
                AttrDistributeHigh,
                ValueDistributeLow,
                ValueDistributeHigh,
                TupleDistributeLow,
                TupleDistributeHigh,
                dropSourceEdge,
                dropSampleEdge);
        GenerateNormalizeDistributeSourceTripartiteRandomWalk walkGraph = new GenerateNormalizeDistributeSourceTripartiteRandomWalk(graph);
        nodes = graph.getAll_nodes();
        Set<String> nodeSet = new HashSet<>(nodes);
        for (String str : nodeSet) {
            for (int i = 0; i < n_walks / n_nodes; i++) {
                List<String> list = walkGraph.randomWalk(str, length);
                walks.add(list);
            }

        }

        return walks;
    }

    public List<String> Meta_AlgorithmUseGraphFilePath(String graphFilePath, int n_walks, int n_nodes, int length) {
        List<String> walks = new ArrayList<>();
        FileInputStream fileInputStream = null;
        GenerateNormalizeDistributeSourceTripartite graph = new GenerateNormalizeDistributeSourceTripartite();
        try {
            fileInputStream = new FileInputStream(graphFilePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            graph = (GenerateNormalizeDistributeSourceTripartite) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("graphFile can't be found!");
            e.printStackTrace();
        }
        GenerateNormalizeDistributeSourceTripartiteRandomWalk walkGraph = new GenerateNormalizeDistributeSourceTripartiteRandomWalk(graph);
        nodes = graph.getAll_nodes();
        Set<String> nodeSet = new HashSet<>(nodes);
        for (String str : nodeSet) {
            for (int i = 0; i < n_walks / n_nodes; i++) {
                List<String> list = walkGraph.randomWalk(str, length);
                walks.addAll(list);
            }
        }

        return walks;
    }


}
