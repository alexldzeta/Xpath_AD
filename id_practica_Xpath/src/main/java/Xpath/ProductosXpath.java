package Xpath;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.xpath.*;

public class ProductosXpath {

	public static void display() {

		try {

			// Carga del documento xml
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse("src//main//java//datos//Productos.xml");

			// Preparacion del documento
			XPath xp = XPathFactory.newInstance().newXPath();

			// Consultas
			NodeList nodelist = (NodeList) xp.compile("//produc").evaluate(d, XPathConstants.NODESET);

			System.out.println("Numero de productos: " + nodelist.getLength());
			System.out.println("Informacion de los productos:");

			for (int i = 0; i < nodelist.getLength(); i++) {

				System.out.println("Denominación: " + xp.compile("./denominacion").evaluate(nodelist.item(i))); 
				System.out.println("Precio: " + xp.compile("./precio").evaluate(nodelist.item(i))); 

				System.out.println("*********************************************************************************");

			}

			//Coger los prodcutos que sean placas bases
			XPathExpression expr = xp.compile("//produc[contains(denominacion,'Placa Base')]");
			Object result = expr.evaluate(d, XPathConstants.NODESET);
			nodelist = (NodeList) result;
			
				for (int i = 0; i < nodelist.getLength(); i++) {
					
					System.out.println("Los id de los productos placa base son: " + (nodelist.item(i).getChildNodes().item(1).getTextContent()));
					
			}
				
			System.out.println("*********************************************************************************");
				
			
			//Obtén los nodos de los productos con precio mayor de 60€ y de la zona 20.
			expr = xp.compile("//produc[precio>60 and cod_zona= '20']/denominacion/text()");
			Object result2 = expr.evaluate(d, XPathConstants.NODESET);
			nodelist = (NodeList) result2;
			
				for(int i = 0; i < nodelist.getLength(); i++) {
					
					System.out.println("Los productos que valen mas de 60€ y son de la zona 20 son: " + (nodelist.item(i).getNodeValue()));
					
				}
				
			System.out.println("*********************************************************************************");
				
			//Obtén el número de productos que sean memorias y de la zona 10.	
			expr = xp.compile("/productos/produc[denominacion[contains(., 'Memoria')] and cod_zona[text() = 10]]");
			Object result3 = expr.evaluate(d, XPathConstants.NODESET);
			nodelist = (NodeList) result3;
			
			System.out.println("Productos memoria y de la zona 10 son: " + nodelist.getLength());
				
				
			System.out.println("*********************************************************************************");	
				
			
			//Obtén los datos de los productos cuyo stock mínimo sea mayor que su stock actual.
				
			expr = xp.compile("/productos/produc[number(stock_minimo) > number(stock_actual)]");
			Object result4 = expr.evaluate(d, XPathConstants.NODESET);
			nodelist = (NodeList) result4;
				
			for (int i = 0; i < nodelist.getLength(); i++) {

				System.out.print("Nodo " + i + ": ");
				System.out.println(nodelist.item(i).getNodeName() + " : " + nodelist.item(i).getTextContent());

			}
			System.out.println();
				
			System.out.println("*********************************************************************************");		
				
			
			//Obtén el nombre del producto y el precio de aquellos cuyo stock mínimo sea mayor que su stock actual y sean de la zona 40.
			expr = xp.compile("/productos/produc/*[number(../stock_minimo/text()) > number(../stock_actual/text()) and ../cod_zona/text() = 40]");
			Object result5 = expr.evaluate(d, XPathConstants.NODESET);
			nodelist = (NodeList) result5;	
			
			for (int i = 0; i < nodelist.getLength(); i++) {

				System.out.print("Nodo 1: ");
				System.out.println(nodelist.item(i).getNodeName() + " : " + nodelist.item(i).getTextContent());

			}
			System.out.println();
				
			System.out.println("*********************************************************************************");	
						

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

	}

}
