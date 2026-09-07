
package solution;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author lewi0146, leib0006
 */
public class GraphBuilder {

    public static Graph buildFromGraphML(String file) throws JDOMException, IOException {

        int nNodes = 0;
        // the SAXBuilder is the easiest way to create the JDOM2 objects.
        SAXBuilder jdomBuilder = new SAXBuilder();

        // jdomDocument is the JDOM2 Object
        Document jdomDocument = jdomBuilder.build(file);

        // The root element is the root of the document. we print its name
        //System.out.println(jdomDocument.getRootElement().getName());

        Element graphxml = jdomDocument.getRootElement();

        Namespace ns = graphxml.getNamespace(); // Namespace.getNamespace("http://foo.com");

        Element graph = graphxml.getChild("graph", ns);
        //System.out.println(graph.getName() + ": " + graph.getChildren().size());

        List<Element> nodes = graph.getChildren("node", ns);

        //System.out.println("number of nodes: " + nodes.size());


        //GraphBuilder g = null; //new Graph(nodes.size());

        List<Element> edges = graph.getChildren("edge", ns);
        MyGraph g = new MyGraph(nodes.size());
        //System.out.println("number of edges: " + edges.size());
        int max = 0;
        int min = 10000;
        for (Element e : edges) {
            //System.out.println("e: " + e.getName());
            List<Attribute> at = e.getAttributes();
            //System.out.println(at);
            int nS = e.getAttribute("source").getIntValue();
            int nD = e.getAttribute("target").getIntValue();
            int nV = Integer.parseInt(e.getChild("data", ns).getText());
            g.addEdge(nS,nD,nV);
            //System.out.println(nS+" "+nD+" "+nV);
            // here you could add an edge to your graph from source to target with the weight of nV

            if (nV > max) max = nV;
            if (nV < min) min = nV;
        }



        //System.out.println("max weight: "+max);
        //System.out.println("min weight: "+min);
        return g;
    }

}

