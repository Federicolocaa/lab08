package it.unibo.mvc;

import it.unibo.mvc.api.DrawNumber;
import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;
import it.unibo.mvc.view.DrawNumberSwingView; // Necessario per 'instanceof'

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Application entry-point.
 * Carica dinamicamente le viste usando la riflessione.
 */
public final class LaunchApp {

    // Costanti per zittire Checkstyle (errore MagicNumber e Stringhe)
    private static final String VIEW_PACKAGE = "it.unibo.mvc.view.DrawNumber";
    private static final String SWING_VIEW_NAME = "SwingView";
    private static final String OUTPUT_VIEW_NAME = "StandardOutputView";
    private static final int COPIES = 3;

    private LaunchApp() {
        // Costruttore privato per classe 'utility' (per Checkstyle)
    }

    /**
     * Runs the application.
     *
     * @param args command line arguments, ignored.
     * @throws ClassNotFoundException if the fetches class does not exist
     * @throws NoSuchMethodException if the 0-ary constructor do not exist
     * @throws InvocationTargetException if the constructor throws exceptions
     * @throws InstantiationException if the constructor throws exceptions
     * @throws IllegalAccessException in case of reflection issues
     */
    public static void main(final String... args)
            throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        final DrawNumber model = new DrawNumberImpl();
        final DrawNumberController app = new DrawNumberControllerImpl(model);

        final List<String> viewClassNames = List.of(OUTPUT_VIEW_NAME, SWING_VIEW_NAME);

        for (final String viewName : viewClassNames) {
            final Class<?> clazz = Class.forName(VIEW_PACKAGE + viewName);
            final Constructor<?> ctor = clazz.getConstructor();
            for (int i = 0; i < COPIES; i++) {
                final Object newViewObject = ctor.newInstance();
                if (DrawNumberView.class.isAssignableFrom(newViewObject.getClass())) {
                    final DrawNumberView view = (DrawNumberView) newViewObject;
                    app.addView(view);
                    if (newViewObject instanceof DrawNumberSwingView) {
                        view.setController(app);
                        view.start();
                    }
                } else {
                    throw new IllegalStateException(
                        newViewObject.getClass() + " is not a subclass of " + DrawNumberView.class
                    );
                }
            }
        }
    }
}
