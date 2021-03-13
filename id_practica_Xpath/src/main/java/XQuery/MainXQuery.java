package XQuery;


import java.util.Objects;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

public class MainXQuery {

	private static Database db;
	private static XQueryService xqueryService;

	public static void main(String[] args) {
		
		try {
		
			db = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
			Collection col = Objects.requireNonNull(db.getCollection("xmldb:exist://localhost:8080/exist/xmlrpc/db/XML/productos.xml", "admin", "admin"));
			xqueryService = Objects.requireNonNull((XQueryService) col.getService(XQueryService.SERVICE_NAME, null));

			ResourceIterator resourceIterator;
			
			
			System.out.println("******************** CONSUTLAS PRODUCTOS ********************");
					
					//Obtén por cada zona el número de productos que tiene.
					resourceIterator = xqueryService.query("for $v in distinct-values(/productos/produc/cod_zona) return ($v, count(/productos/produc[cod_zona = $v]))").getIterator();

					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();
						XMLResource zona = ((XMLResource) resourceIterator.nextResource());
						System.out.print("La zona " + zona.getContent());
						
							if (resourceIterator.hasMoreResources()) {
								
								XMLResource count = ((XMLResource) resourceIterator.nextResource());
								System.out.println(" tiene " + count.getContent() + " productos");
						}
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					

					//Obtén la denominación de los productos entre las etiquetas <zona10></zona10>
					//si son del código de zona 10, <zona20></zona20> si son del código de zona 20,
					//<zona30></zona30> si son del código de zona 30 y <zona40></zona40> si son del
					//código de zona 40.
					resourceIterator = xqueryService.query("for $v in distinct-values(/productos/produc/cod_zona) return element{ 'zona' || $v }{ /productos/produc[cod_zona = $v]/denominacion }").getIterator();
					
					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();
						XMLResource nodo = ((XMLResource) resourceIterator.nextResource());
						System.out.println(nodo.getContent());
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					

					//Obtén por cada zona la denominación del o de los productos más caros.
					resourceIterator = xqueryService.query("for $v in distinct-values(/productos/produc/cod_zona) return ($v, /productos/produc[precio = max(/productos/produc[cod_zona = $v]/precio)]/denominacion/text())").getIterator();
					
					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();
						XMLResource nodo = ((XMLResource) resourceIterator.nextResource());
						System.out.print("En la zona " + nodo.getContent());
						
							if (resourceIterator.hasMoreResources()) {
								
								XMLResource den = ((XMLResource) resourceIterator.nextResource());
								System.out.println(", el producto más caro es " + den.getContent());
						}
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					

					//Obtén la denominación de los productos contenida entra las etiquetas <placa></pla-ca> para los productos en cuya denominación aparece la palabra Placa Base, 
					//<memoria></memoria>, para los que contienen la palabra Memoria <micro></micro>,
					//para los que contienen la palabra Micro y <otros></otros> para el resto de productos.
					resourceIterator = xqueryService.query("(<placa>{/productos/produc/denominacion[contains(., 'Placa Base')]}</placa>,"
							+ "<micro>{/productos/produc/denominacion[contains(., 'Micro')]}</micro>,"
							+ "<memoria>{/productos/produc/denominacion[contains(., 'Memoria')]}</memoria>,"
							+ "<otros>{/productos/produc/denominacion[not(contains(., 'Memoria') or contains(., 'Micro') or contains(., 'Placa Base'))]}</otros>)").getIterator();
					
					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();
						XMLResource nodo = ((XMLResource) resourceIterator.nextResource());
						System.out.println(nodo.getContent());
					}
						
					System.out.println();
					
					
					System.out.println("\n *****************************************************************************************************************************");
					
					
					System.out.println("******************** CONSUTLAS SUCURSALES ********************");

					//Devuelve el código de sucursal y el número de cuentas que tiene de tipo AHORRO y de tipo pensiones.
					resourceIterator = xqueryService.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), count($suc/cuenta[data(@tipo)='AHORRO']), count($suc/cuenta[data(@tipo)='PENSIONES']))").getIterator();
					
					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();
						XMLResource nodo = ((XMLResource) resourceIterator.nextResource());
						System.out.print("La sucursal " + nodo.getContent());

							if (resourceIterator.hasMoreResources()) {
								
								XMLResource n2 = (XMLResource) resourceIterator.nextResource();
								System.out.print(" tiene " + n2.getContent() + " cuentas tipo AHORRO y ");
						}

							if (resourceIterator.hasMoreResources()) {
								
								XMLResource n2 = (XMLResource) resourceIterator.nextResource();
								System.out.println(n2.getContent() + " cuentas tipo PENSIONES");
						}
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					

					//Devuelve por cada sucursal el código de sucursal, el director, la población, la suma del total saldodebe y la suma del total saldohaber de sus cuentas.
					resourceIterator = xqueryService.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text(), sum($suc/cuenta/saldodebe), sum($suc/cuenta/saldohaber))").getIterator();

					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();

						XMLResource res = (XMLResource) resourceIterator.nextResource();
						System.out.println("Código: " + res.getContent());

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Director: " + res.getContent());
						}

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Población: " + res.getContent());
						}

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Total saldodebe: " + res.getContent());
						}

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Total saldohaber: " + res.getContent());
						}
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					

					//Devuelve el nombre de los directores, el código de sucursal y la población de las sucursales con más de 3 cuentas.
					resourceIterator = xqueryService.query("for $suc in /sucursales/sucursal[count(cuenta) > 3] return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text())").getIterator();

					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();

						XMLResource res = (XMLResource) resourceIterator.nextResource();
						System.out.println("Código: " + res.getContent());

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Director: " + res.getContent());
						}

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Población: " + res.getContent());
						}
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					

					//Devuelve por cada sucursal, el código de sucursal y los datos de las cuentas con más saldodebe.
					resourceIterator = xqueryService.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/cuenta[saldodebe = max($suc/cuenta/saldodebe)]/*/text())").getIterator();

					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();

						XMLResource res = (XMLResource) resourceIterator.nextResource();
						System.out.println("Cuenta con más saldodebe de la sucursal " + res.getContent());

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Nombre: " + res.getContent());
						}

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Número: " + res.getContent());
						}

							if (resourceIterator.hasMoreResources()) {
								
								res = (XMLResource) resourceIterator.nextResource();
								System.out.println("Saldohaber: " + res.getContent());
						}

						if (resourceIterator.hasMoreResources()) {
							res = (XMLResource) resourceIterator.nextResource();
							System.out.println("Saldodebe: " + res.getContent());
						}
					}
					
					System.out.println();
					System.out.println("*****************************************************************************************************************************");
					
					

					//Devuelve la cuenta del tipo PENSIONES que ha hecho más aportación.
					resourceIterator = xqueryService.query("/sucursales/sucursal/cuenta[data(@tipo) = 'PENSIONES' and aportacion = max(/sucursales/sucursal/cuenta/aportacion)]").getIterator();

					while (resourceIterator.hasMoreResources()) {
						
						System.out.println();

						XMLResource res = (XMLResource) resourceIterator.nextResource();
						System.out.println(res.getContent());
					}
					
					System.out.println();
					
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			
		}
		
    	}
}
