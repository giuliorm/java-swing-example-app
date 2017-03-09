import database.*;
import domain.business.Activity;
import domain.business.WBS;
import helpers.DataGenerator;
import services.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class Main extends JFrame {

    private static ActivityService activityService;
    private static WbsService wbsService;
    private static BusinessMapperService businessMapperService;
    private static MSSQLConnection connection;

    private JButton quitButton;
    private JButton generateButton;
    private JButton loadButton;
    private JLabel wbsItemsLabel;
    private JLabel wbsDescription;
    private JLabel activityItemsLabel;
    private JLabel activityDescription;
    private JTextField wbsTextField;
    private JTextField activityTextField;

    public Main() {
        init();
        initComponents();
        initUI();
        createGenerateLayout();
    }

    private void initComponents() {
        generateButton = new JButton("Generate!");
        quitButton = new JButton("OK");
        loadButton = new JButton("Load data");

        loadButton.addActionListener((ActionEvent event) -> {
            createTreeLayout();
        });

        quitButton.addActionListener((ActionEvent event) -> {
            connection.close();
            System.exit(0);
        });

        generateButton.addActionListener((ActionEvent event) -> {
            String wbsNumber = wbsTextField.getText();
            String activityNumber = activityTextField.getText();
            try {
                int wbs = Integer.parseInt(wbsNumber);
                int activity = Integer.parseInt(activityNumber);
                if (wbs >= 0 && activity >= 0) {
                    generateData(wbs, activity);
                    createTreeLayout();
                }
            }
            catch (Exception ex) {

            }
        });

        wbsItemsLabel = new JLabel("WBS: ");
        activityItemsLabel = new JLabel("Activity: ");
        wbsDescription = new JLabel("(write the amount of wbs here)");
        activityDescription = new JLabel("(write the amount of activities here)");
        wbsTextField = new JTextField("", 5);
        activityTextField = new JTextField("", 5);
    }

    private static void init() {

        connection = MSSQLConnection.connect("jdbc:sqlserver://127.0.0.1:1941;databaseName=test_db;integratedSecurity=true;",
                "sa", "1234");
        businessMapperService = new BusinessMapperServiceImpl();
        wbsService = new WbsServiceImpl(new WbsRepositoryImpl(connection), businessMapperService);
        activityService = new ActivityServiceImpl(new ActivityRepositoryImpl(connection), businessMapperService);
    }

    private DefaultMutableTreeNode createTreeLayoutRecursively(WBS node){
        if (node != null) {
            BigDecimal sum = node.getSum();
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node.getName() + "(" + node.getSum().toString() + ")");

            Iterator<WBS> iterator = node.iterator();
            while(iterator.hasNext()) {
                treeNode.add(createTreeLayoutRecursively(iterator.next()));
            }
            for(Activity activity : node.getActivities()) {
                if (activity != null)
                    treeNode.add(new DefaultMutableTreeNode(activity.getName() +
                            " (" + activity.getQuantity().toPlainString() + ")"));
            }
            return treeNode;
        } else return null;
    }

    private void createTreeLayout(){
        JTree tree;
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("WBS tree");
        List<WBS> wbses = wbsService.findAll();
        Iterator<WBS> root = wbses.stream().filter(item -> item.getParent() == null).iterator();
        while(root.hasNext()){
            DefaultMutableTreeNode node = createTreeLayoutRecursively(root.next());
            if (node != null)
                top.add(node);
        }
        tree = new JTree(top);

        JScrollPane treeView = new JScrollPane(tree);
        Container pane = getContentPane();

        GroupLayout gl = new GroupLayout(pane);
        pane.removeAll();
        pane.setLayout(gl);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addGroup(gl.createSequentialGroup()
                        .addComponent(treeView)
                        .addComponent(quitButton)));


        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                        .addComponent(treeView)
                        .addComponent(quitButton)));
    }

    private void createGenerateLayout() {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addGroup(gl.createSequentialGroup()
                        .addComponent(wbsItemsLabel)
                        .addComponent(wbsTextField))
                .addGroup(gl.createSequentialGroup()
                        .addComponent(wbsDescription))
                .addGroup(gl.createSequentialGroup()
                        .addComponent(activityItemsLabel)
                        .addComponent(activityTextField))
                .addGroup(gl.createSequentialGroup()
                        .addComponent(activityDescription))
                .addGroup(gl.createSequentialGroup()
                        .addComponent(generateButton)
                        .addComponent(loadButton)
                        .addComponent(quitButton)));


        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                        .addComponent(wbsItemsLabel)
                        .addComponent(wbsTextField))
                .addGroup(gl.createParallelGroup()
                        .addComponent(wbsDescription))
                .addGroup(gl.createParallelGroup()
                        .addComponent(activityItemsLabel)
                        .addComponent(activityTextField))
                .addGroup(gl.createParallelGroup()
                        .addComponent(activityDescription))
                .addGroup(gl.createParallelGroup()
                        .addComponent(generateButton)
                        .addComponent(loadButton)
                        .addComponent(quitButton)));
    }

    private void initUI() {
        setTitle("MSSQL App");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }

    private static void generateData(int numberOfWBS, int numberOfActivities) {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.generateData(numberOfActivities, numberOfWBS);
        wbsService.insertAll(dataGenerator.getWBS());
        activityService.insertAll(dataGenerator.getActivities());
    }
    public static void main(String[] args) {
        //init();
        //generateData();
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
      //  List<WBS> wbs = wbsService.findAll();
     //   List<Activity> activities = activityService.findAll();
    }
}
