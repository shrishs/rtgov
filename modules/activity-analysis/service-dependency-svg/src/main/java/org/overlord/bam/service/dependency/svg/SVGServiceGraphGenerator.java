/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008-12, Red Hat Middleware LLC, and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.overlord.bam.service.dependency.svg;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.overlord.bam.service.dependency.InvocationLink;
import org.overlord.bam.service.dependency.OperationNode;
import org.overlord.bam.service.dependency.ServiceGraph;
import org.overlord.bam.service.dependency.ServiceNode;
import org.overlord.bam.service.dependency.UsageLink;
import org.overlord.bam.service.dependency.layout.ServiceGraphLayout;

/**
 * This class generates a SVG representation of a
 * service graph.
 *
 */
public class SVGServiceGraphGenerator {
    
    private static final Logger LOG=Logger.getLogger(SVGServiceGraphGenerator.class.getName());

    /**
     * The default constructor.
     */
    public SVGServiceGraphGenerator() {
    }
    
    /**
     * This method generates the SVG representation of the supplied
     * service graph to the output stream.
     * 
     * @param sg The service graph
     * @param os The output stream
     * @throws Exception Failed to generate SVG
     */
    public void generate(ServiceGraph sg, java.io.OutputStream os) 
                        throws Exception {
        
        org.w3c.dom.Document doc=loadTemplate("main");
        
        if (doc != null) {
            org.w3c.dom.Element container=doc.getDocumentElement();
            
            org.w3c.dom.Node insertPoint=null;
            
            org.w3c.dom.NodeList nl=container.getElementsByTagName("insert");
            if (nl.getLength() == 1) {
                insertPoint = nl.item(0);
            }
            
            for (ServiceNode sn : sg.getServiceNodes()) {
                generateService(sn, container, insertPoint);
            }
            
            for (UsageLink ul : sg.getUsageLinks()) {
                generateUsageLink(ul, container, insertPoint);
            }

            for (InvocationLink il : sg.getInvocationLinks()) {
                generateInvocationLink(il, container, insertPoint);
            }

            // Remove insertion point
            if (insertPoint != null) {
                container.removeChild(insertPoint);
            }
            
            saveDocument(doc, os);
        }
    }
    
    /**
     * This method generates the usage link.
     * 
     * @param ul The usage link
     * @param container The container
     * @param insertPoint The insertion point
     */
    protected void generateUsageLink(UsageLink ul,
                    org.w3c.dom.Element container, org.w3c.dom.Node insertPoint) {
        
        int x1=(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.X_POSITION)
                +(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.WIDTH);
        
        int y1=(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.Y_POSITION);
        
        int x2=(Integer)ul.getTarget().getProperties().get(ServiceGraphLayout.X_POSITION);
        
        int y2=(Integer)ul.getTarget().getProperties().get(ServiceGraphLayout.Y_POSITION);
        
        int x3=(Integer)ul.getTarget().getProperties().get(ServiceGraphLayout.X_POSITION);
        
        int y3=(Integer)ul.getTarget().getProperties().get(ServiceGraphLayout.Y_POSITION)
                +(Integer)ul.getTarget().getProperties().get(ServiceGraphLayout.HEIGHT);
        
        int x4=(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.X_POSITION)
                +(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.WIDTH);
        
        int y4=(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.Y_POSITION)
                +(Integer)ul.getSource().getProperties().get(ServiceGraphLayout.HEIGHT);
        
        org.w3c.dom.Element polygon=
                container.getOwnerDocument().createElement("polygon");
        polygon.setAttribute("points", x1+","+y1+" "+x2+","+y2+" "
                +x3+","+y3+" "+x4+","+y4);
        polygon.setAttribute("style", "fill:#FFD6AD;fill-opacity:0.3");
        
        container.insertBefore(polygon, insertPoint);
        
        // Title
        org.w3c.dom.Element title=
                container.getOwnerDocument().createElement("desc");
        polygon.appendChild(title);
    
        org.w3c.dom.Text titleText=
                container.getOwnerDocument().createTextNode(getDescription(ul));
        title.appendChild(titleText);
    
    }
    
    /**
     * This method returns the description to be used for the
     * invocation link.
     * 
     * @param il The invocation link
     * @return The description
     */
    protected String getDescription(UsageLink ul) {
        return (ul.getSource().getService().getServiceType()
                +" -> "+ul.getTarget().getService().getServiceType());
    }
    
    /**
     * This method generates the invocation link.
     * 
     * @param il The invocation link
     * @param container The container
     * @param insertPoint The insertion point
     */
    protected void generateInvocationLink(InvocationLink il,
                    org.w3c.dom.Element container, org.w3c.dom.Node insertPoint) {
        
        int x1=(Integer)il.getSource().getProperties().get(ServiceGraphLayout.X_POSITION)
                +(Integer)il.getSource().getProperties().get(ServiceGraphLayout.WIDTH);
        
        int y1=(Integer)il.getSource().getProperties().get(ServiceGraphLayout.Y_POSITION)
                +(Integer)il.getSource().getProperties().get(ServiceGraphLayout.HEIGHT)/2;
        
        int x2=(Integer)il.getTarget().getProperties().get(ServiceGraphLayout.X_POSITION);
        
        int y2=(Integer)il.getTarget().getProperties().get(ServiceGraphLayout.Y_POSITION)
                +(Integer)il.getTarget().getProperties().get(ServiceGraphLayout.HEIGHT)/2;
        
        org.w3c.dom.Element line=
                container.getOwnerDocument().createElement("line");
        line.setAttribute("x1", ""+x1);
        line.setAttribute("y1", ""+y1);
        line.setAttribute("x2", ""+x2);
        line.setAttribute("y2", ""+y2);
        
        //String colour="#FF0000";
        String colour="#00FF00";
        
        line.setAttribute("style", "stroke:"+colour+";stroke-width:3");
        
        container.insertBefore(line, insertPoint);
        
        // Title
        org.w3c.dom.Element title=
                container.getOwnerDocument().createElement("desc");
        line.appendChild(title);
    
        org.w3c.dom.Text titleText=
                container.getOwnerDocument().createTextNode(getDescription(il));
        title.appendChild(titleText);
    
    }
    
    /**
     * This method returns the description to be used for the
     * invocation link.
     * 
     * @param il The invocation link
     * @return The description
     */
    protected String getDescription(InvocationLink il) {
        return (il.getTarget().getService().getServiceType()
                +" -> "+il.getTarget().getOperation().getName());
    }
    
    /**
     * This method generates the service node.
     * 
     * @param sn The service node
     * @param container The container
     * @param insertPoint The insertion point
     */
    protected void generateService(ServiceNode sn, org.w3c.dom.Element container,
                        org.w3c.dom.Node insertPoint) {
        
        org.w3c.dom.Element rect=
                    container.getOwnerDocument().createElement("rect");
        rect.setAttribute("width",
                sn.getProperties().get(ServiceGraphLayout.WIDTH).toString());
        rect.setAttribute("height",
                sn.getProperties().get(ServiceGraphLayout.HEIGHT).toString());
        rect.setAttribute("x",
                sn.getProperties().get(ServiceGraphLayout.X_POSITION).toString());
        rect.setAttribute("y",
                sn.getProperties().get(ServiceGraphLayout.Y_POSITION).toString());
        
        rect.setAttribute("fill", "#B8DBFF");
        rect.setAttribute("stroke", "white");
        rect.setAttribute("stroke-width", "2");
        rect.setAttribute("filter", "url(#f1)");
        
        container.insertBefore(rect, insertPoint);
        
        // Title
        org.w3c.dom.Element title=
                container.getOwnerDocument().createElement("desc");
        rect.appendChild(title);
    
        org.w3c.dom.Text titleText=
                container.getOwnerDocument().createTextNode(sn.getService().getServiceType());
        title.appendChild(titleText);

        // Generate text
        org.w3c.dom.Element text=
                container.getOwnerDocument().createElement("text");
        
        int x=(Integer)sn.getProperties().get(ServiceGraphLayout.X_POSITION);
        x += 5;
        
        int y=(Integer)sn.getProperties().get(ServiceGraphLayout.Y_POSITION);
        y += 10;
        
        text.setAttribute("x", ""+x);
        text.setAttribute("y", ""+y);
        
        text.setAttribute("font-family", "Verdana");
        text.setAttribute("font-size", "10");
        text.setAttribute("fill", "#00008F");
        
        org.w3c.dom.Text value=
                container.getOwnerDocument().createTextNode(
                        sn.getService().getServiceType());
        text.appendChild(value);
        
        container.insertBefore(text, insertPoint);
    
        // Generate operations
        for (OperationNode opn : sn.getOperations()) {
            generateOperation(opn, container, insertPoint);
        }
    }
    
    /**
     * This method generates the operation node.
     * 
     * @param opn The operation node
     * @param doc The svg document
     * @param insertPoint The insertion point
     */
    protected void generateOperation(OperationNode opn, org.w3c.dom.Element container,
            org.w3c.dom.Node insertPoint) {
        
        org.w3c.dom.Element rect=
                    container.getOwnerDocument().createElement("rect");
        rect.setAttribute("width",
                opn.getProperties().get(ServiceGraphLayout.WIDTH).toString());
        rect.setAttribute("height",
                opn.getProperties().get(ServiceGraphLayout.HEIGHT).toString());
        rect.setAttribute("x",
                opn.getProperties().get(ServiceGraphLayout.X_POSITION).toString());
        rect.setAttribute("y",
                opn.getProperties().get(ServiceGraphLayout.Y_POSITION).toString());
        
        rect.setAttribute("fill", "#85D6FF");
        rect.setAttribute("stroke", "#78C1E6");
        rect.setAttribute("stroke-width", "1");
        
        container.insertBefore(rect, insertPoint);
        
        org.w3c.dom.Element text=
                container.getOwnerDocument().createElement("text");
        
        int x=(Integer)opn.getProperties().get(ServiceGraphLayout.X_POSITION);
        x += 5;
        
        int y=(Integer)opn.getProperties().get(ServiceGraphLayout.Y_POSITION);
        y += 14;
        
        text.setAttribute("x", ""+x);
        text.setAttribute("y", ""+y);
        
        text.setAttribute("font-family", "Verdana");
        text.setAttribute("font-size", "12");
        text.setAttribute("fill", "#00008F");
        
        org.w3c.dom.Text value=
                container.getOwnerDocument().createTextNode(
                        opn.getOperation().getName());
        text.appendChild(value);
        
        container.insertBefore(text, insertPoint);
    }
    
    /**
     * This method saves the supplied document to the output stream.
     * 
     * @param doc The SVG document
     * @param os The output stream
     * @throws Exception Failed to save SVG document
     */
    protected void saveDocument(org.w3c.dom.Document doc, java.io.OutputStream os) 
                        throws Exception {
        
        // Write out SVG document
        javax.xml.transform.Transformer transformer=
                javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        
        transformer.transform(new javax.xml.transform.dom.DOMSource(doc),
                new javax.xml.transform.stream.StreamResult(os));

    }
    
    /**
     * This method returns the named template file.
     * 
     * @param name The svg template name
     * @return The template, or null if failed to load
     */
    protected org.w3c.dom.Document loadTemplate(String name) {
        org.w3c.dom.Document ret=null;
        
        try {
            java.io.InputStream is=ClassLoader.getSystemResourceAsStream("templates"
                            +java.io.File.separator+name+".svg");
            
            DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
            
            ret = builder.parse(is);
                        
            is.close();
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to load template '"+name+"'", e);
        }
        
        return (ret);
    }
}