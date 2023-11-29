import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Registro {

    private static final String FILENAME = "credenciales.cre";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Registrar Clases.Usuario");
            System.out.println("2. Iniciar Sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea después del número

            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    System.out.println("Saliendo del programa.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }

    private static void registrarUsuario() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Registro de Clases.Usuario");
        System.out.print("Ingrese su identificador:");
        String identificador = scanner.nextLine();

        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        String hashContrasena = calcularHash(contrasena);

        Map<String, String> credenciales = cargarCredenciales();

        if (credenciales.containsKey(identificador)) {
            System.out.println("Nombre de usuario en uso. Por favor, elija otro.");
            return;
        }

        credenciales.put(identificador, hashContrasena);
        guardarCredenciales(credenciales);

        System.out.println("¡Registro exitoso!");
    }

    private static void iniciarSesion() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese su identificador: ");
        String identificador = scanner.nextLine();

        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        String hashContrasena = calcularHash(contrasena);

        Map<String, String> credenciales = cargarCredenciales();

        if (credenciales.containsKey(identificador) && credenciales.get(identificador).equals(hashContrasena)) {
            System.out.println("¡Inicio de sesión exitoso!");
        } else {
            System.out.println("Inicio de sesión fallido. Verifique sus credenciales.");
        }
    }

    private static String calcularHash(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(contrasena.getBytes());

            // Convertir bytes a formato hexadecimal
            StringBuilder hashStringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hashStringBuilder.append(String.format("%02x", hashByte));
            }

            return hashStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, String> cargarCredenciales() {
        Map<String, String> credenciales = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 2) {
                    credenciales.put(partes[0], partes[1]);
                }
            }
        } catch (IOException e) {
            // El archivo no existe o no se puede leer, se ignora
        }

        return credenciales;
    }

    private static void guardarCredenciales(Map<String, String> credenciales) {
        try (FileWriter fw = new FileWriter(FILENAME)) {
            for (Map.Entry<String, String> entry : credenciales.entrySet()) {
                fw.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
