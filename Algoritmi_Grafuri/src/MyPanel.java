import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MyPanel extends JPanel {
    private boolean isOriented = true;
    private Boolean isQuasiConnected = false;
    private boolean isDoubleClicked = false;
    private boolean isOrientedChanged = false;

    private boolean StronglyConnectedDrawingGraph = false;
    File fisier = new File("matriceAdiacenta.txt");
    private int nodeNr = 1;
    private int node_diam = 30;
    private Vector<Node> listaNoduri = new Vector();
    private Vector<Arc>listaArceComponenteConexe = new Vector<>();
    private Vector<Node> StronglyConnectedGraph = new Vector<>();
    private Vector<Arc> listaArce = new Vector();
    private Integer indexRadacina;
    Point pointStart = null;
    Point pointEnd = null;

    Point selectedPoint = null;

    Node selectedPointOfNode;
    private boolean select = false;
    private boolean findConnectedComponents = false;
    private boolean findRadacina = false;
    boolean isDragging = false;
    boolean isTopological = false;

    private JButton ArborePartialMinim;
    private JButton orientedButton;
    private JButton unorientedButton;
    private JButton topologicalOrder;

    private JButton connectedComponents;

    private JButton determinareRadacina;
    private JButton ComponenteTareConexe;
    private JButton GenericAlgorithm;
    private HashMap<Integer, Color> componentColors = new HashMap<>();

    private Node movedNode = null;
    private Node selectedNode ;

    public JPanel butttonsPanel = new JPanel();

    private boolean isRightClicked = false;
    public void setMatrix(File file) {
        int[][] adjacencyMatrix = generateAdjacencyMatrix(isOriented);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Integer.toString(listaNoduri.size()) + "\n");
            for (int i = 0; i < listaNoduri.size(); i++) {
                for (int j = 0; j < listaNoduri.size(); j++) {
                    writer.write(Integer.toString(adjacencyMatrix[i][j]) + "");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][] generateAdjacencyMatrix(boolean isOriented) {
        int numNodes = listaNoduri.size();
        int[][] adjacencyMatrix = new int[numNodes][numNodes];

        for (int i = 0; i < numNodes; i++) {
            Arrays.fill(adjacencyMatrix[i], 0);
        }

        for (Arc arc : listaArce) {
            int startIndex = arc.getStartNode();
            int endIndex = arc.getEndNode();

            if (isOriented) {
                adjacencyMatrix[startIndex][endIndex] = 1;
            } else {
                adjacencyMatrix[startIndex][endIndex] = 1;
                adjacencyMatrix[endIndex][startIndex] = 1; // Adăugăm și pentru graf neorientat
            }
        }

        return adjacencyMatrix;
    }



    public MyPanel() {
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Scanner scanner = new Scanner(System.in);
        // Creare buton pentru graf orientat
        orientedButton = new JButton("Orientat");
        orientedButton.addActionListener(e -> {
            setOriented(true);
        });

        // Creare buton pentru graf neorientat
        unorientedButton = new JButton("Neorientat");
        unorientedButton.addActionListener(e -> {
            setOriented(false);
        });

        topologicalOrder = new JButton("TopologicalOrder");
        topologicalOrder.addActionListener(e -> {
            DFS(listaNoduri);
        });

        connectedComponents = new JButton("ConnectedComponents");
        connectedComponents.addActionListener(e -> {
            DeterminareComponenteConexe(listaNoduri);
        });

        determinareRadacina = new JButton("Radacina");
        determinareRadacina.addActionListener(e -> {
            determinareRadacina(listaNoduri);
        });

        ComponenteTareConexe = new JButton("ComponenteTareConexe");
        ComponenteTareConexe.addActionListener(e -> {
            CTC(listaNoduri);
        });

        ArborePartialMinim = new JButton("ArborePartialMinim");
        ArborePartialMinim.addActionListener(e -> {
            Kruskal();
        });

        GenericAlgorithm = new JButton("Generic Algorithm");
        GenericAlgorithm.addActionListener(e -> {
            GenericAlgorithmDo(listaNoduri);
        });

        butttonsPanel.setLayout(new FlowLayout());
        // Adăugare butoane la panoul curent
        butttonsPanel.add(orientedButton);
        butttonsPanel.add(unorientedButton);
        butttonsPanel.add(topologicalOrder);
        butttonsPanel.add(connectedComponents);
        butttonsPanel.add(determinareRadacina);
        butttonsPanel.add(ComponenteTareConexe);
        butttonsPanel.add(ArborePartialMinim);
        butttonsPanel.add(GenericAlgorithm);

        this.add(butttonsPanel, BorderLayout.NORTH);


        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                pointStart = e.getPoint();
                if (SwingUtilities.isRightMouseButton(e) && select==false)
                {
                    int distanceVerificare, raza = node_diam/2;
                    selectedPoint = e.getPoint();
                    for (Node existingNode : listaNoduri)
                    {
                        distanceVerificare = (int) Math.sqrt(Math.pow(existingNode.getCoordX() - selectedPoint.getX() + raza, 2) +
                                Math.pow(existingNode.getCoordY() - selectedPoint.getY() + raza, 2));
                        if (distanceVerificare <  raza)
                        {
                            select = true;
                            selectedPoint.setLocation(existingNode.getCoordX(),existingNode.getCoordY());
                            selectedPointOfNode = existingNode;
                        }
                    }
            }
            }

            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (!isDragging) {
                        addNode(e.getX(), e.getY());
                        repaint();
                    } else {
                        if (isOrientedChanged) {
                            listaArce.clear();
                            for (Node node : listaNoduri) {
                                node.Vecinii.clear();
                            }
                            isOrientedChanged = false;
                        }
                        boolean start = false;
                        boolean end = false;
                        int index = 0, indexStartSave = -1, indexEndSave = -1;
                        Node StartNode = null, EndNode = null;
                        int raza = node_diam / 2, distanceS, distanceF;
                        for (Node existingNode : listaNoduri) {
                            distanceS = (int) Math.sqrt(Math.pow(existingNode.getCoordX() - pointStart.getX() + raza, 2) +
                                    Math.pow(existingNode.getCoordY() - pointStart.getY() + raza, 2));
                            distanceF = (int) Math.sqrt(Math.pow(existingNode.getCoordX() - pointEnd.getX() + raza, 2) +
                                    Math.pow(existingNode.getCoordY() - pointEnd.getY() + raza, 2));

                            if (distanceS < raza && !start) {
                                StartNode = existingNode;
                                indexStartSave = index;
                                pointStart.setLocation(existingNode.getCoordX() + raza, existingNode.getCoordY() + raza);
                                start = true;
                            }
                            if (distanceF < raza && !end) {
                                indexEndSave = index;
                                EndNode = existingNode;
                                end = true;
                                pointEnd.setLocation(existingNode.getCoordX() + raza, existingNode.getCoordY() + raza);
                            }
                            index++;
                        }

                        if (start && end) {
                            Arc arc = new Arc(pointStart, pointEnd);
                            StartNode.Vecinii.add(indexEndSave);
                            StartNode.VeciniNoduri.add(EndNode);
                            arc.setStartNode(StartNode);
                            arc.setEndNodeNode(EndNode);
                            if (!isOriented) {
                                EndNode.Vecinii.add(indexStartSave);
                                EndNode.VeciniNoduri.add(listaNoduri.elementAt(indexEndSave));
                            }

                            System.out.print("Introdu un numar intreg: ");

                            if (scanner.hasNextLine()) {
                                String text = scanner.nextLine();
                                // Folosește linia citită aici


                            try {

                                int numarIntreg = Integer.parseInt(text);
                                arc.setCostInteger(numarIntreg);
                                arc.setCost(text);
                            } catch (NumberFormatException z) {
                                System.out.println("Textul introdus nu poate fi convertit intr-un numar intreg.");
                            }
                            }


                            arc.setStartNode(StartNode);
                            arc.setEndNodeNode(EndNode);
                            arc.startNode = indexStartSave;
                            arc.endNode = indexEndSave;
                            listaArce.add(arc);
                            repaint();

                        }

                    }
                    setMatrix(fisier);
                    pointStart = null;
                    isDragging = false;
                }

            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                   if(SwingUtilities.isLeftMouseButton(e)){
                    pointEnd = e.getPoint();
                    isDragging = true;
                    repaint();
                   }
                   else {
                       Point changePoint = e.getPoint();
                       for (Node existingNode : listaNoduri)
                       {
                           if (selectedPointOfNode == existingNode)
                           {
                               existingNode.setCoordX((int) changePoint.getX());
                               existingNode.setCoordY((int) changePoint.getY());
                           }
                       }
                       for(Arc existingArc : listaArce)
                       {
                           if (selectedPointOfNode == listaNoduri.elementAt(existingArc.startNode) || selectedPointOfNode == listaNoduri.elementAt(existingArc.endNode))
                           {
                               changePoint.setLocation(e.getX()+node_diam/2,e.getY()+node_diam/2);
                               existingArc.setStart(changePoint);
                           }
                           if (selectedPoint == existingArc.getCoordY())
                           {
                               changePoint.setLocation(e.getX()+node_diam/2,e.getY()+node_diam/2);
                               existingArc.setEnd(changePoint);
                           }
                       }
                       select = false;
                       repaint();
                   }
            }
        });
    }

    private void addNode(int x, int y) {
        Node node = new Node(x, y, nodeNr-1);
        double minDistance = 50;
        int numarDeVerificari = 0;
        double distance;
        if (listaNoduri.size() > 0) {
            for (Node n : listaNoduri) {
                distance = Math.sqrt(Math.pow(x - n.getCoordX(), 2) + Math.pow(y - n.getCoordY(), 2));
                if (distance > minDistance)
                    numarDeVerificari++;
            }
            if (numarDeVerificari == listaNoduri.size()) {
                listaNoduri.add(node);
                ++nodeNr;
                repaint();
            }
        } else {
            listaNoduri.add(node);
            ++nodeNr;
            repaint();
        }
    }

    boolean isQuasiStronglyConnected(Vector<Node> graf, Node currentNode) {
        boolean[] visited = new boolean[graf.size()];
        LinkedList<Node> stack = new LinkedList<>();
        stack.push(currentNode); // Adăugăm nodul curent în stivă
            while (!stack.isEmpty()) {
                Node current = stack.pop();
                if (!visited[current.getNumber()]) {
                    visited[current.getNumber()] = true;

                    for (Integer i : current.Vecinii) {
                        if (!visited[i]) {
                            stack.push(listaNoduri.elementAt(i));
                        }
                    }
                }
        }
        for (boolean b : visited) {
            if (!b) {
                return false;
            }
        }
        return true;
    }


    public List<Integer> DFS(Vector<Node> graf)
    {
        Vector<Node> grafModificat = new Vector<>(graf);
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        List<Integer> topologicalOrder = new ArrayList<>();
        Map<Node, Node> parent= new HashMap<>();
        Map<Node, Integer> time1= new HashMap<>();
        Map<Node, Integer> time2= new HashMap<>();
        Boolean hasCycle = false;
        int t = 1;
        int p = 0;
        int index =0;
       while(index < grafModificat.size())
       {
            p++;
            Node StartNode = grafModificat.elementAt(index);
            stack.push(StartNode);
            visited.add(StartNode);
            time1.put(StartNode, t++);
            parent.put(StartNode,null);

            while (!stack.empty()&& !hasCycle)
            {
                Node currentNode = stack.peek();
                grafModificat.remove(currentNode);
                if (currentNode.Vecinii!=null)
                {
                    boolean allNeighBorsVisited = true;
                    for (Node neighbor:currentNode.VeciniNoduri)
                    {
                        if (!visited.contains(neighbor))
                        {
                            stack.push(neighbor);
                            visited.add(neighbor);
                            time1.put(neighbor,t++);
                            parent.put(neighbor,currentNode);
                            allNeighBorsVisited = false;
                            break;
                        }else if (stack.contains(neighbor))
                        {
                            hasCycle = true;
                            System.out.println("Un ciclu a fost gasit!! Nu se poate realiza sortarea topologica!");
                        }
                    }
                    if (allNeighBorsVisited)
                    {
                        stack.pop();
                        time2.put(currentNode,t++);
                        topologicalOrder.add(currentNode.getNumber());
                    }
                }else
                {
                    stack.pop();
                    time2.put(currentNode,t++);
                    topologicalOrder.add(currentNode.getNumber());
                }

            }
        }
        if (!hasCycle){
            for (int it = topologicalOrder.size()-1;it>-1;it--)
            {
                System.out.print(topologicalOrder.get(it) + " ");
            }
            System.out.println();
            isTopological = true;
            return topologicalOrder;
        }
        isTopological = false;
        return null;
    }

    public Vector<Node> Inversare(Vector<Node> graf){

        for (Node currentNode : graf)
        {
            for (Node arc : currentNode.VeciniNoduri)
            {
                arc.VeciniInversi.add(currentNode);
            }
        }
        return graf;
    }

    public Stack<Node> PTDF(Vector<Node> graf)
    {
        Stack<Node> dfsParcurgere = new Stack<>();
        Vector<Node> grafModificat = new Vector<>(graf);
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        List<Integer> topologicalOrder = new ArrayList<>();
        Map<Node, Node> parent= new HashMap<>();
        Map<Node, Integer> time1= new HashMap<>();
        Map<Node, Integer> time2= new HashMap<>();
        Boolean hasCycle = false;
        int t = 1;
        int p = 0;
        int index =0;
        while(index < grafModificat.size())
        {
            p++;
            Node StartNode = grafModificat.elementAt(index);
            stack.push(StartNode);
            visited.add(StartNode);
            time1.put(StartNode, t++);
            parent.put(StartNode,null);

            while (!stack.empty()&& !hasCycle)
            {
                Node currentNode = stack.peek();
                grafModificat.remove(currentNode);
                if (currentNode.Vecinii!=null)
                {
                    boolean allNeighBorsVisited = true;
                    for (Node neighbor:currentNode.VeciniNoduri)
                    {
                        if (!visited.contains(neighbor))
                        {
                            stack.push(neighbor);
                            visited.add(neighbor);
                            time1.put(neighbor,t++);
                            parent.put(neighbor,currentNode);
                            allNeighBorsVisited = false;
                            break;
                        }
                    }
                    if (allNeighBorsVisited)
                    {
                        stack.pop();
                        time2.put(currentNode,t++);
                        dfsParcurgere.push(currentNode);
                    }
                }else
                {
                    stack.pop();
                    time2.put(currentNode,t++);
                    dfsParcurgere.push(currentNode);
                }

            }
        }

        return dfsParcurgere;
    }


    public void DeterminareComponenteConexe(Vector<Node> graf)
    {
        findConnectedComponents = true;
        Vector<Node> grafFormat = new Vector<>(graf);
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        int p = 0, index = 0;
        HashMap<Integer, Color> componentColor = componentColors;
        Random random = new Random();

        while(index<grafFormat.size())
        {
            Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            Node StartNode = grafFormat.elementAt(index);
            stack.add(StartNode);
            visited.add(StartNode);
            componentColors.put(StartNode.getNumber(),randomColor);
            while(!stack.empty())
            {
                Node currentNode = stack.peek();
                stack.pop();
                grafFormat.remove(currentNode);
                if (currentNode.Vecinii!=null)
                {
                    boolean foundNeighbor = true;
                    for (Integer neighbour : currentNode.Vecinii) {
                        if (!visited.contains(listaNoduri.elementAt(neighbour))) {
                            stack.push(listaNoduri.elementAt(neighbour));
                            visited.add(listaNoduri.elementAt(neighbour));
                            componentColor.put(neighbour, randomColor);
                        }

                    }

            }

            }
            p++;

        }

        repaint();
        System.out.println(p);

    }

    public void determinareRadacina(Vector<Node>graf)
    {
        List<Integer>topologicalSortInteger = DFS(graf);
        if (topologicalSortInteger!=null){
            isQuasiConnected = isQuasiStronglyConnected(graf, graf.elementAt(topologicalSortInteger.getLast()));
        }
        if (isTopological && isQuasiConnected == true) {
            Vector<Node> grafNou = new Vector<>(graf);
            Integer nrNoduri = 0;
            Stack<Node> stack = new Stack<>();
            Node startNode;
            startNode = graf.elementAt(topologicalSortInteger.getLast());
            stack.push(startNode);
            while (!stack.empty())
            {
                Node currentNode = stack.peek();
                stack.pop();
                grafNou.remove(currentNode);
                if (currentNode.VeciniNoduri.size()!=0)
                {
                    for (Node neighbor : currentNode.VeciniNoduri)
                    {
                        stack.push(neighbor);
                        nrNoduri++;
                    }
                }else {
                }
            }
            if (grafNou.size() == 0)
            {
                findRadacina = true;
                indexRadacina = topologicalSortInteger.getLast();
                repaint();
            }
        }

        repaint();
    }



    public void CTC(Vector<Node> graf) {
       int index =-1 ;
        Stack<Node> dfs1 = PTDF(graf);
        Set<Node> visited = new HashSet<>();
        Vector<Node> inversareGraf = Inversare(graf);
        LinkedList<Node>[] componenteConexe = new LinkedList[graf.size()];
        while (!dfs1.empty())
        {
            Node currentNode = dfs1.peek();
            dfs1.pop();
            Stack<Node> dfs2 = new Stack<>();
            dfs2.push(currentNode);
            if (!visited.contains(currentNode))
            {
                index++;
                componenteConexe[index] = new LinkedList<>();
            }
            while(!dfs2.empty()){
                if (!visited.contains(currentNode)&& currentNode.VeciniInversi!=null)
                {
                    visited.add(currentNode);
                    boolean allNeighbours = true;
                    componenteConexe[index].add(currentNode);
                    for (Node neighbour : inversareGraf.elementAt(currentNode.getNumber()).VeciniInversi)
                    {
                        if (!visited.contains(neighbour))
                        {
                            dfs2.push(neighbour);
                            allNeighbours = false;
                            currentNode = neighbour;
                            break;
                        }
                    }
                    if (allNeighbours)
                    {
                        dfs2.pop();
                    }

                }else
                {
                    dfs2.pop();
                }
            }
        }
        for (LinkedList<Node> list : componenteConexe) {
            if (list != null && !list.isEmpty()) {
                Node StronglyConnected = new Node(list.getFirst().getCoordX(),list.getFirst().getCoordY(),list.getFirst().getNumber());
                String nodes = "";
            for (Node currentNode : list)
            {
                nodes = nodes  + String.valueOf(currentNode.getNumber()) + " ";
            }
            StronglyConnected.setLabel(nodes);
            StronglyConnected.setCoordX(list.getFirst().getCoordX());
            StronglyConnected.setCoordY(list.getFirst().getCoordY());
            StronglyConnectedGraph.add(StronglyConnected);
        }

        }
       int  nr = componenteConexe.length;

        for (int i = 0; i < nr; i++) {
            if (componenteConexe[i] != null) {
                if (!componenteConexe[i].isEmpty()) {
                    Point Start = new Point();
                    Point End = new Point();
                    for (Node currentNode : componenteConexe[i]) {
                        for (int j = i + 1; j < nr; j++) {
                            if (componenteConexe[j] != null) {
                                if (!componenteConexe[j].isEmpty()) {
                                    for (Node nextNode : componenteConexe[j])
                                        if (currentNode.VeciniNoduri.contains(nextNode)) {
                                            Start = new Point(StronglyConnectedGraph.elementAt(i).getCoordX() + 35, StronglyConnectedGraph.elementAt(i).getCoordY() + 35);
                                            End = new Point(StronglyConnectedGraph.elementAt(j).getCoordX() + 35, StronglyConnectedGraph.elementAt(j).getCoordY() + 35);
                                            Arc arc = new Arc(Start, End);
                                            arc.setStartNode(componenteConexe[i].getFirst());
                                            arc.setEndNode(componenteConexe[j].getFirst());
                                            listaArceComponenteConexe.add(arc);
                                        }
                                }
                            }
                        }
                    }
                }

            }
        }
        StronglyConnectedDrawingGraph = true;
        repaint();
    }

    public Vector<Node> isNodeVerified(Vector<Vector<Node>>multime,Node nodCautat)
    {

        for (Vector<Node> currentNode : multime)
        {
            if (currentNode.contains(nodCautat))
                return currentNode;
        }
        return null;
    }

    public void Kruskal(){
        Vector<Arc> listaArceArbore = new Vector<>();
        Comparator<Arc> costComparator = Comparator.comparingInt(Arc::getCostInteger);
        Collections.sort(listaArce, costComparator);
        Vector<Vector<Node>> multimiDeNoduri = new Vector<>();
        int tempCounter;
        for (Arc currentArc : listaArce)
        {
            tempCounter = 0;

                if (isNodeVerified(multimiDeNoduri, currentArc.getStartNodeNode()) == null)
                    tempCounter++;
                if (isNodeVerified(multimiDeNoduri, currentArc.getEndNodeNode()) == null)
                    tempCounter++;
            switch (tempCounter) {
                case 0:
                    Vector<Node>start = isNodeVerified(multimiDeNoduri, currentArc.getStartNodeNode());
                    Vector<Node>end = isNodeVerified(multimiDeNoduri, currentArc.getEndNodeNode());
                    if (!start.equals(end)){
                        start.addAll(end);
                        multimiDeNoduri.remove(end);
                        listaArceArbore.add(currentArc);
                    }
                    break;

                case 1:
                    if(isNodeVerified(multimiDeNoduri,currentArc.getStartNodeNode())!=null){
                        isNodeVerified(multimiDeNoduri,currentArc.getStartNodeNode()).add(currentArc.getEndNodeNode());
                    }
                    else{
                        isNodeVerified(multimiDeNoduri,currentArc.getEndNodeNode()).add(currentArc.getStartNodeNode());
                    }
                    listaArceArbore.add(currentArc);
                    break;

                case 2:
                    multimiDeNoduri.add(new Vector<>());
                    multimiDeNoduri.lastElement().add(currentArc.getStartNodeNode());
                    multimiDeNoduri.lastElement().add(currentArc.getEndNodeNode());
                    listaArceArbore.add(currentArc);
                    break;
            }

        }
        listaArce = listaArceArbore;
        repaint();
    }

    public void GenericAlgorithmDo(Vector<Node> listaNoduri){
        Vector<Arc> partialTreeEdge = new Vector<>();
        Vector<Vector<Node>> multimiDeNoduri = new Vector<>();
        int min = Integer.MAX_VALUE;
        Node EndNode = null;
        Arc minArc = null;
        for (Node currentNode : listaNoduri)
        {
            Vector<Node> multimeNodIndice = new Vector<>();
            multimeNodIndice.add(currentNode);
            multimiDeNoduri.add(multimeNodIndice);
        }
        Vector<Arc> multimeAleasa = new Vector<>();
        for(int  i = 0 ; i <= listaNoduri.size()-1; i++)
        {
            Vector<Node> multimeOptima = multimiDeNoduri.firstElement();
            min = Integer.MAX_VALUE;
           for (Node currentNode : multimeOptima)
            {
               for (Arc currentArc : listaArce)
               {
                   if (currentArc.getStartNodeNode() == currentNode && currentArc.getCostInteger()<=min && !multimeOptima.contains(currentArc.getEndNodeNode()))
                   {
                       EndNode = currentArc.getEndNodeNode();
                       min = currentArc.getCostInteger();
                       minArc = currentArc;
                   }
                   if (currentArc.getEndNodeNode() == currentNode && currentArc.getCostInteger()<=min && !multimeOptima.contains(currentArc.getStartNodeNode()))
                   {
                       EndNode = currentArc.getStartNodeNode();
                       min = currentArc.getCostInteger();
                       minArc = currentArc;
                   }
               }
            }
            multimeOptima.add(EndNode);
            multimeAleasa.add(minArc);
        }
        listaArce = multimeAleasa;
        repaint();
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (StronglyConnectedDrawingGraph == false){

        for (Arc a : listaArce) {
            a.drawArrowEdge(g, isOriented);
            a.drawArc(g);
        }

        if (pointStart != null) {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
        }

        for (int i = 0; i < listaNoduri.size(); ++i) {
            listaNoduri.elementAt(i).drawNode(g, node_diam);
        }

        if(findConnectedComponents==true) {
            Set<Map.Entry<Integer, Color>> entrySet = componentColors.entrySet();
            for (Map.Entry<Integer, Color> entry : entrySet) {
                Integer key = entry.getKey();
                Color value = entry.getValue();
                listaNoduri.elementAt(key).drawComponents(g,value,node_diam);
            }

        }
        if (findRadacina == true){
            if (isQuasiConnected == true && isTopological == true)
                listaNoduri.elementAt(indexRadacina).drawComponents(g, Color.BLACK,node_diam);
            else
                for (int i = 0; i < listaNoduri.size(); ++i) {
                    listaNoduri.elementAt(i).drawNode(g, node_diam);
                }
        }
    }else
    {
        for(Arc a : listaArceComponenteConexe)
        {
            a.drawArrowEdgeConnected(g,isOriented);
        }

        for (int i = 0; i < StronglyConnectedGraph.size(); i++) {
            StronglyConnectedGraph.elementAt(i).drawConnectedComponent(g);
        }
        StronglyConnectedDrawingGraph = false;
    }
    }


    public void setOriented(boolean oriented) {
        isOrientedChanged = isOriented != oriented;
        isOriented = oriented;
        repaint();
    }
}
