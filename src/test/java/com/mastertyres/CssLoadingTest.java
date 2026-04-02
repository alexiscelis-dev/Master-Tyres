package com.mastertyres;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CssLoadingTest {

    private static final String[] CSS_FILES = {
            "/styles-css/Nota.css",
            "/styles-css/ClientesStyles.css",
            "/styles-css/Inventario.css",
            "/styles-css/PaginaPrincipal.css",
            "/styles-css/stylesLogin.css",
            "/styles-css/Historial.css",
            "/styles-css/PromocionesStyles.css"
    };

    private static final String[][] FXML_CSS_REFERENCES = {
            {"/fxmlViews/nota/NotaFormulario.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/nota/Nota.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/nota/NotaDetalles.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/nota/RegistrarNota.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/nota/BuscarCliente.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/nota/BuscarLlanta.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/nota/EditarNota.fxml", "/styles-css/Nota.css"},
            {"/fxmlViews/cliente/Cliente.fxml", "/styles-css/ClientesStyles.css"},
            {"/fxmlViews/inventario/Inventario.fxml", "/styles-css/Inventario.css"}
    };

    @Test
    void testCssFilesExistInClasspath() {
        for (String cssFile : CSS_FILES) {
            URL resourceUrl = getClass().getResource(cssFile);
            assertNotNull(resourceUrl, "CSS file should exist in classpath: " + cssFile);
        }
    }

    @Test
    void testNotaCssCanBeLoadedAsUrl() {
        URL cssUrl = getClass().getResource("/styles-css/Nota.css");
        assertNotNull(cssUrl, "Nota.css should be loadable as URL");
    }

    @Test
    void testNotaCssExternalForm() {
        URL cssUrl = getClass().getResource("/styles-css/Nota.css");
        assertNotNull(cssUrl);
        String externalForm = cssUrl.toExternalForm();
        assertNotNull(externalForm);
        assertTrue(externalForm.contains("styles-css/Nota.css"),
                "External form should contain CSS path: " + externalForm);
    }

    @Test
    void testNotaFormularioFxmlHasCssReference() throws IOException {
        URL fxmlUrl = getClass().getResource("/fxmlViews/nota/NotaFormulario.fxml");
        assertNotNull(fxmlUrl, "NotaFormulario.fxml should exist");

        String fxmlContent = readResource(fxmlUrl);
        assertTrue(fxmlContent.contains("stylesheets"),
                "NotaFormulario.fxml should contain stylesheets attribute");
        assertTrue(fxmlContent.contains("Nota.css"),
                "NotaFormulario.fxml should reference Nota.css");
    }

    @Test
    void testAllNotaRelatedFxmlHaveCssReference() throws IOException {
        List<String> missingCss = new ArrayList<>();

        for (String[] fxmlCss : FXML_CSS_REFERENCES) {
            String fxmlFile = fxmlCss[0];
            String expectedCss = fxmlCss[1];

            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl != null) {
                String fxmlContent = readResource(fxmlUrl);
                if (!fxmlContent.contains("stylesheets")) {
                    missingCss.add(fxmlFile + " (missing stylesheets attribute)");
                } else if (!fxmlContent.contains(expectedCss)) {
                    missingCss.add(fxmlFile + " (should reference " + expectedCss + ")");
                }
            } else {
                missingCss.add(fxmlFile + " (file not found)");
            }
        }

        assertTrue(missingCss.isEmpty(),
                "FXML CSS configuration issues: " + missingCss);
    }

    @Test
    void testNotaCssFileIsReadable() throws IOException {
        URL cssUrl = getClass().getResource("/styles-css/Nota.css");
        assertNotNull(cssUrl);

        String cssContent = readResource(cssUrl);
        assertFalse(cssContent.isEmpty(), "Nota.css should not be empty");

        assertTrue(cssContent.contains(".vBoxNumNota") ||
                        cssContent.contains(".text-field-nota") ||
                        cssContent.contains(".boton-accion"),
                "Nota.css should contain expected style classes (.vBoxNumNota, .text-field-nota, .boton-accion)");
    }

    @Test
    void testNotaCssContainsExpectedStyles() throws IOException {
        URL cssUrl = getClass().getResource("/styles-css/Nota.css");
        assertNotNull(cssUrl);

        String cssContent = readResource(cssUrl);

        String[] expectedStyles = {
                ".vBoxNumNota",
                ".vBoxInfo",
                ".text-field-nota",
                ".boton-accion",
                ".check-box",
                ".scrollNota-pane",
                ".scrollNota-pane .viewport"
        };

        List<String> missingStyles = new ArrayList<>();
        for (String style : expectedStyles) {
            if (!cssContent.contains(style)) {
                missingStyles.add(style);
            }
        }

        assertTrue(missingStyles.isEmpty(),
                "Nota.css is missing these expected styles: " + missingStyles);
    }

    @Test
    void testFxmlStylesheetPathFormat() throws IOException {
        URL fxmlUrl = getClass().getResource("/fxmlViews/nota/NotaFormulario.fxml");
        assertNotNull(fxmlUrl);

        String fxmlContent = readResource(fxmlUrl);

        assertTrue(fxmlContent.contains("stylesheets=\"/styles-css/Nota.css\"") ||
                        fxmlContent.contains("stylesheets=\"@/styles-css/Nota.css\"") ||
                        fxmlContent.contains("stylesheets=\"/styles-css/Nota.css\""),
                "NotaFormulario.fxml should use correct stylesheet path format (/styles-css/Nota.css or @/styles-css/Nota.css)");
    }

    private String readResource(URL url) throws IOException {
        try (InputStream is = url.openStream();
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
