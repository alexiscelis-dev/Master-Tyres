# Master-Tyres

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Supabase](https://img.shields.io/badge/Supabase-Auth-black)

> Sistema de gestión integral para talleres mecánicos y llanteras. Aplicación de escritorio en Java con Spring Boot, JavaFX, MySQL y Supabase (PostgreSQL).

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Módulos](#-módulos)
- [Generación de Ejecutable](#-generación-de-ejecutable)
- [Licencia](#-licencia)

---

## ✨ Características

- **Gestión de Clientes**: Registro completo de clientes (personas y empresas)
- **Administración de Vehículos**: Control de vehículos por cliente con historial
- **Sistema de Notas**: Ventas y servicios con créditos, adeudos y plazos de pago
- **Inventario de Llantas**: Stock, precios, medidas, códigos de barras e imágenes
- **Catálogo de Marcas/Modelos**: Gestión de marcas y modelos de vehículos
- **Sistema de Promociones**: Descuentos por cliente o vehículo
- **Generación de PDFs**: Documentación de notas y servicios
- **Respaldos Automáticos**: Backups con MySQLDump + sincronización a Supabase Storage
- **Autenticación Segura**: Spring Security con BCrypt + Supabase Auth
- **Interfaz Personalizada**: UI moderna sin bordes con barra de título custom
- **Notificaciones**: Alertas para próximos servicios de vehículos

---

## 🛠️ Tecnologías

### Backend
| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.5.3 |
| Spring Security | - |
| Spring Data JPA | - |
| Hibernate | 6.6.18 |

### Frontend
| Tecnología | Versión |
|------------|---------|
| JavaFX | 21.0.3 |
| FXML | - |
| CSS3 | - |

### Base de Datos
| Tecnología | Propósito |
|------------|-----------|
| MySQL | Base de datos principal |
| Supabase | Autenticación + Storage |
| Hibernate | ORM |

### Bibliotecas
| Biblioteca | Propósito |
|------------|-----------|
| iText 7 | Generación de PDFs |
| Lombok | Reducción de boilerplate |
| JWT (jjwt) | Tokens de autenticación |
| BCrypt | Encriptación de contraseñas |
| Passay | Validación de contraseñas |

### Build & Deploy
| Herramienta | Propósito |
|------------|-----------|
| Maven | Gestión de dependencias |
| JPackage | Generador de instaladores Windows |
| JavaFX Maven Plugin | Soporte JavaFX |

---

## 📁 Estructura del Proyecto

```
Master-Tyres/
├── database/
│   └── master_tires_db.sql           # Esquema de base de datos
├── src/main/java/com/mastertyres/
│   ├── MasterTyresApplication.java    # Punto de entrada
│   ├── auth/                         # Autenticación
│   ├── storage/                      # Almacenamiento (Supabase)
│   ├── security/                     # Seguridad
│   ├── config/                       # Configuración
│   ├── cliente/                      # Módulo de Clientes
│   ├── vehiculo/                     # Módulo de Vehículos
│   ├── nota/                         # Módulo de Notas
│   ├── notaDetalle/                  # Detalles de notas
│   ├── notaClienteDetalle/           # Info adicional de clientes
│   ├── inventario/                    # Módulo de Inventario
│   ├── marca/                        # Marcas de vehículos
│   ├── modelo/                       # Modelos de vehículos
│   ├── categoria/                    # Categorías de productos
│   ├── detalleCategoria/              # Detalles de categorías
│   ├── promociones/                  # Sistema de Promociones
│   ├── vehiculoPromocion/            # Promociones por vehículo
│   ├── clientesPromocion/            # Promociones por cliente
│   ├── respaldo/                     # Sistema de Respaldos
│   ├── user/                         # Gestión de Usuarios
│   ├── components/                   # Componentes reutilizables
│   ├── controllers/fxControllers/     # Controladores JavaFX
│   │   ├── login/                    # Login
│   │   ├── cliente/                  # Clientes
│   │   ├── vehiculo/                 # Vehículos
│   │   ├── nota/                     # Notas
│   │   ├── imprimirNota/             # Impresión de notas
│   │   ├── inventario/               # Inventario
│   │   ├── Promociones/              # Promociones
│   │   ├── historial/                # Historial
│   │   ├── configuracion/            # Configuración
│   │   └── ...
│   └── common/                       # Utilidades y excepciones
├── src/main/resources/
│   ├── fxmlViews/                   # Vistas FXML
│   ├── fxmlComponentes/              # Componentes FXML
│   ├── styles-css/                   # Estilos CSS
│   ├── icons/                        # Iconos e imágenes
│   └── application.properties        # Configuración
├── src/test/java/                    # Tests unitarios
└── pom.xml                           # Dependencias Maven
```

---

## 📦 Requisitos

### Software
- **JDK 21** o superior
- **MySQL 8.0** o superior
- **Maven 3.6+**
- **Windows 10/11** (para instalación)

---

## 🚀 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/Master-Tyres.git
cd Master-Tyres/Master-Tyres
```

### 2. Configurar base de datos

El proyecto incluye un archivo SQL con el esquema completo de la base de datos. Ejecuta el script en tu servidor MySQL antes de iniciar la aplicación:

```bash
mysql -u root -p < database/master_tires_db.sql
```

### 3. Configurar variables de entorno

Consulta la sección [Configuración](#-configuración) para ver todas las variables requeridas.

### 4. Compilar proyecto
```bash
mvn clean compile
```

### 5. Ejecutar aplicación

**Opción A: Con Maven (requiere JDK con JavaFX)**
```bash
mvn javafx:run
```

**Opción B: JAR compilado**
```bash
java -jar target/master-tyres-0.0.1-SNAPSHOT.jar
```

**Opción C: Desde IDE**
Abrir el proyecto en IntelliJ IDEA, Eclipse o VS Code y ejecutar la clase `MasterTyresApplication.java`.

---

## ⚙️ Configuración

### Variables de Entorno

La aplicación requiere las siguientes variables de entorno para funcionar:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DB_DATABASE` | Nombre de la base de datos MySQL | `master_tyres` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | `tu_password` |
| `SUPABASE_URL` | URL del proyecto Supabase | `https://xyz.supabase.co` |
| `SUPABASE_APIKEY` | API Key de Supabase | `eyJhbGciOi...` |
| `SUPABASE_BUCKET` | Nombre del bucket de storage | `archivos` |
| `MYSQL_DUMP_RUTA` | Ruta al ejecutable mysqldump | `C:\mysql\bin\mysqldump.exe` |

### Configurar en Windows (PowerShell)

```powershell
# Crear variables de forma permanente
[Environment]::SetEnvironmentVariable("DB_DATABASE", "master_tyres", "User")
[Environment]::SetEnvironmentVariable("DB_USER", "root", "User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "tu_password", "User")
[Environment]::SetEnvironmentVariable("SUPABASE_URL", "https://tu-proyecto.supabase.co", "User")
[Environment]::SetEnvironmentVariable("SUPABASE_APIKEY", "tu-api-key", "User")
[Environment]::SetEnvironmentVariable("SUPABASE_BUCKET", "bucket-name", "User")
[Environment]::SetEnvironmentVariable("MYSQL_DUMP_RUTA", "C:\mysql\bin\mysqldump.exe", "User")

# Reiniciar terminal/IDE para aplicar cambios
```

### Configurar en Windows (CMD)

```cmd
setx DB_DATABASE "master_tyres"
setx DB_USER "root"
setx DB_PASSWORD "tu_password"
setx SUPABASE_URL "https://tu-proyecto.supabase.co"
setx SUPABASE_APIKEY "tu-api-key"
setx SUPABASE_BUCKET "bucket-name"
setx MYSQL_DUMP_RUTA "C:\mysql\bin\mysqldump.exe"
```

### Configurar en IntelliJ IDEA

1. **Run > Edit Configurations**
2. Seleccionar la configuración de la clase `MasterTyresApplication`
3. En la pestaña **Environment**, hacer clic en **Variable environment**
4. Agregar cada variable:
   ```
   DB_DATABASE=master_tyres
   DB_USER=root
   DB_PASSWORD=tu_password
   SUPABASE_URL=https://tu-proyecto.supabase.co
   SUPABASE_APIKEY=tu-api-key
   SUPABASE_BUCKET=bucket-name
   MYSQL_DUMP_RUTA=C:\mysql\bin\mysqldump.exe
   ```
5. Aplicar y ejecutar

### Configurar en Eclipse

1. Click derecho en proyecto > **Run As > Run Configurations**
2. Seleccionar **Java Application**
3. En la pestaña **Environment**, hacer clic en **New** y agregar cada variable
4. Aplicar y ejecutar

### Configurar para JAR (línea de comandos)

```bash
java -jar target/master-tyres-0.0.1-SNAPSHOT.jar ^
  -DDB_DATABASE=master_tyres ^
  -DDB_USER=root ^
  -DDB_PASSWORD=tu_password ^
  -DSUPABASE_URL=https://tu-proyecto.supabase.co ^
  -DSUPABASE_APIKEY=tu-api-key
```

### Configurar para JAR (archivo .env)

Crear archivo `.env` en el mismo directorio del JAR:

```env
DB_DATABASE=master_tyres
DB_USER=root
DB_PASSWORD=tu_password
SUPABASE_URL=https://tu-proyecto.supabase.co
SUPABASE_APIKEY=tu-api-key
SUPABASE_BUCKET=bucket-name
MYSQL_DUMP_RUTA=C:\mysql\bin\mysqldump.exe
```

> **Nota:** Para usar `.env`, necesitarías pasar las variables al iniciar el JAR o configurar el sistema para leerlas.

---

### application.properties

```properties
# Conexión MySQL
spring.datasource.url=jdbc:mysql://localhost:3307/${DB_DATABASE}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

# Supabase
supabase.url=${SUPABASE_URL}
supabase.api.key=${SUPABASE_APIKEY}
supabase.url.bucket=${SUPABASE_BUCKET}

# Respaldos
app.ruta.respaldos=${user.home}/MasterTires/data/respaldos
```

---

## 📊 Módulos

### 🧑 Clientes
Gestión completa de clientes con:
- Datos personales y de contacto
- Historial de servicios
- Saldo y crédito disponible

### 🚗 Vehículos
Administración de vehículos por cliente:
- Marca, modelo, año
- Kilómetros y placas
- Historial de servicios

### 📝 Notas
Sistema de ventas y servicios:
- Detalle de productos y servicios
- Créditos y adeudos
- Plazos de pago configurables
- Generación de PDFs

### 🛞 Inventario
Control de inventario de llantas:
- Stock y precios
- Medidas y códigos de barras
- Imágenes de productos

### 🏷️ Promociones
Sistema de descuentos:
- Por cliente
- Por vehículo
- Descuentos porcentuales o fijos

### 💾 Respaldos
Respaldo automático:
- Exportación MySQLDump
- Sincronización a Supabase Storage
- Historial de respaldos

---

## 📦 Generación de Ejecutable

### Generar JAR (recomendado para desarrollo)
```bash
mvn clean package -DskipTests
```
El JAR se generará en: `target/master-tyres-0.0.1-SNAPSHOT.jar`

### Generar Instalador MSI (Windows)
```bash
mvn jpackage:jpackage
```
El instalador se generará en: `installer-output/`

### Ejecutar JAR
```bash
java -jar target/master-tyres-0.0.1-SNAPSHOT.jar
```

> **Importante:** La aplicación requiere las variables de entorno configuradas. Consulta la sección [Configuración](#-configuración) para más detalles.

---



## 📄 Licencia

Este proyecto es propiedad de **Master Tires**. Todos los derechos reservados.

---

## 👤 Autores

- **Ing. Raphael Alexis Celis Torres**
- **Ing. Jayro de Jesús Echeverría Mena**

**Master Tires** - Cliente

---

<p align="center">
  <strong>Master-Tyres</strong> — Sistema de Gestión para Talleres Mecánicos y Llanteras
</p>
