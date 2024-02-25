/**
 * INSTITUTO TECNOLÓGICO NACIONAL DE MÉXICO CAMPUS LEÓN
 * INGENIERÍA EN SISTEMAS COMPUTACIONALES
 * 24 DE FEBRERO DE 2024
 *
 * ELABORADO POR: SAMUEL ARTURO GARCÍA HERNÁNDEZ
 * MATERIA: LABORATORIO PARA EL DESPLIEGUE DE APLICACIONES
 *
 * EL PRESENTE CÓDIGO FUE ELABORADO PARA DEMOSTRAR EL CONSUMO DE API CON JAVA
 * SE CONSULTA LA API DE CHISTES MEDIANTE DOS BOTONES
 * "CHISTE ALEATORIO" O "CHISTE POR TEMA"
 *
 */
package programa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChistesMalosGUI extends JFrame {

    /**
     * Constructor de la clase.
     */
    public ChistesMalosGUI() {
        setTitle("Generador de Chistes Malos");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Botones para generar chistes aleatorios o por tema
        JButton chisteRandomBut = new JButton("Chiste Aleatorio");
        JButton temaChisteBut = new JButton("Chiste por Tema");

        // Configuración de colores para los botones
        chisteRandomBut.setBackground(new Color(255, 217, 102)); // Amarillo claro
        temaChisteBut.setBackground(new Color(255, 102, 102)); // Rojo claro

        // Acciones de los botones
        chisteRandomBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Llamada al método para obtener chistes aleatorios
                    String chiste = generarChiste(true, "");
                    mostrarChiste(chiste);
                } catch (Exception ex) {
                    mostrarError("Error al obtener chiste aleatorio.");
                }
            }
        });

        temaChisteBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solicitar al usuario un tema para el chiste
                String tema = JOptionPane.showInputDialog("Ingresa el tema para el chiste:");
                if (tema != null && !tema.isEmpty()) {
                    try {
                        // Llamada al método para obtener chistes por tema
                        String chiste = generarChiste(false, tema);
                        mostrarChiste(chiste);
                    } catch (Exception ex) {
                        mostrarError("Error al obtener chiste sobre el tema: " + tema);
                    }
                } else {
                    mostrarError("Tema no válido");
                }
            }
        });

        // Configuración de fuentes para los botones
        chisteRandomBut.setFont(new Font("Arial", Font.BOLD, 16));
        temaChisteBut.setFont(new Font("Arial", Font.BOLD, 16));

        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(chisteRandomBut);
        panel.add(temaChisteBut);

        // Configuración de fondo del panel
        panel.setBackground(new Color(173, 216, 230)); // Azul claro

        // Agregar panel a la interfaz
        add(panel);
        setVisible(true);
    }

    /**
     * Método para generar chistes aleatorios o por tema.
     *
     * @param esAleatorio Indica si se desea obtener un chiste aleatorio.
     * @param tema Tema para buscar chistes por tema.
     * @return Cadena con el chiste obtenido.
     * @throws Exception Excepción en caso de error en la conexión o lectura.
     */
    private String generarChiste(boolean esAleatorio, String tema) throws Exception {
        String apiUrl;
        if (esAleatorio) {
            apiUrl = "https://icanhazdadjoke.com/";
        } else {
            apiUrl = "https://icanhazdadjoke.com/search?term=" + tema;
        }

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "text/plain");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Método para mostrar el chiste en un cuadro de diálogo.
     *
     * @param chiste Chiste a mostrar.
     */
    private void mostrarChiste(String chiste) {
        JTextArea textArea = new JTextArea(chiste);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Chiste", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Método para mostrar mensajes de error en un cuadro de diálogo.
     *
     * @param mensaje Mensaje de error.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Método principal para iniciar la aplicación.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChistesMalosGUI();
            }
        });
    }
}
