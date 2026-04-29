v1.1.0

Date 08/04/2026





Changed



* El puerto de MySQL dejo de ser estático y se hizo dinámico haciéndolo una variable de entorno y mandándola desde el instalador.
* Se agrega una variable de entorno para el correo de recuperación de contraseña.
* Corrección de errores de formato en vista de usuario.
* Se hicieron editables los campos de agregar imagen en promoción por vehículos y Clientes.
* Se agrego la propiedad   "-fx-font-weight: bold;" a todos los choice box.
* Se corrige bug de estilos en historial de Notas.
* Se cambia la manera en la que se autentifica al recuperar contraseña, se elimina el usuario para que forzosamente se autentifique en supabase.
* Se cambia la cuenta de "Olvide contraseña" por la de alexisjayro.devtem@gmail.com.
* Se cambio la ruta de guardado de respaldos a C:/MasterTires/${user.home} para que este junto con toda la data de la aplicación.





Fixed



* Se corrigió el bug en agregar promoción por vehículos y por Clientes (Botón agregar imagen deshabilitado).
* Se corrige el problema de licencia al momento de cambiar a SUSPENDED y regresarla -> ACTIVE o LIFETIME.(No se podia volver a cambiar).
* Corrección de bug al actualizar adeudo



Added



* Se agrega métodos enable y disable de la clase TaskService a las clases RegistrarNota, EditarSaldo, EditarAdeudo
* Se agrega clase UserProxyService para manejo de licencia.
* Se agrega sección de eliminar usuarios locales como administrador en el menu de configuraciones.
* Se agrega archivo CHANGELOG.md para registrar los cambios de las actualizaciones.
* Se agrega método de actualizarNextCheck después de validar licencia en authenticate local para cerciorarse que siempre vaya al corriente el campo nextNext.





V1.1.1

Date 15/04/2026



Changed



* Corrección de formato al lanzar excepciones. Se muestran ahora desde el back con formato \*\*Clase + mensaje\*\* y se muestras en mensajes con mostrarExcepcionThrowable.
* Se cambio la ubicación de los respaldos a "C:/MasterTires/respaldos"



Fixed



* Refactorización de código.
* Corrección de año en login derechos Reservados.





V1.1.2

Date 25/04/2026



Fixed



* Corrección de bug visual en "Agregar Vehículo".
* Corrección de bug en "Agregar Llanta" en Nota, dejar solamente ingresar números.
* Corrección de bug Agregaba llanta con stock en 0.

