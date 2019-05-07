# Implementación del sistema visual en el middleware

La primera versión pública de la implementación del sistema sensorial visual 

## Características
 * Incorporación de la implementación de Madrigal del sistema visual con las nuevas funciones
 * Como es la primera versión, aún no tiene un enfoque distribuido
 * Incorporación de un _FrameActivity_ para visualizar el contenido visual
 * Tiene un generador de nodos y procesos automático en el paquete _src/generator_
 * El primer frame es una ventana que representa el ojo, las imagenes cambian si se le da click, las imagenes que aparecerán ahí están en la carpeta _images_
 
 ## Requerimientos
 
 * Tener en el proyecto el archivo opencv_java330.dll para Windows, si se usa otro sistema, reemplazar por el archivo equivalente al DLL
 * Tener el jar _kmiddle.jar_
 * El proyecto fué hecho en NetBeans, así que es mas facil que funcione si tienen NetBeans
