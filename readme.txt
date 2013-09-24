El programa obtiene las llamadas del UCCX para grabarlas en una tabla de una BD donde pueda ser utilizada para la extracción de reportes.

Lógica:
------
- Obtener las llamadas desde el UCCX
- Eliminar las llamadas almacenadas en la tabla destino
- Restar 5 horas a las fechas de inicio y fin de las llamadas ya que por alguna razón no identificada se obtienen con 5 horas más.
- Eliminar las llamadas que se encuentren fuera del rango de fechas solicitado. Debido a que las llamadas se obtienen con 5 horas más, al restarles las 5 horas pueden quedar llamadas del día anterior. Ejm: se obtienen las llamadas del día 2013/08/12 de las cuales una llamada tiene fecha de inicio 2013/08/12 01:00:00 am al restarle 5 horas veriamos que esta llamada fue realizada realmente el 2013/08/11 08:00:00 pm. Esta llamada no es realmente del día que queremos obtener llamadas.
- Eliminar Llamadas Duplicadas: en algunos casos las llamadas vienen duplicadas (mismo id). En este caso se deben eliminar las llamadas cuyo tiempo de hablado es cero.
- Guardar llamadas en la tabla de destino indicada en el archivo de configuración

Estructura de carpetas:
----------------------
bin: almacena los archivos compilados del programa.
conf: contiene el archivo de configuración con parametros configurables para el programa
lib: contiene las librerias externas utilizadas. En este caso solo dos drivers de conexion a Informix y SQLServer
src: codigo fuente del programa