# SonrisaClinic — Prototipo de gestión odontológica (JavaFX)

Prototipo funcional de escritorio para un consultorio odontológico, construido con **JavaFX 21**,
tema visual **AtlantaFX** (estilo moderno tipo Primer/GitHub) e iconos **Ikonli Feather**.
Los datos se simulan **en memoria** (sin base de datos) para efectos del prototipo.

## Funcionalidades incluidas

1. **Inicio de sesión** — autenticación contra usuarios de prueba.
2. **Registrar paciente** — formulario completo + tabla de pacientes registrados.
3. **Buscar historia clínica** — búsqueda por nombre/documento y vista consolidada
   (procedimientos, solicitudes a especialista, citas y pagos) en pestañas.
4. **Registrar procedimientos** — asocia procedimientos a un paciente (pieza, odontólogo, costo, estado).
5. **Solicitud con especialista** — remisión de un paciente a un especialista (interno o externo).
6. **Asignar cita** — programación de nuevas citas.
7. **Cobro de cita** — registra el pago de una cita pendiente y la marca como pagada.

## Requisitos

- **JDK 17 o superior** (recomendado JDK 21)
- **Maven 3.8+**
- Conexión a internet la primera vez (para descargar JavaFX, AtlantaFX e Ikonli desde Maven Central)

> Nota: este prototipo se generó en un entorno sin acceso a Maven Central, por lo que el código
> no pudo compilarse/ejecutarse aquí. Está escrito siguiendo cuidadosamente las APIs oficiales de
> JavaFX, AtlantaFX e Ikonli; si al compilar en tu máquina aparece algún error puntual, dime el
> mensaje exacto y lo corrijo de inmediato.

## Cómo ejecutarlo

```bash
cd dental-clinic-app
mvn clean javafx:run
```

## Usuarios de prueba

| Usuario     | Contraseña | Rol            |
|-------------|------------|----------------|
| admin       | admin123   | Administrador  |
| recepcion   | 1234       | Recepción      |

## Estructura del proyecto

```
dental-clinic-app/
├── pom.xml
└── src/main/
    ├── java/com/clinic/
    │   ├── MainApp.java                 # Punto de entrada
    │   ├── model/                       # Patient, Appointment, Payment, etc.
    │   ├── data/DataStore.java          # Repositorio en memoria (mock) + datos de ejemplo
    │   └── controller/                  # Un controlador por pantalla
    └── resources/com/clinic/
        ├── view/*.fxml                  # Pantallas (login, shell, cada módulo)
        └── css/styles.css               # Estilos modernos sobre AtlantaFX
```

## Siguientes pasos sugeridos

- Reemplazar `DataStore` por persistencia real (JPA/Hibernate + PostgreSQL o SQLite) manteniendo
  la misma interfaz pública, para no tener que tocar los controladores.
- Agregar validaciones más estrictas (formato de documento, teléfono, email).
- Agregar roles/permisos reales según el usuario autenticado.
- Exportar historia clínica o comprobante de pago a PDF.
